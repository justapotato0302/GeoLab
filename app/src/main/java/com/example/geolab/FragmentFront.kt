package com.example.geolab

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.geolab.databinding.FragmentFrontBinding

class FragmentFront: Fragment(R.layout.fragment_front) {

    private lateinit var binding: FragmentFrontBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFrontBinding.bind(view)

        val signInBtn : View = binding.signInBtn
        signInBtn.setOnClickListener {
            (activity as TestFragmentActivity).showSignInFragment()
        }

        val registerBtn : View = binding.signUpBtn
        registerBtn.setOnClickListener {
            (activity as TestFragmentActivity).showRegisterFragment()
        }
    }

}