package com.example.mobile_computing_project.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mobile_computing_project.databinding.FragmentComplaintBinding
import com.example.mobile_computing_project.models.ComplaintItem
import com.example.mobile_computing_project.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID

private const val TAG = "ComplaintFragment"

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ComplaintFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ComplaintFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentComplaintBinding
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
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentComplaintBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Firebase.firestore
        val auth = Firebase.auth

        db.collection("Users").document(auth.currentUser?.uid as String).get().addOnSuccessListener {
            signedInUser = it.toObject(User::class.java)
        }.addOnFailureListener {error ->
            Log.i(TAG, "Failure in fetching current user", error)
        }

        binding.btnSendComplaint.setOnClickListener {
            if(signedInUser != null){
                val complaint = ComplaintItem(cid = UUID.randomUUID().toString(), user = signedInUser, createdAt = System.currentTimeMillis(),
                    description = binding.etComplaintDesc.text.toString())
                db.collection("Complaints").document(complaint.cid).set(complaint)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Complaint Submitted Successfully", Toast.LENGTH_SHORT).show()
                        binding.etComplaintDesc.text.clear()
                        binding.btnSendComplaint.hint = binding.btnSendComplaint.hint
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to Submit Complaint", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        binding.btnClearComplaint.setOnClickListener {
            binding.etComplaintDesc.text.clear()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Complaint.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ComplaintFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}