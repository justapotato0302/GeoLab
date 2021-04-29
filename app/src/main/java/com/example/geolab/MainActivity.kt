package com.example.geolab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.geolab.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, TestFragmentActivity::class.java)
            startActivity(intent)

//        val signInBtn : View = binding.signInBtn
//        signInBtn.setOnClickListener {
//            val intent = Intent(this, TestFragmentActivity::class.java)
//            startActivity(intent)
//        }
    }
}