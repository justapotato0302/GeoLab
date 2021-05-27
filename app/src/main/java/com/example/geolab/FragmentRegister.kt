package com.example.geolab

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.geolab.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FragmentRegister: Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding

    private lateinit var auth: FirebaseAuth

    private lateinit var mDatabase : DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)

        auth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference.child("users")

        val registerBtn: View = binding.registerSubmitBtn
        val emailInput = binding.usernameInput
        val passwordInput = binding.password
        val rePasswordInput = binding.confirmPassword

        registerBtn.setOnClickListener {
            register(emailInput,passwordInput,rePasswordInput)

        }

    }

    private fun register(email: EditText, password: EditText, repassword: EditText){

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

        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    writeNewUser(currentUser.uid,"CHAPTER1_sec1","0")
                    verifyEmail()
                    Toast.makeText(requireContext(), "Registered Successfully.",
                        Toast.LENGTH_SHORT).show()
                    auth.signOut()
                    val action = FragmentRegisterDirections.actionFragmentRegisterToFragmentSignIn()
                    view?.findNavController()?.navigate(action)
                } else {
                    Toast.makeText(requireContext(), "Sign Up failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun writeNewUser(userID: String, currentStage: String, highScore: String){
        val user = User(currentStage, highScore, userID)
        mDatabase.child(userID).setValue(user)
    }

    private fun verifyEmail() {
        val currentUser = auth.currentUser;
        currentUser.sendEmailVerification()
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(),
                        "Verification email sent to " + currentUser.getEmail(),
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(),
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

}