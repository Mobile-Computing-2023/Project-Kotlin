package com.example.mobile_computing_project.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.mobile_computing_project.MainActivity
import com.example.mobile_computing_project.R
import com.example.mobile_computing_project.adapters.user.ComplaintItemUserAdapter
import com.example.mobile_computing_project.adapters.user.OrderItemUserAdapter
import com.example.mobile_computing_project.adapters.user.TabsAdapter
import com.example.mobile_computing_project.databinding.FragmentProfileBinding
import com.example.mobile_computing_project.models.ComplaintItem
import com.example.mobile_computing_project.models.OrderItem
import com.example.mobile_computing_project.models.User
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
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

    private lateinit var binding: FragmentProfileBinding
    private var auth: FirebaseAuth = Firebase.auth
    private var signedInUser: User? = null
    private val db = Firebase.firestore
    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView

    private lateinit var tabsAdapter: TabsAdapter
    private lateinit var viewPager: ViewPager2

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
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        db.collection("Users").document(auth.currentUser?.uid as String).get().addOnSuccessListener {
            signedInUser = it.toObject(User::class.java)!!
            binding.tvName.text = signedInUser!!.name
            binding.tvEmail.text = signedInUser!!.email
            Log.i(TAG, "Signed In User: $signedInUser")
        }.addOnFailureListener {error ->
            Log.i(TAG, "Failure in fetching current user", error)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderHistoryItems: MutableList<OrderItem> = mutableListOf()
        val orderHistoryItemsAdapter = OrderItemUserAdapter(orderHistoryItems)
        binding.rvOrderHistoryItems.adapter = orderHistoryItemsAdapter
        binding.rvOrderHistoryItems.layoutManager = LinearLayoutManager(requireContext())

        val complaintHistoryItems: MutableList<ComplaintItem> = mutableListOf()
        val complaintHistoryItemsAdapter = ComplaintItemUserAdapter(complaintHistoryItems)
        binding.rvComplaintHistoryItems.adapter = complaintHistoryItemsAdapter
        binding.rvComplaintHistoryItems.layoutManager = LinearLayoutManager(requireContext())

        tabsAdapter = TabsAdapter(this)
        viewPager = view.findViewById(R.id.pager_profile)
        viewPager.adapter = tabsAdapter

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout_profile)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if (position == 0) {
                tab.text = "Orders"
            }
            else {
                tab.text = "Complaints"
            }
        }.attach()

        val orderHistoryRef = db.collection("Orders").whereEqualTo("status", "Completed").whereEqualTo("user.uid", auth.currentUser?.uid)
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

        val complaintHistoryRef = db.collection("Complaints").whereEqualTo("user.uid", auth.currentUser?.uid)
        complaintHistoryRef.addSnapshotListener { snapshot, error ->
            if(error != null || snapshot == null){
                Log.i(TAG, "Error when querying items", error)
            }
            if (snapshot != null) {
                val complaintHistoryList = snapshot.toObjects(ComplaintItem::class.java)
                complaintHistoryItems.clear()
                complaintHistoryItems.addAll(complaintHistoryList)
                complaintHistoryItemsAdapter.notifyDataSetChanged()
            }
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