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
import com.example.mobile_computing_project.adapters.canteen.OrderItemCanteenAdapter
import com.example.mobile_computing_project.models.OrderItem
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrdersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val TAG = "OrderFragment"
class OrdersFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var orderItemsAdapter: OrderItemCanteenAdapter
    private var orderItems: MutableList<OrderItem> = mutableListOf()


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
        val view = inflater.inflate(R.layout.fragment_orders, container, false)
        orderItemsAdapter = OrderItemCanteenAdapter(orderItems)
        recyclerView = view.findViewById(R.id.rv_canteen_orders)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.adapter = orderItemsAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        orderItemsAdapter.setOnBtnClickListener(object: OrderItemCanteenAdapter.OnBtnClickListener {
            override fun onBtnClick(item: OrderItem) {
                markOrderAsComplete(item)
            }
        })

        val db = Firebase.firestore

        db.collection("Orders").whereEqualTo("status", "Pending").orderBy("createdAt", Query.Direction.DESCENDING).addSnapshotListener { snapshot, error ->
            if(error != null || snapshot == null){
                Log.i(TAG, "Error when querying items", error)
            }
            if (snapshot != null) {
                val orderList = snapshot.toObjects(OrderItem::class.java)
                orderItems.clear()
                orderItems.addAll(orderList)
                orderItemsAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun markOrderAsComplete(orderItem: OrderItem){
        val db = Firebase.firestore
        db.collection("Orders").document(orderItem.oid).update("status", "Completed")
            .addOnSuccessListener {
                Toast.makeText(context, "Order Completed", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "There was some error in completing order", Toast.LENGTH_SHORT).show()
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
         * @return A new instance of fragment OrdersFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrdersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}