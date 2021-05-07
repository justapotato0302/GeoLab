package com.example.geolab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.geolab.databinding.FragmentMainActivity2Binding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso

class MainActivity2Fragment : Fragment() {
    private var _binding : FragmentMainActivity2Binding? = null
    private val binding get() = _binding!!

    lateinit var mDatbase : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Retrieve and inflate the layout for this fragment
        _binding = FragmentMainActivity2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mDatbase = FirebaseDatabase.getInstance().reference.child("Map2")
        val chapterTestView : TextView = binding.chapter
        val chapterBane : TextView = binding.chapterName
        val imgView : ImageView = binding.image
        val mRadioGroup : RadioGroup = binding.radioGroup
        mRadioGroup.check(R.id.radioButton1)

        mDatbase.get().addOnSuccessListener {
            chapterTestView.text = when (mRadioGroup.checkedRadioButtonId) {
                R.id.radioButton1 -> "CHAPTER1"
                R.id.radioButton2 -> "CHAPTER2"
                R.id.radioButton3 -> "CHAPTER3"
                R.id.radioButton4 -> "CHAPTER4"
                R.id.radioButton5 -> "CHAPTER5"
                R.id.radioButton6 -> "CHAPTER6"
                R.id.radioButton7 -> "CHAPTER7"
                else -> "ERROR"
            }
            Picasso.get().load(it.child(chapterTestView.text as String).child("Cover").getValue<String>()).resize(800,1600).into(imgView)
            chapterBane.text = it.child(chapterTestView.text as String).child("Name").getValue<String>()
        }

        val button : Button = binding.startButton
        button.setOnClickListener {
            val action = MainActivity2FragmentDirections.actionMainActivity2FragmentToSectionFragment(chapterName = chapterTestView.text as String)
            view.findNavController().navigate(action)
        }

        /*mRadioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener{ group, checkedId ->
            when (checkedId) {
                R.id.radioButton1 -> (
                        )
            }
        })*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}