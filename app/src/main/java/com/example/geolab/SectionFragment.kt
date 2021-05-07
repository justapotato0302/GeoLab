package com.example.geolab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.geolab.databinding.FragmentSectionBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso


class SectionFragment : Fragment() {
    private var _binding : FragmentSectionBinding? = null
    private val binding get() = _binding!!

    private lateinit var mDatbase : DatabaseReference
    private lateinit var chapterName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            chapterName = it.getString("chapterName").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Retrieve and inflate the layout for this fragment
        _binding = FragmentSectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mDatbase = FirebaseDatabase.getInstance().reference.child("Map2").child(chapterName).child("Section")
        mDatbase.get().addOnSuccessListener {
            val img : ImageView = binding.insideImageview1
            Picasso.get().load(it.child("sec1").child("CoverPicture").getValue<String>()).resize(1300,450).into(img)
            val img2 : ImageView = binding.insideImageview2
            Picasso.get().load(it.child("sec2").child("CoverPicture").getValue<String>()).resize(1300,450).into(img2)
            val img3 : ImageView = binding.insideImageview3
            Picasso.get().load(it.child("sec3").child("CoverPicture").getValue<String>()).resize(1300,450).into(img3)
            val img4 : ImageView = binding.insideImageview4
            Picasso.get().load(it.child("sec4").child("CoverPicture").getValue<String>()).resize(1300,450).into(img4)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}