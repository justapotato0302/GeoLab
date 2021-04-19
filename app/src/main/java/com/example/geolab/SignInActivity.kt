package com.example.geolab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.widget.EditText
import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.w3c.dom.Text

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        auth = FirebaseAuth.getInstance()

        val login_btn: View = findViewById(R.id.login_btn)
        val register_btn: View = findViewById(R.id.register_from_login)
        val email_input = findViewById<EditText>(R.id.username_input)
        val password_input = findViewById<EditText>(R.id.password)

        login_btn.setOnClickListener {
            signIn(email_input, password_input)
        }


        register_btn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }


    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun signIn(email_input: EditText, password_input: EditText) {
        if (email_input.text.toString().isEmpty()) {
            email_input.error = "Please enter email"
            email_input.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email_input.text.toString()).matches()) {
            email_input.error = "Please enter valid email"
            email_input.requestFocus()
            return
        }

        if (password_input.text.toString().isEmpty()) {
            password_input.error = "Please enter password"
            password_input.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(email_input.text.toString(), password_input.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    updateUI(null)
                }
            }
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