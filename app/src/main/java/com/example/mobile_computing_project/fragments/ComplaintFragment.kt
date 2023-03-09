package com.example.mobile_computing_project.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mobile_computing_project.R
import com.example.mobile_computing_project.models.ComplaintItem
import com.example.mobile_computing_project.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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

    private lateinit var btnSendComplaint: Button
    private lateinit var btnClear: Button
    private lateinit var etComplaintContent: EditText

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
        val view = inflater.inflate(R.layout.fragment_complaint, container, false)
        btnSendComplaint = view.findViewById(R.id.btn_send_complaint)
        btnClear = view.findViewById(R.id.btn_clear_complaint)
        etComplaintContent = view.findViewById(R.id.et_complaint_hint)

        val db = Firebase.firestore
        val auth = Firebase.auth
        val currUid = auth.currentUser?.uid.toString()


        btnSendComplaint.setOnClickListener {
            val complaint = ComplaintItem(userid = currUid, createdAt = System.currentTimeMillis(),
                description = etComplaintContent.text.toString())
            db.collection("Complaints").add(complaint)
                .addOnSuccessListener {
                Toast.makeText(context, "Complaint Submitted Successfully", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener { Toast.makeText(context, "Failed to Submit Complaint", Toast.LENGTH_SHORT).show() }

            etComplaintContent.text.clear()
            btnSendComplaint.hint = btnSendComplaint.hint
        }

        btnClear.setOnClickListener {
            etComplaintContent.text.clear()
            btnSendComplaint.hint = btnSendComplaint.hint
        }

        return view
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