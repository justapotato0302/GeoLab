package com.example.geolab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase

class MainMenuActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        val button : Button = findViewById(R.id.button)
        auth = FirebaseAuth.getInstance()

        button.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        auth = FirebaseAuth.getInstance()

//        val user = auth.currentUser
//        user?.let {
//            // Name, email address, and profile photo Url
//            val name = user.displayName
//            val email = user.email
//            val photoUrl = user.photoUrl
//
//            // Check if user's email is verified
//            val emailVerified = user.isEmailVerified
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getToken() instead.
//            val uid = user.uid
//        }
    }

    public override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        updateUI(currentUser)

    }

    private fun updateUI(currentUser: FirebaseUser?){
        if (currentUser != null){
            startActivity(Intent(this, MainMenuActivity::class.java))
        } else {
            Toast.makeText(
                baseContext, "Not Signed In.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}