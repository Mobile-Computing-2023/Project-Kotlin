package com.example.mobile_computing_project.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_computing_project.R
import com.example.mobile_computing_project.models.CartItem

class CanteenOrderItemsAdapter (private val cartItems: List<CartItem>):
    RecyclerView.Adapter<CanteenOrderItemsAdapter.ViewHolder>() {
    private lateinit var tvName: TextView
    private lateinit var ivVeg: ImageView
    private lateinit var tvQty: TextView

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(cartItem: CartItem){
            tvName.text = cartItem.name.capitalize()
            tvQty.text = cartItem.qty.toString()
            if (cartItem.isVeg){
                val color = Color.parseColor("#049D4E")
                ivVeg.setColorFilter(color)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_item, parent, false)
        tvName = view.findViewById(R.id.tv_name)
        ivVeg = view.findViewById(R.id.iv_nonveg_symbol)
        tvQty = view.findViewById(R.id.tv_qty)
        return ViewHolder(view)
    }

    override fun getItemCount() = cartItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }
}