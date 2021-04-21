package com.example.geolab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.widget.EditText
import android.util.Patterns
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import org.w3c.dom.Text

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        auth = FirebaseAuth.getInstance()

        //registered email and password signin
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

        //google signin
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val googleBtn:SignInButton = findViewById(R.id.google_signin);
        googleBtn.setSize(SignInButton.SIZE_STANDARD);

        googleBtn.setOnClickListener {
            googleSignIn()
        }



    }

    public override fun onStart() {
        super.onStart()

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

    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val currentUser = auth.currentUser
                    updateUI(currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
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

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
}