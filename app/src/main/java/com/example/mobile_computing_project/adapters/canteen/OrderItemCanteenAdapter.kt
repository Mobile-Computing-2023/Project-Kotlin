package com.example.mobile_computing_project.adapters.canteen

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_computing_project.databinding.ItemOrderCanteenBinding
import com.example.mobile_computing_project.models.CartItem
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

    private var canteenOrderItems: MutableList<CartItem> = mutableListOf()

    inner class ViewHolder(private val binding: ItemOrderCanteenBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(orderItem: OrderItem, listener: OnBtnClickListener?){
            binding.tvName.text = "${orderItem.user?.name}'s Order #${orderItem.oid.subSequence(0,8)}"
            binding.tvAmount.text = "Order Total: Rs " + orderItem.amount.toString()
            binding.tvStatus.text = "Status: " + orderItem.status
            binding.tvCreatedAt.text = DateUtils.getRelativeTimeSpanString(orderItem.createdAt)
            canteenOrderItems = orderItem.items as MutableList<CartItem>
            binding.rvCanteenOrdersItems.adapter = CanteenOrderItemsAdapter(canteenOrderItems)
            binding.btnMarkComplete.setOnClickListener {
                listener?.onBtnClick(orderItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderCanteenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.rvCanteenOrdersItems.layoutManager = LinearLayoutManager(parent.context)
        return ViewHolder(binding)
    }

    override fun getItemCount() = orderItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(orderItems[position], listener)
    }
}