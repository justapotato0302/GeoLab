package com.example.geolab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.geolab.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        auth.signOut()

        val logout_btn : CardView? = binding.logoutBtn

        if (logout_btn != null) {
            logout_btn.setOnClickListener {
                auth.signOut()
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
        }

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

//        val currentUser = auth.currentUser
//        updateUI(currentUser)

    }

//    private fun updateUI(currentUser: FirebaseUser?){
//        if (currentUser != null){
//            startActivity(Intent(this, SignInActivity::class.java))
//        } else {
//            Toast.makeText(
//                baseContext, "Not Signed In.",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }
}