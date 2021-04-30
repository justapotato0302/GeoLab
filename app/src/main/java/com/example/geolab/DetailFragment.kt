package com.example.geolab

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.geolab.databinding.FragmentDetailBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso

class DetailFragment : Fragment() {
    private var _binding : FragmentDetailBinding? = null
    private val binding get() = _binding!!


    private lateinit var map_name : String
    private lateinit var mDatabase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            map_name = it.getString("map_name").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mDatabase = FirebaseDatabase.getInstance().reference.child("Map").child(map_name)
        val mapListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.w("DetailFragment", "loadPost:onCancelled", error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val imageURL : String? = snapshot.child("Picture").getValue<String>()
                Picasso.get().load(imageURL).into(binding.mapImage)
                binding.mapTitle.text = snapshot.child("Name").getValue<String>()
                binding.mapDescription.text = snapshot.child("Description").getValue<String>()
            }

        }
        mDatabase.addValueEventListener(mapListener)
        binding.start.setOnClickListener{
            val action = DetailFragmentDirections.actionDetailFragmentToGameFragment(mapName2 = map_name)
            view.findNavController().navigate(action)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}