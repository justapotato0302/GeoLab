package com.example.geolab

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.geolab.databinding.FragmentResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FragmentResetPassword: Fragment(R.layout.fragment_reset_password) {

    private lateinit var binding: FragmentResetPasswordBinding

    private lateinit var auth: FirebaseAuth

    private lateinit var mDatabase : DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentResetPasswordBinding.bind(view)

        auth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference.child("users")

        val resetBtn: View = binding.submitBtn
        val emailInput = binding.usernameInput

        resetBtn.setOnClickListener {
            sendResetEmail(emailInput)

        }

    }

    private fun sendResetEmail(email: EditText){

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


        auth.sendPasswordResetEmail(email.text.toString())
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Email Sent. Check Your Inbox.",
                        Toast.LENGTH_SHORT).show()
                    val action = FragmentResetPasswordDirections.actionFragmentResetPasswordToFragmentSignIn()
                    view?.findNavController()?.navigate(action)
                } else {
                    Toast.makeText(requireContext(), "Failed. Confirm Your Email.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

}