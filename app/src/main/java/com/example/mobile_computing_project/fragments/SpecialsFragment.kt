package com.example.mobile_computing_project.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_computing_project.MainActivity
import com.example.mobile_computing_project.R
import com.example.mobile_computing_project.adapters.MenuItemAdapter
import com.example.mobile_computing_project.adapters.MenuItemCanteenAdapter
import com.example.mobile_computing_project.models.MenuItem
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SpecialsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SpecialsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private var menuItems: MutableList<com.example.mobile_computing_project.models.MenuItem> = mutableListOf()
    private lateinit var btnAddSplItemToMenu: Button
    private val isSpl = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_specials, container, false)
        recyclerView = view.findViewById(R.id.rv_menu_items)
        btnAddSplItemToMenu = view.findViewById(R.id.btn_add_spl_item_to_menu)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuItemAdapter = context?.let { MenuItemCanteenAdapter(context = it, menuItems = menuItems) }
        recyclerView.adapter = menuItemAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        menuItemAdapter?.setOnBtnClickListener(object: MenuItemCanteenAdapter.OnBtnClickListener {
            override fun onBtnClick(item: MenuItem) {
                removeItemFromMenu(item)
            }
        })

        val db = Firebase.firestore

        db.collection("Menu").whereEqualTo("special", true).addSnapshotListener { snapshot, error ->
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

        btnAddSplItemToMenu.setOnClickListener {
            val addItemFragment = AddMenuItemFragment()
            val args = Bundle()
            args.putBoolean("isSpl", isSpl)
            addItemFragment.arguments = args
            addItemFragment.show(childFragmentManager, "spl_popup")
            Toast.makeText(context, "Add a new spl item to menu now", Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeItemFromMenu(menuItem: MenuItem){
        val db = Firebase.firestore
        db.collection("Menu").document(menuItem.mid).delete().addOnSuccessListener {
            Toast.makeText(context, "Removed ${menuItem.name} from Specials", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "There was some error in removing ${menuItem.name} from Specials", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showButtonInActionBar(true)
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).showButtonInActionBar(false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SpecialsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SpecialsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}