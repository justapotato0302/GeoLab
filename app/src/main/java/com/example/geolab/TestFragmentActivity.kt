package com.example.geolab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.geolab.databinding.ActivityFragmentTestBinding

class TestFragmentActivity: AppCompatActivity() {

    private lateinit var binding: ActivityFragmentTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentSignIn = FragmentSignIn()
        val fragmentRegister = FragmentRegister()
        val fragmentFront = FragmentFront()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragmentFront)
            commit()
        }

    }

    fun showRegisterFragment() {
        val fragmentRegister = FragmentRegister()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragmentRegister)
            commit()
        }
    }

    fun showSignInFragment() {
        val fragmentSignIn = FragmentSignIn()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragmentSignIn)
            commit()
        }
    }

    fun showDashboardFragment() {
        val fragmentDashboard = FragmentDashboard()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragmentDashboard)
            commit()
        }
    }

    fun showFrontFragment() {
        val fragmentFront = FragmentFront()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragmentFront)
            commit()
        }
    }



}