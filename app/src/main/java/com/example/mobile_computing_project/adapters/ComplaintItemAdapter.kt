package com.example.mobile_computing_project.adapters

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_computing_project.R
import com.example.mobile_computing_project.models.ComplaintItem
import com.example.mobile_computing_project.models.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class ComplaintItemAdapter (private val complaints: List<ComplaintItem>):
    RecyclerView.Adapter<ComplaintItemAdapter.ViewHolder>() {
    private lateinit var tvDescription: TextView
    private lateinit var tvUser: TextView
    private lateinit var tvCreatedAt: TextView

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(complaintItem: ComplaintItem){
            tvDescription.text = complaintItem.description
            // Get username from here
            tvUser.text = "Complainant: " + complaintItem.userid
            val db = Firebase.firestore
            db.collection("Users")
                .whereEqualTo("uid", complaintItem.userid)
                .get()
                .addOnSuccessListener {docs ->
                    for (d in docs){
                        val complainant = d.toObject<User>()
                        tvUser.text = "Complainant: " + complainant.name
                    }
                }
            tvCreatedAt.text = DateUtils.getRelativeTimeSpanString(complaintItem.createdAt)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.complaint_item, parent, false)
        tvDescription = view.findViewById(R.id.tv_description)
        tvUser = view.findViewById(R.id.tv_user)
        tvCreatedAt = view.findViewById(R.id.tv_createdAt)
        return ViewHolder(view)
    }

    override fun getItemCount() = complaints.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(complaints[position])
    }

}