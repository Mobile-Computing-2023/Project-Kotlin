package com.example.mobile_computing_project.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_computing_project.R
import com.example.mobile_computing_project.adapters.OrderItemUserAdapter
import com.example.mobile_computing_project.models.OrderItem
import com.example.mobile_computing_project.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "ProfileFragment"

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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

        val db = Firebase.firestore
        val binding = inflater.inflate(R.layout.fragment_profile, container, false)
        var userId = "USER ID"

        db.collection("Users").document(auth.currentUser?.uid as String).get().addOnSuccessListener {
            val signedInUser = it.toObject(User::class.java)!!
            binding.findViewById<TextView>(R.id.tv_my_name).text = signedInUser?.name
            binding.findViewById<TextView>(R.id.tv_email).text = signedInUser?.email
            userId = signedInUser?.uid.toString()
//            Toast.makeText(context, "User ID: $userId", Toast.LENGTH_SHORT).show()

            var orderHistoryItems: MutableList<OrderItem> = mutableListOf()
//            val orderHistoryItemAdapter =
//                context?.let { it1 -> OrderItemUserAdapter(it1, orderHistoryItems) }
//            val ordersRef = db.collection("Orders").whereEqualTo("user.uid", userId)
//                .addSnapshotListener { value, error ->
//                    if(error != null || value == null){
//                        Log.i(TAG, "Error when querying items", error)
//                    }
//                    if (value != null){
//                        val orderList = value.toObjects(OrderItem::class.java)
//                        orderHistoryItems.clear()
//                        orderHistoryItems.addAll(orderList)
//                        if (orderHistoryItemAdapter != null) {
//                            orderHistoryItemAdapter.notifyDataSetChanged()
//                        }
//                        else{
//                            Toast.makeText(context, "Problem Here", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
            Log.i(TAG, "Signed In User: $signedInUser")
        }.addOnFailureListener {error ->
            Log.i(TAG, "Failure in fetching current user", error)
        }

//        Toast.makeText(context, "User ID: $userId", Toast.LENGTH_SHORT).show()

        return binding
//        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}