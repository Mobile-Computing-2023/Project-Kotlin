package com.example.mobile_computing_project.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_computing_project.R
import com.example.mobile_computing_project.models.MenuItem

class MenuItemAdapter (private val context: Context, private val menuItems: List<MenuItem>):
    RecyclerView.Adapter<MenuItemAdapter.ViewHolder>() {
        private lateinit var tvName: TextView
        private lateinit var tvPrice: TextView
        private lateinit var ivVeg: ImageView
        private lateinit var tvQty: TextView

        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            fun bind(menuItem: MenuItem){
                tvName.text = menuItem.name
                tvPrice.text = "Rs "+ menuItem.price.toString()
                tvQty.text = "5 pcs"
                if (menuItem.isVeg){
                    val color = Color.parseColor("#049D4E")
                    ivVeg.setColorFilter(color)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false)
            tvName = view.findViewById(R.id.name)
            tvPrice = view.findViewById(R.id.menu_item_price)
            ivVeg = view.findViewById(R.id.veg_nonveg_symbol)
            tvQty = view.findViewById(R.id.menu_item_qty)
            return ViewHolder(view)
        }

        override fun getItemCount() = menuItems.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(menuItems[position])
        }
    }