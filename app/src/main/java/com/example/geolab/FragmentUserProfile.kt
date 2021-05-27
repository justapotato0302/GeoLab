package com.example.geolab

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.geolab.databinding.FragmentUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue

class FragmentUserProfile: Fragment(R.layout.fragment_user_profile) {

    private lateinit var binding: FragmentUserProfileBinding

    private lateinit var auth: FirebaseAuth

    private lateinit var userDatabase: DatabaseReference


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserProfileBinding.bind(view)

        auth = FirebaseAuth.getInstance()
        var user = auth.currentUser
        var userId = user.uid
        var name = binding.fullname
        var level = binding.Level
        var highscore = binding.highScore
        var pass = binding.passwordTxt
        var email = binding.emailTxt
        userDatabase = FirebaseDatabase.getInstance().reference.child("users").child(userId)

        pass.isEnabled = false
        name.isEnabled = false
        email.isEnabled = false


        userDatabase.get().addOnSuccessListener {
            var displayName: String? = it.child("displayName").getValue<String>()
            name.setText(displayName)
            name.isEnabled = true

            var score: String? = it.child("highScore").getValue<String>()
            highscore.setText("High Score: " + score)

            var currentStage: String? = it.child("currentStage").getValue<String>()
            level.setText("Level: " + currentStage)
        }

        binding.emailTxt.setText(user.email)

        var editName: Button = binding.editBtn
        editName.setOnClickListener {
            editDisplayName(name)
        }

        var resetBtn: Button = binding.resetBtn
        resetBtn.setOnClickListener {
            sendPasswordReset(user.email)
        }
    }

    private fun editDisplayName(inputName: EditText){
        if (inputName.text.toString().isEmpty()) {
            inputName.error = "Please enter name!"
            inputName.requestFocus()
            return
        }
        userDatabase.get().addOnSuccessListener {
            userDatabase.child("displayName").setValue(inputName.text.toString())
            Toast.makeText(requireContext(), "Display Name Changed.",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendPasswordReset(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Email Sent. Check Your Inbox.",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed. Try Again Later.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}