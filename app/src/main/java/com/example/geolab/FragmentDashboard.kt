package com.example.geolab

import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.geolab.databinding.FragmentDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_dashboard.*


class FragmentDashboard: Fragment(R.layout.fragment_dashboard) {

    private lateinit var binding: FragmentDashboardBinding

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDashboardBinding.bind(view)

        auth = FirebaseAuth.getInstance()

        val userID : String = auth.currentUser.uid

        val logout_btn : CardView? = binding.logoutBtn

        if (logout_btn != null) {
            logout_btn.setOnClickListener {
                auth.signOut()

                val action = FragmentDashboardDirections.actionFragmentDashboardToFragmentSignIn()
                view.findNavController().navigate(action)
            }
        }
        val adventureModeNavigation : CardView = binding.adventure
        adventureModeNavigation.setOnClickListener {
            val action = FragmentDashboardDirections.actionFragmentDashboardToMainActivityFragment()
            view.findNavController().navigate(action)
        }

        val discoveryModeNavigation : CardView = binding.discovery
        discoveryModeNavigation.setOnClickListener {
            val action = FragmentDashboardDirections.actionFragmentDashboardToMainActivity2Fragment()
            view.findNavController().navigate(action)
        }

        val userProfileNavigation : CardView = binding.profile
        userProfileNavigation.setOnClickListener {
            val action = FragmentDashboardDirections.actionFragmentDashboardToFragmentUserProfile()
            view.findNavController().navigate(action)
        }
        val leadboardNavigation : CardView = binding.leaderboardBtn
        leaderboardBtn.setOnClickListener {
            val action = FragmentDashboardDirections.actionFragmentDashboardToFragmentLeaderboard()
            view.findNavController().navigate(action)
        }

    }

}