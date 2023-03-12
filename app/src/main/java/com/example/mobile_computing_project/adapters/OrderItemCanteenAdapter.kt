package com.example.mobile_computing_project.adapters

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_computing_project.R
import com.example.mobile_computing_project.models.CartItem
import com.example.mobile_computing_project.models.MenuItem
import com.example.mobile_computing_project.models.OrderItem

class OrderItemCanteenAdapter (private val orderItems: List<OrderItem>):
    RecyclerView.Adapter<OrderItemCanteenAdapter.ViewHolder>() {

    interface OnBtnClickListener {
        fun onBtnClick(item: OrderItem)
    }
    private var listener: OnBtnClickListener? = null

    fun setOnBtnClickListener(listener: OnBtnClickListener) {
        this.listener = listener
    }

    private lateinit var tvName: TextView
    private lateinit var tvAmount: TextView
    private lateinit var tvStatus: TextView
    private lateinit var tvCreatedAt: TextView
    private lateinit var rvItems: RecyclerView
    private var canteenOrderItems: MutableList<CartItem> = mutableListOf()
    private lateinit var btnMarkResolved: Button

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(orderItem: OrderItem, listener: OnBtnClickListener?){
            tvName.text = orderItem.user?.name
            tvAmount.text = "Order Total: Rs " + orderItem.amount.toString()
            tvStatus.text = "Status: " + orderItem.status
            tvCreatedAt.text = DateUtils.getRelativeTimeSpanString(orderItem.createdAt)
            canteenOrderItems = orderItem.items as MutableList<CartItem>
            rvItems.adapter = CanteenOrderItemsAdapter(canteenOrderItems)

            btnMarkResolved.setOnClickListener {
                listener?.onBtnClick(orderItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item_canteen, parent, false)
        tvName = view.findViewById(R.id.tv_name)
        tvAmount = view.findViewById(R.id.tv_amount)
        tvStatus = view.findViewById(R.id.tv_status)
        tvCreatedAt = view.findViewById(R.id.tv_createdAt)
        rvItems = view.findViewById(R.id.rv_canteen_orders_items)
        rvItems.layoutManager = LinearLayoutManager(parent.context)
        btnMarkResolved = view.findViewById(R.id.btn_mark_complete)
        return ViewHolder(view)
    }

    override fun getItemCount() = orderItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(orderItems[position], listener)
    }
}