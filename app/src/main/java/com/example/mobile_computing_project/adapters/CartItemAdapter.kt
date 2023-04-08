package com.example.mobile_computing_project.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_computing_project.databinding.ItemCartBinding
import com.example.mobile_computing_project.models.CartItem

class CartItemAdapter (private val cartItems: List<CartItem>):
    RecyclerView.Adapter<CartItemAdapter.ViewHolder>() {

    interface OnBtnClickListener {
        fun onBtnClick(item: CartItem)
    }

    interface OnIncBtnClickListener {
        fun onBtnClick(item: CartItem)
    }

    interface OnDecBtnClickListener {
        fun onBtnClick(item: CartItem)
    }

    private var listener: OnBtnClickListener? = null
    private var listenerInc: OnIncBtnClickListener? = null
    private var listenerDec: OnDecBtnClickListener? = null

    fun setOnBtnClickListener(listener: OnBtnClickListener) {
        this.listener = listener
    }

    fun setOnIncBtnClickListener(listener: OnIncBtnClickListener) {
        this.listenerInc = listener
    }

    fun setOnDecBtnClickListener(listener: OnDecBtnClickListener) {
        this.listenerDec = listener
    }

    inner class ViewHolder(private val binding: ItemCartBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(cartItem: CartItem, listener: OnBtnClickListener?, listenerInc: OnIncBtnClickListener?, listenerDec: OnDecBtnClickListener?){
            binding.tvName.text = cartItem.name.capitalize()
            binding.tvPrice.text = "Rs "+ cartItem.price.toString()
            binding.tvQty.text = "Qty: "+ cartItem.qty.toString()
            binding.tvShowQty.text = cartItem.qty.toString()
            if (cartItem.isVeg){
                val color = Color.parseColor("#049D4E")
//                binding.ivVeg.setColorFilter(color)
            }

            binding.btnRemoveButton.setOnClickListener {
                listener?.onBtnClick(cartItem)
            }

            binding.tvIncrement.setOnClickListener {
                listenerInc?.onBtnClick(cartItem)
            }

            binding.tvDecrement.setOnClickListener {
                listenerDec?.onBtnClick(cartItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = cartItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cartItems[position], listener, listenerInc, listenerDec)
    }
}