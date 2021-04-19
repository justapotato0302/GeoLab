package com.example.geolab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()

        val register_btn: View = findViewById(R.id.register_submit_btn)
        val email_input = findViewById<EditText>(R.id.username_input)
        val password_input = findViewById<EditText>(R.id.password)
        val repassword_input = findViewById<EditText>(R.id.confirm_password)

        register_btn.setOnClickListener {
            register(email_input,password_input,repassword_input)
        }



    }

    private fun register(email:EditText, password:EditText, repassword:EditText){

        if (email.text.toString().isEmpty()) {
            email.error = "Please enter email"
            email.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.error = "Please enter valid email"
            email.requestFocus()
            return
        }

        if (password.text.toString().isEmpty()) {
            password.error = "Please enter password"
            password.requestFocus()
            return
        }

        if (repassword.text.toString().isEmpty()) {
            repassword.error = "Please enter password"
            repassword.requestFocus()
            return
        }

        if (repassword.text.toString() != password.text.toString()) {
            repassword.error = "Passwords do not match"
            repassword.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(email.text.toString(), email.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this,SignInActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(baseContext, "Sign Up failed.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }
}