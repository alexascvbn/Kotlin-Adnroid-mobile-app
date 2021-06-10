package com.example.bottonavigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.app_bar_main.*

class DashboardActivity : AppCompatActivity() {
    private lateinit var profileTv: TextView
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        auth = FirebaseAuth.getInstance()

        profileTv = findViewById(R.id.profileTv)

        setSupportActionBar(toolbar)
        val actionbar = supportActionBar
        actionbar?.title = "Profile"
        actionbar?.setDisplayHomeAsUpEnabled(true)

        val user = auth.currentUser

        if (user != null) {
            profileTv.setText(user.email)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile2_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if (id == R.id.action_logout) {
            auth.signOut()
        }
        return false
    }

}
