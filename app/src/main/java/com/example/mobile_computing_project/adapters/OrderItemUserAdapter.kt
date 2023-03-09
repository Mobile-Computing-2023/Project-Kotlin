package com.example.mobile_computing_project.adapters

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_computing_project.R
import com.example.mobile_computing_project.models.OrderItem

class OrderItemUserAdapter(private val orderItems: List<OrderItem>):
    RecyclerView.Adapter<OrderItemUserAdapter.ViewHolder>() {
    private lateinit var tvAmount: TextView
    private lateinit var tvItems: TextView
    private lateinit var tvStatus: TextView
    private lateinit var tvCreatedAt: TextView

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(orderItem: OrderItem){
            tvAmount.text = "Order Total: Rs " + orderItem.amount.toString()
            tvStatus.text = "Status: " + orderItem.status
            tvCreatedAt.text = DateUtils.getRelativeTimeSpanString(orderItem.createdAt)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item_user, parent, false)
        tvAmount = view.findViewById(R.id.tv_amount)
        tvStatus = view.findViewById(R.id.tv_status)
        tvItems = view.findViewById(R.id.tv_items)
        tvCreatedAt = view.findViewById(R.id.tv_createdAt)
        return ViewHolder(view)
    }

    override fun getItemCount() = orderItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(orderItems[position])
    }
}