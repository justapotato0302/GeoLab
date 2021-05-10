package com.example.geolab

import android.os.Bundle
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AlignmentSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
            val sec1Name : TextView = binding.text1
            sec1Name.text = it.child("sec1").child("Name").getValue<String>().toString()
            img.setOnClickListener { showSectionInformationDialog("sec1") }

            val img2 : ImageView = binding.insideImageview2
            Picasso.get().load(it.child("sec2").child("CoverPicture").getValue<String>()).resize(1300,450).into(img2)
            val sec2Name : TextView = binding.text2
            sec2Name.text = it.child("sec2").child("Name").getValue<String>().toString()
            img2.setOnClickListener {
                if (lock2.visibility == View.VISIBLE) {
                    showAlertDialog()
                } else {
                    showSectionInformationDialog("sec2")
                }
            }

            val img3 : ImageView = binding.insideImageview3
            Picasso.get().load(it.child("sec3").child("CoverPicture").getValue<String>()).resize(1300,450).into(img3)
            val sec3Name : TextView = binding.text3
            sec3Name.text = it.child("sec3").child("Name").getValue<String>().toString()
            img3.setOnClickListener {
                if (lock3.visibility == View.VISIBLE) {
                    showAlertDialog()
                } else {
                    showSectionInformationDialog("sec3")
                }
            }

            val img4 : ImageView = binding.insideImageview4
            Picasso.get().load(it.child("sec4").child("CoverPicture").getValue<String>()).resize(1300,450).into(img4)
            val sec4Name : TextView = binding.text4
            sec4Name.text = it.child("sec4").child("Name").getValue<String>().toString()
            img4.setOnClickListener {
                if (lock4.visibility == View.VISIBLE) {
                    showAlertDialog()
                } else {
                    showSectionInformationDialog("sec4")
                }
            }
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

    private fun showSectionInformationDialog(section : String) {
        mDatbase.child(section).get().addOnSuccessListener {
            val sectionName : String = it.child("Name").getValue<String>().toString()
            val sectionInfo : String = it.child("Description").getValue<String>().toString()
            val title = SpannableString(sectionName)
            title.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0,
                title.length,
                0
            )
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(title)
                .setMessage(sectionInfo)
                .setNegativeButton("Countinue") { dialog, _ ->
                    dialog.cancel()
                    dialog.dismiss()
                }
                .show()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}