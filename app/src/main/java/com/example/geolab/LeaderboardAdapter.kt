package com.example.geolab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class LeaderboardAdapter(private val userList : ArrayList<User>) : RecyclerView.Adapter<LeaderboardAdapter.myViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user, parent, false)
        return myViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {

        val currentItem = userList[position]

        holder.displayName.text = currentItem.displayName
        holder.highScore.text = currentItem.highScore

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class myViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val displayName : TextView = itemView.findViewById(R.id.tvName)
        val highScore : TextView = itemView.findViewById(R.id.tvScore)

    }
}