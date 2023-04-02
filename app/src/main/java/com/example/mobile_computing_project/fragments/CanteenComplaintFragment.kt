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
import com.example.mobile_computing_project.adapters.ComplaintItemAdapter
import com.example.mobile_computing_project.models.ComplaintItem
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG = "CanteenComplaintFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [CanteenComplaintFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CanteenComplaintFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private var complaintItems: MutableList<ComplaintItem> = mutableListOf()

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
        val view = inflater.inflate(R.layout.fragment_canteen_complaint, container, false)
        recyclerView = view.findViewById(R.id.rv_canteen_complaint)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val complaintItemAdapter = ComplaintItemAdapter(complaintItems)
        complaintItemAdapter.setOnBtnClickListener(object : ComplaintItemAdapter.OnBtnClickListener{
            override fun onBtnClick(item: ComplaintItem) {
                resolveComplaint(item)
            }

        })
        recyclerView.adapter = complaintItemAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val db = Firebase.firestore
        val complaintsReference = db.collection("Complaints").whereEqualTo("resolved", false).orderBy("createdAt", Query.Direction.DESCENDING)
        complaintsReference.addSnapshotListener { snapshot, error ->
            if(error != null || snapshot == null){
                Log.i(TAG, "Error when querying items", error)
            }
            if (snapshot != null) {
                val complaintList = snapshot.toObjects(ComplaintItem::class.java)
                complaintItems.clear()
                complaintItems.addAll(complaintList)
                complaintItemAdapter.notifyDataSetChanged()
                println(complaintList)
            }
        }
    }

    private fun resolveComplaint(complaintItem: ComplaintItem){
        val db = Firebase.firestore
        db.collection("Complaints").document(complaintItem.cid).update("resolved", true).addOnSuccessListener {
            Toast.makeText(context, "Complaint Resolved", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            println(it.message)
            Toast.makeText(context, "Some error while resolving complaint", Toast.LENGTH_SHORT).show()
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
         * @return A new instance of fragment CanteenComplaintFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CanteenComplaintFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}