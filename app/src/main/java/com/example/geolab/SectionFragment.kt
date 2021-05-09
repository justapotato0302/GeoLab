package com.example.geolab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isGone
import com.example.geolab.databinding.FragmentSectionBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso


class SectionFragment : Fragment() {
    private var _binding : FragmentSectionBinding? = null
    private val binding get() = _binding!!

    private lateinit var mDatbase : DatabaseReference
    private lateinit var mDatabase2 : DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var chapterName : String
    private lateinit var userId : String

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
        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser.uid

        val lock2 : ImageView = binding.outsideImageview2
        val lock3 : ImageView = binding.outsideImageview3
        val lock4 : ImageView = binding.outsideImageview4

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
        mDatabase2 = FirebaseDatabase.getInstance().reference.child("users").child(userId)
        mDatabase2.child("currentStage").get().addOnSuccessListener {
            val currentSatge = stringSeperated(it.getValue<String>())
            when (currentSatge) {
                "sec2" -> {
                    lock2.visibility = View.GONE
                }
                "sec3" -> {
                    lock3.visibility = View.GONE
                    disableLock2()
                }
                "sec4" -> {
                    lock4.visibility = View.GONE
                    disableLock2()
                    disableLock3()
                }
            }
        }
        lock2.setOnClickListener{showAlertDialog()}
        lock3.setOnClickListener{showAlertDialog()}
        lock4.setOnClickListener{showAlertDialog()}

    }

    private fun stringSeperated(currentStage : String? = null) : String? {
        val delimeter = "_"
        val list : List<String>? = currentStage?.split(delimeter)
        return list?.get(1)
    }

    private fun disableLock2(){
        val lock : ImageView = binding.outsideImageview2
        lock.visibility = View.GONE
    }
    private fun disableLock3(){
        val lock : ImageView = binding.outsideImageview3
        lock.visibility = View.GONE
    }

    private fun showAlertDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.unclock))
            .setNegativeButton(getString(R.string.exit)) { dialog, _ ->
                dialog.cancel()
                dialog.dismiss()
            }
            .show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}