package com.example.mobile_computing_project.adapters.user

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
import com.example.mobile_computing_project.databinding.ItemMenuUserBinding
import com.example.mobile_computing_project.models.MenuItem
import com.google.android.material.card.MaterialCardView


class MenuItemUserAdapter(private val context: Context, private val menuItems: List<MenuItem>, private val activity: MainActivity):
    RecyclerView.Adapter<MenuItemUserAdapter.ViewHolder>() {

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

//    private lateinit var tvName: TextView
//    private lateinit var tvPrice: TextView
//    private lateinit var ivVeg: ImageView
//    private lateinit var ivImgSrc: ImageView
    private val cartList = activity.listInMainActivity

    inner class ViewHolder(private val binding: ItemMenuUserBinding): RecyclerView.ViewHolder(binding.root){
//        private val btnAddToCart: Button = itemView.findViewById(R.id.btn_add_to_cart)
//        private val btnMenuIncDec: MaterialCardView = itemView.findViewById(R.id.btn_menu_inc_dec)
//        private val tvMenuQty: TextView = itemView.findViewById(R.id.tv_menu_show_qty)
//        private val tvMenuInc: TextView = itemView.findViewById(R.id.tv_menu_inc)
//        private val tvMenuDec: TextView = itemView.findViewById(R.id.tv_menu_dec)
        fun bind(menuItem: MenuItem, listener: OnBtnClickListener?, listenerInc: OnIncBtnClickListener?, listenerDec: OnDecBtnClickListener?){
            binding.tvName.text = menuItem.name
            binding.tvPrice.text = "Rs "+ menuItem.price.toString()
            binding.tvMenuShowQty.text = "1"
            if (menuItem.isVeg){
                val color = Color.parseColor("#049D4E")
                binding.ivVeg.setColorFilter(color)
            }
            val i = cartList.indexOfFirst { it.name == menuItem.name }
            if (i != -1) {
                binding.btnAddToCart.visibility = View.GONE
                binding.btnMenuIncDec.visibility = View.VISIBLE
                binding.tvMenuShowQty.text = cartList[i].qty.toString()
            }

            binding.btnAddToCart.setOnClickListener {
                listener?.onBtnClick(menuItem)
                binding.btnAddToCart.visibility = View.GONE
                binding.btnMenuIncDec.visibility = View.VISIBLE
            }

            binding.tvMenuInc.setOnClickListener {
                listenerInc?.onBtnClick(menuItem)
            }

            binding.tvMenuDec.setOnClickListener {
                listenerDec?.onBtnClick(menuItem)
                val x = cartList.indexOfFirst { it.name == menuItem.name }
                if(x == -1) {
                    binding.btnAddToCart.visibility = View.VISIBLE
                    binding.btnMenuIncDec.visibility = View.GONE
                }
            }

            Glide.with(context).load(menuItem.imgSrc).into(binding.ivImgSrc)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu_user, parent, false)
//        tvName = view.findViewById(R.id.tv_name)
//        tvPrice = view.findViewById(R.id.tv_price)
//        ivVeg = view.findViewById(R.id.iv_veg)
//        ivImgSrc = view.findViewById(R.id.iv_img_src)
        val binding = ItemMenuUserBinding.inflate(LayoutInflater.from((parent.context)), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = menuItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(menuItems[position], listener, listenerInc, listenerDec)
    }
}