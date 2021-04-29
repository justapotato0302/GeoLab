package com.example.geolab

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.geolab.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class FragmentRegister: Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)

        auth = FirebaseAuth.getInstance()

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

        auth.createUserWithEmailAndPassword(email.text.toString(), email.text.toString())
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    (activity as TestFragmentActivity).showSignInFragment()
                } else {
                    Toast.makeText(requireContext(), "Sign Up failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

}