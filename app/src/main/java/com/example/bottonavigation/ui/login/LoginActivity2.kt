package com.example.bottonavigation.ui.login

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import com.example.bottonavigation.R
import com.example.bottonavigation.util.SharedPref
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.bottonavigation.*
import com.example.bottonavigation.adapter.MainPagerAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.profile_prompt.*


class LoginActivity2 : AppCompatActivity() {
    private lateinit var mEmailET: EditText
    private lateinit var mPasswordET: EditText
    private lateinit var btnSignIn: Button
    private lateinit var tvSignUp: TextView
    private lateinit var mRecoverPassTv: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var pd: ProgressBar
    private lateinit var mGoogleLoginBtn: SignInButton
    val RC_SIGN_IN: Int = 100
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var gso: GoogleSignInOptions
    private lateinit var database : FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        mEmailET = findViewById(R.id.etLoginEmail)
        mPasswordET = findViewById(R.id.etLoginassword)
        btnSignIn = findViewById(R.id.btnMainLogin)
        tvSignUp = findViewById(R.id.tvSignUp)
        mRecoverPassTv = findViewById(R.id.recoverPassTv)
        pd = findViewById(R.id.determinateBar)
        pd.visibility = View.GONE
        mGoogleLoginBtn = findViewById(R.id.googleLoginBtn)

        tvSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }

        mRecoverPassTv.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Forgot Password")

            val linearLayout = LinearLayout(this)
            linearLayout.orientation = LinearLayout.VERTICAL
            linearLayout.setPadding(10, 10, 10, 10)
            builder.setView(linearLayout)

            val view = layoutInflater.inflate(R.layout.dialog_forget_password, null)
            val username = view.findViewById<EditText>(R.id.email_forget_password)
            builder.setView(view)
            builder.run {
                setPositiveButton("Reset") { _, _ ->
                    forgotPassword(username)
                }
                setNeutralButton(R.string.cancel) { _, _ ->

                }
            }
            builder.show()
        }

        mGoogleLoginBtn.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }


        setSupportActionBar(toolbar)

        val actionbar = supportActionBar
        actionbar?.title = "Login"
        actionbar?.setDisplayHomeAsUpEnabled(true)

        // Configure Google Sign In
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


        FirebaseAuth.AuthStateListener { firebaseAuth ->
            val mFirebaseUser = firebaseAuth.currentUser
            if (mFirebaseUser != null) {
                Toast.makeText(this@LoginActivity2, "You are Logged in", Toast.LENGTH_SHORT)
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                Toast.makeText(this@LoginActivity2, "Please Login", Toast.LENGTH_SHORT)
            }
        }
        btnSignIn.setOnClickListener {
            val email: String = mEmailET.text.toString().trim()
            val password: String = mPasswordET.text.toString().trim()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                mEmailET.setError("Invalid email")
                mEmailET.isFocusable = true
            } else if (password.length < 6) {
                mPasswordET.setError("Password length must at least 6 characters")
                mPasswordET.isFocusable = true
            } else {
                pd.visibility = View.VISIBLE
                LoginUser(email, password)
            }
        }


    }

    private fun forgotPassword(username: EditText) {
        if (username.text.toString().isEmpty()) {
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()) {
            Toast.makeText(
                this, "Invalid Email.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        pd.visibility = View.VISIBLE

        auth.sendPasswordResetEmail(username.text.toString())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    pd.visibility = View.GONE
                    Toast.makeText(
                        this, "Email sent.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    pd.visibility = View.GONE
                    Toast.makeText(
                        this, "Failed....",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.addOnFailureListener {
                pd.visibility = View.GONE
                Toast.makeText(
                    this, "" + it.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


    private fun LoginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) { // Sign in success, update UI with the signed-in user's information
                        pd.visibility = View.GONE
                        val user = auth.currentUser

                        // new user
                        if (task.getResult()?.additionalUserInfo!!.isNewUser) {
                            val email: String = user?.email.toString()
                            val uid: String = user?.uid.toString()

                            val hasMap = HashMap<String, Any>()

                            hasMap.put("email", email)
                            hasMap.put("uid", uid)
                            hasMap.put("name", "")
                            hasMap.put("phone", "")
                            hasMap.put("image", "")
                            hasMap.put("cover", "")
                            hasMap.put("deliveryAddress", "")
                            hasMap.put("credits", 0)

                            val reference: DatabaseReference = database.getReference("Users")
                            reference.child(uid).setValue(hasMap)

                        }

                        Toast.makeText(
                            this, "Login Success " + user?.email,
                            Toast.LENGTH_SHORT
                        ).show()
                        onBackPressed()
//                        finish()
                    } else { // If sign in fails, display a message to the user.
                        pd.visibility = View.GONE
                        Toast.makeText(
                            this, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }).addOnFailureListener {
                pd.visibility = View.GONE
                Toast.makeText(
                    this, it.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(
                    this, "Google Login Error : ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun finish() {
         super.finish()
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser

                    if (task.getResult()?.additionalUserInfo!!.isNewUser) {
                        val email: String = user?.email.toString()
                        val uid: String = user?.uid.toString()

                        val hasMap = HashMap<String, Any>()

                        hasMap.put("email", email)
                        hasMap.put("uid", uid)
                        hasMap.put("name", "")
                        hasMap.put("phone", "")
                        hasMap.put("image", "")
                        hasMap.put("cover", "")
                        hasMap.put("deliveryAddress", "")
                        hasMap.put("credits", 0)

                        val reference: DatabaseReference = database.getReference("Users")
                        reference.child(uid).setValue(hasMap)

                    }
                    Toast.makeText(
                        this, "" + user?.email,
                        Toast.LENGTH_SHORT
                    ).show()
                    // back to previous page
//                    startActivity(Intent(this, MainActivity::class.java))
//                    finish()
                    onBackPressed()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        this, "Login Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // ...
            }.addOnFailureListener {
                Toast.makeText(
                    this, "" + it.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}