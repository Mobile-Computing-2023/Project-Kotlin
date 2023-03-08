package com.example.mobile_computing_project.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_computing_project.R
import com.example.mobile_computing_project.models.OrderItemCanteen

class OrderItemCanteenAdapter (private val context: Context, private val orderItems: List<OrderItemCanteen>):
    RecyclerView.Adapter<OrderItemCanteenAdapter.ViewHolder>() {
    private lateinit var tvName: TextView
    private lateinit var tvAmount: TextView
    private lateinit var tvItems: TextView
    private lateinit var tvStatus: TextView

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(orderItem: OrderItemCanteen){
            tvName.text = orderItem.user?.name
            tvAmount.text = orderItem.amount.toString()
            tvStatus.text = orderItem.status
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.order_item_canteen, parent, false)
        tvName = view.findViewById(R.id.tv_name)
        tvAmount = view.findViewById(R.id.tv_amount)
        tvStatus = view.findViewById(R.id.tv_status)
        tvItems = view.findViewById(R.id.tv_items)
        return ViewHolder(view)
    }

    override fun getItemCount() = orderItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(orderItems[position])
    }
}