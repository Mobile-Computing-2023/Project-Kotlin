package com.example.mobile_computing_project.adapters

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_computing_project.databinding.ItemComplaintUserBinding
import com.example.mobile_computing_project.models.ComplaintItem

class ComplaintItemUserAdapter(private val complaints: List<ComplaintItem>):
    RecyclerView.Adapter<ComplaintItemUserAdapter.ViewHolder>() {

    interface OnBtnClickListener {
        fun onBtnClick(item: ComplaintItem)
    }

    inner class ViewHolder(private val binding: ItemComplaintUserBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(complaintItem: ComplaintItem){
            binding.tvDescription.text = complaintItem.description
            binding.tvCreatedAt.text = DateUtils.getRelativeTimeSpanString(complaintItem.createdAt)
            if(complaintItem.resolved){
                binding.tvStatus.text = "Status: Resolved"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemComplaintUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = complaints.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(complaints[position])
    }
}