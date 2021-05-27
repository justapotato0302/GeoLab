package com.example.geolab

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.geolab.databinding.FragmentLeaderboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class FragmentLeaderboard: Fragment(R.layout.fragment_leaderboard) {

    lateinit var binding: FragmentLeaderboardBinding

    lateinit var auth: FirebaseAuth

    private lateinit var dbref: DatabaseReference

    private lateinit var userRecyclerView: RecyclerView

    private lateinit var userArrayList : ArrayList<User>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLeaderboardBinding.bind(view)

        auth = FirebaseAuth.getInstance()

        val userID: String = auth.currentUser.uid

        userRecyclerView = binding.rvLeaderboard
        userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        userRecyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf<User>()
        getUserData()


    }

    private fun getUserData() {

        dbref = FirebaseDatabase.getInstance().reference.child("users")

        dbref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(User::class.java)
                        userArrayList.add(user!!)

                    }
                    userRecyclerView.adapter = LeaderboardAdapter(userArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}