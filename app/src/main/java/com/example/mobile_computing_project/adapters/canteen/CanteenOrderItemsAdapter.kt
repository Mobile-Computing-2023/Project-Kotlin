package com.example.mobile_computing_project.adapters.canteen

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_computing_project.R
import com.example.mobile_computing_project.databinding.ItemOrderCanteenBinding
import com.example.mobile_computing_project.databinding.ItemOrderItemBinding
import com.example.mobile_computing_project.models.CartItem

class CanteenOrderItemsAdapter (private val cartItems: List<CartItem>):
    RecyclerView.Adapter<CanteenOrderItemsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemOrderItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(cartItem: CartItem){
            binding.tvName.text = cartItem.name.capitalize()
            binding.tvQty.text = cartItem.qty.toString()
            if (cartItem.isVeg){
                val color = Color.parseColor("#049D4E")
                binding.ivVeg.setColorFilter(color)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = cartItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }
}