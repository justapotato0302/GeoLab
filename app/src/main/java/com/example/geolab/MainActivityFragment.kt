package com.example.geolab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.geolab.databinding.FragmentMainActivityBinding
import com.example.geolab.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso


class MainActivityFragment : Fragment() {

    private var _binding : FragmentMainActivityBinding? = null
    private val binding get() = _binding!!

    lateinit var mRecyclerView  : RecyclerView
    lateinit var mDatbase : DatabaseReference
    internal lateinit var FirebaseRecyclerAdapter : FirebaseRecyclerAdapter<Map, MainActivityFragment.ItemViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Retrieve and inflate the layout for this fragment
        _binding = FragmentMainActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mRecyclerView = binding.recycler1
        val query = FirebaseDatabase.getInstance().reference.child("Map")
        val options = FirebaseRecyclerOptions.Builder<Map>()
            .setQuery(query, Map::class.java)
            .setLifecycleOwner(this)
            .build()
        FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Map, MainActivityFragment.ItemViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainActivityFragment.ItemViewHolder {
                val adapterLayout = LayoutInflater.from(parent.context)
                    .inflate(R.layout.map, parent, false)
                return MainActivityFragment.ItemViewHolder(adapterLayout)
            }

            override fun onBindViewHolder(holder: MainActivityFragment.ItemViewHolder, position: Int, model: Map) {
                holder.bindMap(model)
            }
        }
        mRecyclerView.adapter = FirebaseRecyclerAdapter
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
    class ItemViewHolder(itemView: View, model: Map? = null) : RecyclerView.ViewHolder(itemView) {
        val item_text : TextView = itemView.findViewById(R.id.item_title)
        val item_image : ImageView = itemView.findViewById(R.id.item_image)
        fun bindMap(model : Map?){
            with(model!!){
                item_text.text = model.Name
                Picasso.get().load(model.Picture).into(item_image)
            }
            itemView.setOnClickListener{

                val action = MainActivityFragmentDirections.actionMainActivityFragmentToDetailFragment(mapName = item_text.text.toString())
                //val position: Int = adapterPosition
                itemView.findNavController().navigate(action)
                //Toast.makeText(itemView.context,"You haved just click #${position + 1}", Toast.LENGTH_SHORT).show()
            }

        }
    }
    override fun onStart() {
        if (FirebaseRecyclerAdapter != null)
            FirebaseRecyclerAdapter.startListening()
        super.onStart()

    }
    override fun onStop() {
        if (FirebaseRecyclerAdapter != null)
            FirebaseRecyclerAdapter.stopListening()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}