package com.example.mobile_computing_project.adapters.canteen

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_computing_project.databinding.ItemComplaintCanteenBinding
import com.example.mobile_computing_project.models.ComplaintItem


class ComplaintItemCanteenAdapter (private val complaints: List<ComplaintItem>):
    RecyclerView.Adapter<ComplaintItemCanteenAdapter.ViewHolder>() {

    interface OnBtnClickListener {
        fun onBtnClick(item: ComplaintItem)
    }
    private var listener: OnBtnClickListener? = null

    fun setOnBtnClickListener(listener: OnBtnClickListener){
        this.listener = listener
    }

    inner class ViewHolder(private val binding: ItemComplaintCanteenBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(complaintItem: ComplaintItem, listener: OnBtnClickListener?){
            binding.tvDescription.text = complaintItem.description
            binding.tvUser.text = "Complainant: " + complaintItem.user?.name
            binding.tvCreatedAt.text = DateUtils.getRelativeTimeSpanString(complaintItem.createdAt)
            binding.btnResolve.setOnClickListener {
                listener?.onBtnClick(complaintItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemComplaintCanteenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = complaints.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(complaints[position], listener)
    }

}