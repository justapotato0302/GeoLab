package com.example.geolab

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.geolab.databinding.FragmentFrontBinding

class FragmentFront: Fragment(R.layout.fragment_front) {

    private lateinit var binding: FragmentFrontBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFrontBinding.bind(view)

        val signInBtn : View = binding.signInBtn
        signInBtn.setOnClickListener {
            val action = FragmentFrontDirections.actionFragmentFrontToFragmentSignIn()
            view.findNavController().navigate(action)
        }

        val registerBtn : View = binding.signUpBtn
        registerBtn.setOnClickListener {

            val action = FragmentFrontDirections.actionFragmentFrontToFragmentRegister()
            view.findNavController().navigate(action)
        }
    }

}