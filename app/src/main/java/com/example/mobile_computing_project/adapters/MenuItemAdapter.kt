package com.example.mobile_computing_project.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobile_computing_project.MainActivity
import com.example.mobile_computing_project.R
import com.example.mobile_computing_project.models.CartItem
import com.example.mobile_computing_project.models.MenuItem
import com.google.android.material.card.MaterialCardView

//private var cartItemsList: MutableList<CartItem> = mutableListOf()

class MenuItemAdapter(private val context: Context, private val menuItems: List<MenuItem>, private val activity: MainActivity):
    RecyclerView.Adapter<MenuItemAdapter.ViewHolder>() {

        interface OnBtnClickListener {
            fun onBtnClick(item: MenuItem)
        }
        private var listener: OnBtnClickListener? = null

        interface OnIncBtnClickListener {
            fun onBtnClick(item: MenuItem)
        }
        private var listenerInc: OnIncBtnClickListener? = null

        interface OnDecBtnClickListener {
            fun onBtnClick(item: MenuItem)
        }
        private var listenerDec: OnDecBtnClickListener? = null

        fun setOnBtnClickListener(listener: OnBtnClickListener){
            this.listener = listener
        }

        fun setOnIncBtnClickListener(l: OnIncBtnClickListener) {
            this.listenerInc = l
        }

        fun setOnDecBtnClickListener(l: OnDecBtnClickListener) {
            this.listenerDec = l
        }

        private lateinit var tvName: TextView
        private lateinit var tvPrice: TextView
        private lateinit var ivVeg: ImageView
        private lateinit var tvQty: TextView
        private lateinit var ivImgSrc: ImageView
        private val cartList = activity.listInMainActivity
//        private val cartItemsAdapter = CartItemAdapter(cartItemsList)

        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            private val btnAddToCart: Button = itemView.findViewById(R.id.btn_add_to_cart)
            private val btnMenuIncDec: MaterialCardView = itemView.findViewById(R.id.btn_menu_inc_dec)
            private val tvMenuQty: TextView = itemView.findViewById(R.id.tv_menu_show_qty)
            private val tvMenuInc: TextView = itemView.findViewById(R.id.tv_menu_inc)
            private val tvMenuDec: TextView = itemView.findViewById(R.id.tv_menu_dec)
            fun bind(menuItem: MenuItem, listener: OnBtnClickListener?, listenerInc: OnIncBtnClickListener?, listenerDec: OnDecBtnClickListener?){
                tvName.text = menuItem.name.capitalize()
                tvPrice.text = "Rs "+ menuItem.price.toString()
                tvQty.text = "5 pcs"
                tvMenuQty.text = "1".toString()
                if (menuItem.isVeg){
                    val color = Color.parseColor("#049D4E")
                    ivVeg.setColorFilter(color)
                }
                val i = cartList.indexOfFirst { it.name == menuItem.name }
                if (i != -1) {
                    btnAddToCart.visibility = View.GONE
                    btnMenuIncDec.visibility = View.VISIBLE
                    tvMenuQty.text = cartList[i].qty.toString()
                }
                btnAddToCart.setOnClickListener {
                    listener?.onBtnClick(menuItem)
                    btnAddToCart.visibility = View.GONE
                    btnMenuIncDec.visibility = View.VISIBLE
//                    val x = CartItem(
//                        name = menuItem.name,
//                        qty = 1,
//                        isVeg = menuItem.isVeg,
//                        price = menuItem.price
//                    )
//                    cartItemsList.add(x)
//                    println(cartItemsList)
//                    cartItemsAdapter.notifyDataSetChanged()
                }

                tvMenuInc.setOnClickListener {
                    listenerInc?.onBtnClick(menuItem)
                }

                tvMenuDec.setOnClickListener {
                    listenerDec?.onBtnClick(menuItem)
                    val i = cartList.indexOfFirst { it.name == menuItem.name }
                    if(i == -1) {
                        btnAddToCart.visibility = View.VISIBLE
                        btnMenuIncDec.visibility = View.GONE
                    }
                }
                Glide.with(context).load(menuItem.imgSrc).into(ivImgSrc)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_item_user, parent, false)
            tvName = view.findViewById(R.id.name)
            tvPrice = view.findViewById(R.id.menu_item_price)
            ivVeg = view.findViewById(R.id.veg_nonveg_symbol)
            tvQty = view.findViewById(R.id.menu_item_qty)
//            btnAddToCart = view.findViewById(R.id.btn_add_to_cart)
            ivImgSrc = view.findViewById(R.id.iv_img_src)
//            btnMenuIncDec = view.findViewById(R.id.btn_menu_inc_dec)
            return ViewHolder(view)
        }

        override fun getItemCount() = menuItems.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(menuItems[position], listener, listenerInc, listenerDec)
        }
    }