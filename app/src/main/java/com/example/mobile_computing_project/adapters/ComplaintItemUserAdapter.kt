package com.example.mobile_computing_project.adapters

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_computing_project.R
import com.example.mobile_computing_project.models.ComplaintItem

class ComplaintItemUserAdapter(private val complaints: List<ComplaintItem>):
    RecyclerView.Adapter<ComplaintItemUserAdapter.ViewHolder>() {

    interface OnBtnClickListener {
        fun onBtnClick(item: ComplaintItem)
    }

    private lateinit var tvDescription: TextView
    private lateinit var tvCreatedAt: TextView
    private lateinit var tvStatus: TextView

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(complaintItem: ComplaintItem){
            tvDescription.text = complaintItem.description
            tvCreatedAt.text = DateUtils.getRelativeTimeSpanString(complaintItem.createdAt)
            if(complaintItem.resolved){
                tvStatus.text = "Status: Resolved"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_complaint_user, parent, false)
        tvDescription = view.findViewById(R.id.tv_description)
        tvCreatedAt = view.findViewById(R.id.tv_createdAt)
        tvStatus = view.findViewById(R.id.tv_status)
        return ViewHolder(view)
    }

    override fun getItemCount() = complaints.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(complaints[position])
    }
}