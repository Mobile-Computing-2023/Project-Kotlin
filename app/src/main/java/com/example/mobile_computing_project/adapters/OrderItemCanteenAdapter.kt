package com.example.mobile_computing_project.adapters

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_computing_project.R
import com.example.mobile_computing_project.models.CartItem
import com.example.mobile_computing_project.models.OrderItem

class OrderItemCanteenAdapter (private val orderItems: List<OrderItem>):
    RecyclerView.Adapter<OrderItemCanteenAdapter.ViewHolder>() {
    private lateinit var tvName: TextView
    private lateinit var tvAmount: TextView
//    private lateinit var tvItems: TextView
    private lateinit var tvStatus: TextView
    private lateinit var tvCreatedAt: TextView
    private lateinit var rvItems: RecyclerView
    private lateinit var bekaarContext: Context

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(orderItem: OrderItem){
            tvName.text = orderItem.user?.name
            tvAmount.text = "Order Total:\nRs " + orderItem.amount.toString()
            tvStatus.text = "status: " + orderItem.status
            tvCreatedAt.text = DateUtils.getRelativeTimeSpanString(orderItem.createdAt)

            val canteenOrderItems: MutableList<CartItem> = mutableListOf()
            val itemsList = orderItem.items?.toMutableList<CartItem>()
            println(itemsList)
//            canteenOrderItems.addAll(itemsList)
            if (itemsList != null) {
                for (i in itemsList){
                    canteenOrderItems.add(i)
                }
            }
            rvItems.adapter = CanteenOrderItemsAdapter(canteenOrderItems)
            rvItems.layoutManager = LinearLayoutManager(bekaarContext)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item_canteen, parent, false)
        bekaarContext = parent.context
        tvName = view.findViewById(R.id.tv_name)
        tvAmount = view.findViewById(R.id.tv_amount)
        tvStatus = view.findViewById(R.id.tv_status)
//        tvItems = view.findViewById(R.id.tv_items)
        tvCreatedAt = view.findViewById(R.id.tv_createdAt)
        rvItems = view.findViewById(R.id.rv_canteen_orders_items)
        return ViewHolder(view)
    }

    override fun getItemCount() = orderItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(orderItems[position])
    }
}