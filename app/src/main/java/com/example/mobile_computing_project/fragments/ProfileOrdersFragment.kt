package com.example.mobile_computing_project.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile_computing_project.adapters.user.OrderItemUserAdapter
import com.example.mobile_computing_project.databinding.FragmentProfileOrdersBinding
import com.example.mobile_computing_project.models.OrderItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "ProfileOrdersFragment"

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileOrdersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileOrdersFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentProfileOrdersBinding
    private val db = Firebase.firestore
    private var auth: FirebaseAuth = Firebase.auth

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
        binding = FragmentProfileOrdersBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderHistoryItems: MutableList<OrderItem> = mutableListOf()
        val orderHistoryItemsAdapter = OrderItemUserAdapter(orderHistoryItems)
        binding.rvOrderHistoryItems.adapter = orderHistoryItemsAdapter
        binding.rvOrderHistoryItems.layoutManager = LinearLayoutManager(requireContext())

        val orderHistoryRef = db.collection("Orders").whereEqualTo("user.uid", auth.currentUser?.uid)
        orderHistoryRef.addSnapshotListener { snapshot, error ->
            if(error != null || snapshot == null){
                Log.i(TAG, "Error when querying items", error)
            }
            if (snapshot != null) {
                val orderHistoryList = snapshot.toObjects(OrderItem::class.java)
                orderHistoryItems.clear()
                orderHistoryItems.addAll(orderHistoryList)
                orderHistoryItemsAdapter.notifyDataSetChanged()
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
         * @return A new instance of fragment ProfileOrders.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileOrdersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}