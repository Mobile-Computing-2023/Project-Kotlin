package com.example.mobile_computing_project.adapters.user

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_computing_project.R
import com.example.mobile_computing_project.models.ComplaintItem


class ComplaintItemAdapter (private val complaints: List<ComplaintItem>):
    RecyclerView.Adapter<ComplaintItemAdapter.ViewHolder>() {

    interface OnBtnClickListener {
        fun onBtnClick(item: ComplaintItem)
    }
    private var listener: OnBtnClickListener? = null

    fun setOnBtnClickListener(listener: OnBtnClickListener){
        this.listener = listener
    }

    private lateinit var tvDescription: TextView
    private lateinit var tvUser: TextView
    private lateinit var tvCreatedAt: TextView
    private lateinit var btnResolve: Button

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(complaintItem: ComplaintItem, listener: OnBtnClickListener?){
            tvDescription.text = complaintItem.description
            tvUser.text = "Complainant: " + complaintItem.user?.name?.capitalize()
            tvCreatedAt.text = DateUtils.getRelativeTimeSpanString(complaintItem.createdAt)
            btnResolve.setOnClickListener {
                listener?.onBtnClick(complaintItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_complaint_canteen, parent, false)
        tvDescription = view.findViewById(R.id.tv_description)
        tvUser = view.findViewById(R.id.tv_user)
        tvCreatedAt = view.findViewById(R.id.tv_createdAt)
        btnResolve = view.findViewById(R.id.btn_resolve)
        return ViewHolder(view)
    }

    override fun getItemCount() = complaints.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(complaints[position], listener)
    }

}