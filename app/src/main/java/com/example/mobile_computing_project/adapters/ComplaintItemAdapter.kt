package com.example.mobile_computing_project.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_computing_project.R
import com.example.mobile_computing_project.models.ComplaintItem


class ComplaintItemAdapter (private val context: Context, private val complaints: List<ComplaintItem>):
    RecyclerView.Adapter<ComplaintItemAdapter.ViewHolder>() {
    private lateinit var tvDescription: TextView
    private lateinit var tvUser: TextView
    private lateinit var tvCreatedAt: TextView

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(complaintItem: ComplaintItem){
            tvDescription.text = complaintItem.description
            tvUser.text = complaintItem.user?.name
            tvCreatedAt.text = complaintItem.createdAt.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.complaint_item, parent, false)
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