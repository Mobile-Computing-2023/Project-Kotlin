package com.example.mobile_computing_project.adapters.user

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_computing_project.adapters.canteen.CanteenOrderItemsAdapter
import com.example.mobile_computing_project.databinding.ItemOrderUserBinding
import com.example.mobile_computing_project.models.CartItem
import com.example.mobile_computing_project.models.OrderItem

class OrderItemUserAdapter(private val orderItems: List<OrderItem>):
    RecyclerView.Adapter<OrderItemUserAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemOrderUserBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(orderItem: OrderItem){
            binding.tvOrderId.text = "Order #${orderItem.oid.subSequence(0,5)}"
            binding.tvAmount.text = "Order Total: Rs " + orderItem.amount.toString()
            binding.tvStatus.text = "Status: " + orderItem.status
            binding.tvCreatedAt.text = DateUtils.getRelativeTimeSpanString(orderItem.createdAt)
            binding.rvUserOrdersItems.adapter = CanteenOrderItemsAdapter(orderItem.items as MutableList<CartItem>)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.rvUserOrdersItems.layoutManager = LinearLayoutManager(parent.context)
        return ViewHolder(binding)
    }

    override fun getItemCount() = orderItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(orderItems[position])
    }
}