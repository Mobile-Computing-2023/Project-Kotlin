package com.example.mobile_computing_project.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_computing_project.MainActivity
import com.example.mobile_computing_project.R
import com.example.mobile_computing_project.adapters.CartItemAdapter
import com.example.mobile_computing_project.models.CartItem
import com.example.mobile_computing_project.models.OrderItem
import com.example.mobile_computing_project.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "CartFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderTotal: TextView
    private lateinit var btnPlaceOrder: Button
    private lateinit var btnClearCart: Button
    private var auth: FirebaseAuth = Firebase.auth
    private var signedInUser: User? = null

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
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        recyclerView = view.findViewById(R.id.rv_cart_items)
        orderTotal = view.findViewById(R.id.tv_order_total)
        btnPlaceOrder = view.findViewById(R.id.btn_place_order)
        btnClearCart = view.findViewById(R.id.btn_clear_cart)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as MainActivity
        val listOfItems = activity.listInMainActivity

        val adaptor = CartItemAdapter(listOfItems)
        recyclerView.adapter = adaptor
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        var total = 0
        listOfItems.forEach {
            total += (it.price*it.qty)
        }
        orderTotal.text = "Rs " + total.toString()

        adaptor.setOnBtnClickListener(object: CartItemAdapter.OnBtnClickListener {
            override fun onBtnClick(item: CartItem) {
                listOfItems.remove(item)
                total -= (item.price*item.qty)
                adaptor.notifyDataSetChanged()
                total = 0
                listOfItems.forEach {
                    total += (it.price*it.qty)
                }
                orderTotal.text = "Rs " + total.toString()
            }
        })

        adaptor.setOnIncBtnClickListener(object: CartItemAdapter.OnIncBtnClickListener{
            override fun onBtnClick(item: CartItem) {
                item.qty++
                adaptor.notifyDataSetChanged()
                total = 0
                listOfItems.forEach {
                    total += (it.price*it.qty)
                }
                orderTotal.text = "Rs " + total.toString()
            }
        })

        adaptor.setOnDecBtnClickListener(object: CartItemAdapter.OnDecBtnClickListener{
            override fun onBtnClick(item: CartItem) {
                item.qty--
                if (item.qty == 0) {
                    listOfItems.remove(item)
                }
                adaptor.notifyDataSetChanged()
                total = 0
                listOfItems.forEach {
                    total += (it.price*it.qty)
                }
                orderTotal.text = "Rs " + total.toString()
            }
        })

        val db = Firebase.firestore

        db.collection("Users").document(auth.currentUser?.uid as String).get().addOnSuccessListener {
            signedInUser = it.toObject(User::class.java)!!
        }.addOnFailureListener {error ->
            Log.i(TAG, "Failure in fetching current user", error)
        }

        btnPlaceOrder.setOnClickListener {
            if(signedInUser != null && listOfItems.isNotEmpty()){
                val order = OrderItem(oid = UUID.randomUUID().toString() ,items = listOfItems, user = signedInUser, amount = total, createdAt = System.currentTimeMillis())
                db.collection("Orders").document(order.oid).set(order).addOnSuccessListener {
                    Toast.makeText(context, "Order Placed Successfully!", Toast.LENGTH_SHORT).show()
                    listOfItems.clear()
                    orderTotal.text = null
                    val newAdapter = CartItemAdapter(listOfItems)
                    recyclerView.adapter = newAdapter
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                }.addOnFailureListener {
                    Toast.makeText(context, "There was some error in placing your order!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnClearCart.setOnClickListener {
            listOfItems.clear()
            orderTotal.text = null
            val newAdapter = CartItemAdapter(listOfItems)
            recyclerView.adapter = newAdapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}