package com.example.bottonavigation

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.app_bar_main.*


class RegisterActivity : AppCompatActivity() {
    lateinit var mEmailET: EditText
    lateinit var mPasswordET: EditText
    private lateinit var mBtnRegister: Button
    private lateinit var auth: FirebaseAuth

    private lateinit var pd: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_register)

        mEmailET = findViewById(R.id.emailET)
        mPasswordET = findViewById(R.id.passwordET)
        mBtnRegister = findViewById(R.id.btnMainRegister)

        pd = findViewById(R.id.determinateBar)
        pd.visibility = View.GONE
        auth = FirebaseAuth.getInstance()

        mBtnRegister.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val email: String = mEmailET.text.toString().trim()
                val password: String = mPasswordET.text.toString().trim()

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmailET.setError("Invalid email")
                    mEmailET.isFocusable = true
                } else if (password.length < 6) {
                    mPasswordET.setError("Password length must at least 6 characters")
                    mPasswordET.isFocusable = true
                } else {
                    register(email, password)
                }
            }
        })

        setSupportActionBar(toolbar)

        val actionbar = supportActionBar

        actionbar?.title = "Register"
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun register(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    pd.visibility = View.VISIBLE
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser


                    val emailTv: String = user?.email.toString()
                    val uid: String = user?.uid.toString()

                    val hasMap = HashMap<String, Any>()

                    hasMap.put("email", emailTv)
                    hasMap.put("uid", uid)
                    hasMap.put("name", "")
                    hasMap.put("phone", "")
                    hasMap.put("image", "")
                    hasMap.put("cover", "")
                    hasMap.put("deliveryAddress", "")
                    hasMap.put("credits", 0)

                    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
                    val reference: DatabaseReference = database.getReference("Users")
                    reference.child(uid).setValue(hasMap)



                    Toast.makeText(
                        this, "Registered...\n" + user?.email,
                        Toast.LENGTH_SHORT
                    ).show()
                    onBackPressed()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    this, "" + it.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}