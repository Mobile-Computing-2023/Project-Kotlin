package com.example.mobile_computing_project.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile_computing_project.MainActivity
import com.example.mobile_computing_project.R
import com.example.mobile_computing_project.adapters.user.MenuItemUserAdapter
import com.example.mobile_computing_project.databinding.FragmentMenuBinding
import com.example.mobile_computing_project.models.CartItem
import com.example.mobile_computing_project.models.MenuItem
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuFragment : Fragment() {
    private lateinit var binding: FragmentMenuBinding
    private var menuItems: MutableList<com.example.mobile_computing_project.models.MenuItem> = mutableListOf()
    private var specialItems: MutableList<com.example.mobile_computing_project.models.MenuItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val splMenuItemAdapter = context?.let { MenuItemUserAdapter(context = it, menuItems = specialItems, activity = requireActivity() as MainActivity) }
        binding.rvSplMenuItems.adapter = splMenuItemAdapter
        binding.rvSplMenuItems.layoutManager = LinearLayoutManager(requireContext())

        val menuItemAdapter = context?.let { MenuItemUserAdapter(context = it, menuItems = menuItems, activity = requireActivity() as MainActivity) }
        binding.rvMenuItems.adapter = menuItemAdapter
        binding.rvMenuItems.layoutManager = LinearLayoutManager(requireContext())

        val activity = requireActivity() as MainActivity
        val cartList = activity.listInMainActivity

        menuItemAdapter?.setOnBtnClickListener(object: MenuItemUserAdapter.OnBtnClickListener {
            override fun onBtnClick(item: MenuItem) {
                val i = cartList.indexOfFirst { it.cid == item.mid }
                if(i != -1){
                    cartList[i].qty += 1
                    cartList[i].price += item.price
                }
                else{
                    cartList.add(CartItem(
                        cid = item.mid,
                        name = item.name,
                        qty = 1,
                        isVeg = item.isVeg,
                        price = item.price
                    ))
                }
            }
        })

        menuItemAdapter?.setOnIncBtnClickListener(object: MenuItemUserAdapter.OnIncBtnClickListener{
            override fun onBtnClick(item: MenuItem) {
                val i = cartList.indexOfFirst { it.cid == item.mid }
                if(i != -1){
                    cartList[i].qty += 1
                }
                menuItemAdapter.notifyDataSetChanged()
            }

        })

        menuItemAdapter?.setOnDecBtnClickListener(object: MenuItemUserAdapter.OnDecBtnClickListener{
            override fun onBtnClick(item: MenuItem) {
                val i = cartList.indexOfFirst { it.cid == item.mid }
                if(i != -1){
                    cartList[i].qty -= 1
                }
                if (cartList[i].qty == 0) {
                    cartList.remove(cartList[i])
                }
                menuItemAdapter.notifyDataSetChanged()
            }

        })

        splMenuItemAdapter?.setOnBtnClickListener(object: MenuItemUserAdapter.OnBtnClickListener {
            override fun onBtnClick(item: MenuItem) {
                val i = cartList.indexOfFirst { it.cid == item.mid }
                if(i != -1){
                    cartList[i].qty += 1
                }
                else{
                    cartList.add(CartItem(
                        cid = item.mid,
                        name = item.name,
                        qty = 1,
                        isVeg = item.isVeg,
                        price = item.price
                    ))
                }
            }
        })

        splMenuItemAdapter?.setOnIncBtnClickListener(object: MenuItemUserAdapter.OnIncBtnClickListener{
            override fun onBtnClick(item: MenuItem) {
                val i = cartList.indexOfFirst { it.cid == item.mid }
                if(i != -1){
                    cartList[i].qty += 1
                }
                splMenuItemAdapter.notifyDataSetChanged()
            }

        })

        splMenuItemAdapter?.setOnDecBtnClickListener(object: MenuItemUserAdapter.OnDecBtnClickListener{
            override fun onBtnClick(item: MenuItem) {
                val i = cartList.indexOfFirst { it.cid == item.mid }
                if(i != -1){
                    cartList[i].qty -= 1
                }
                if (cartList[i].qty == 0) {
                    cartList.remove(cartList[i])
                }
                splMenuItemAdapter.notifyDataSetChanged()
            }

        })

        val db = Firebase.firestore

        fetchRush()

        db.collection("Menu").whereEqualTo("special", false).addSnapshotListener { snapshot, error ->
            if(error != null || snapshot == null){
                Log.i("MenuFragment", "Error when querying items", error)
            }
            if (snapshot != null) {
                val menuList = snapshot.toObjects(com.example.mobile_computing_project.models.MenuItem::class.java)
                menuItems.clear()
                menuItems.addAll(menuList)
                menuItemAdapter?.notifyDataSetChanged()
            }
        }

        db.collection("Menu").whereEqualTo("special", true).addSnapshotListener { snapshot, error ->
            if(error != null || snapshot == null){
                Log.i("MenuFragment", "Error when querying items", error)
            }
            if (snapshot != null) {
                val specialList = snapshot.toObjects(com.example.mobile_computing_project.models.MenuItem::class.java)
                specialItems.clear()
                specialItems.addAll(specialList)
                splMenuItemAdapter?.notifyDataSetChanged()
            }
        }
    }

    private fun fetchRush(){
        val db = Firebase.firestore
        db.collection("Orders").addSnapshotListener { value, error ->
            if(error != null || value== null){
                Log.i("MenuFragment", "Error when querying items", error)
            }
            if (value != null) {
                val orderList = value.size()
                if (orderList < 10){
                    binding.tvRushText.text = "Low"
                    binding.tvRushText.setTextColor(android.graphics.Color.parseColor("#049D4E"))
                    val drawable = context?.let { ContextCompat.getDrawable(it, R.drawable.rush_low) }
                    binding.ivRushGraph.setImageDrawable(drawable)
                }
                else{
                    binding.tvRushText.text = "High"
                    val drawable = context?.let { ContextCompat.getDrawable(it, R.drawable.rush_high) }
                    binding.ivRushGraph.setImageDrawable(drawable)
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MenuFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MenuFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}