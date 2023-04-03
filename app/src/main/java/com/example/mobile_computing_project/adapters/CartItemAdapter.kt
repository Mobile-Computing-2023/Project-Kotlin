package com.example.mobile_computing_project.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_computing_project.R
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

    private lateinit var tvName: TextView
    private lateinit var tvPrice: TextView
    private lateinit var ivVeg: ImageView
    private lateinit var tvQty: TextView
    private lateinit var btnRemoveFromCart: Button

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val tvIncrement = itemView.findViewById<TextView>(R.id.tv_increment)
        private val tvDecrement: TextView = itemView.findViewById(R.id.tv_decrement)
        private val tvShowQty: TextView = itemView.findViewById(R.id.tv_show_qty)
        fun bind(cartItem: CartItem, listener: OnBtnClickListener?, listenerInc: OnIncBtnClickListener?, listenerDec: OnDecBtnClickListener?){
            tvName.text = cartItem.name.capitalize()
            tvPrice.text = "Rs "+ cartItem.price.toString()
            tvQty.text = "Qty: "+ cartItem.qty.toString()
            tvShowQty.text = cartItem.qty.toString()
            if (cartItem.isVeg){
                val color = Color.parseColor("#049D4E")
                ivVeg.setColorFilter(color)
            }

            btnRemoveFromCart.setOnClickListener {
                listener?.onBtnClick(cartItem)
            }

            tvIncrement.setOnClickListener {
                listenerInc?.onBtnClick(cartItem)
            }

            tvDecrement.setOnClickListener {
                listenerDec?.onBtnClick(cartItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        tvName = view.findViewById(R.id.tv_name)
        tvPrice = view.findViewById(R.id.tv_price)
        ivVeg = view.findViewById(R.id.iv_nonveg_symbol)
        tvQty = view.findViewById(R.id.tv_qty)
        btnRemoveFromCart = view.findViewById(R.id.btn_removeButton)

        return ViewHolder(view)
    }

    override fun getItemCount() = cartItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cartItems[position], listener, listenerInc, listenerDec)
    }
}