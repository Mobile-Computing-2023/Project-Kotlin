package com.example.mobile_computing_project.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_computing_project.MainActivity
import com.example.mobile_computing_project.R
import com.example.mobile_computing_project.adapters.MenuItemAdapter
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
    private lateinit var recyclerView: RecyclerView
    private lateinit var splRecyclerView: RecyclerView
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
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        recyclerView = view.findViewById(R.id.rv_menu_items)
        splRecyclerView = view.findViewById(R.id.rv_spl_menu_items)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuItemAdapter = MenuItemAdapter(menuItems)
        recyclerView.adapter = menuItemAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val activity = requireActivity() as MainActivity
        val listOfItems = activity.listInMainActivity
        menuItemAdapter.setOnBtnClickListener(object: MenuItemAdapter.OnBtnClickListener {
            override fun onBtnClick(item: MenuItem) {
                if (listOfItems.size == 0){
                    listOfItems.add(CartItem(
                        name = item.name,
                        qty = 1,
                        isVeg = item.isVeg,
                        price = item.price
                    ))
                }

                else{
                    var added = false
                    for (i in listOfItems){
                        if (!added && i.name == item.name){
                            i.qty+=1
                            i.price+=item.price
                            added = true
                        }
                    }

                    if (!added){
                        listOfItems.add(CartItem(
                            name = item.name,
                            qty = 1,
                            isVeg = item.isVeg,
                            price = item.price
                        ))
                    }
                }

                println("Main List: " + listOfItems)
                Toast.makeText(context, "Clicked Button: " + item.name, Toast.LENGTH_SHORT).show()
            }

        })

        // For SPECIALS:
        val splMenuItemAdapter = MenuItemAdapter(specialItems)
        splRecyclerView.adapter = splMenuItemAdapter
        splRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val db = Firebase.firestore
        val menuReference = db.collection("Menu")
        menuReference.addSnapshotListener { snapshot, error ->
            if(error != null || snapshot == null){
                Log.i("MenuFragment", "Error when querying items", error)
            }
            if (snapshot != null) {
                val menuList = snapshot.toObjects(com.example.mobile_computing_project.models.MenuItem::class.java)
                menuItems.clear()
                menuItems.addAll(menuList)
                menuItemAdapter.notifyDataSetChanged()
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