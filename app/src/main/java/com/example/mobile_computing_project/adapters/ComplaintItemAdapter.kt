package com.example.mobile_computing_project.adapters

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_computing_project.R
import com.example.mobile_computing_project.models.ComplaintItem
import com.example.mobile_computing_project.models.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class ComplaintItemAdapter (private val complaints: List<ComplaintItem>):
    RecyclerView.Adapter<ComplaintItemAdapter.ViewHolder>() {

    interface OnBtnClickListener {
        fun onBtnClick(item: ComplaintItem)
    }
    private var listener: OnBtnClickListener? = null

    fun setOnBtnClickListener(listener: OnBtnClickListener){
        this.listener = listener
    }

    private lateinit var tvDescription: TextView
    private lateinit var tvUser: TextView
    private lateinit var tvCreatedAt: TextView
    private lateinit var btnResolve: Button

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(complaintItem: ComplaintItem, listener: OnBtnClickListener?){
            tvDescription.text = complaintItem.description
            tvUser.text = "Complainant: " + complaintItem.userid
            val db = Firebase.firestore
            db.collection("Users")
                .whereEqualTo("uid", complaintItem.userid)
                .get()
                .addOnSuccessListener {docs ->
                    for (d in docs){
                        val complainant = d.toObject<User>()
                        println(complainant)
                        tvUser.text = "Complainant: " + complainant.name
                    }
                }
            tvCreatedAt.text = DateUtils.getRelativeTimeSpanString(complaintItem.createdat)
            btnResolve.setOnClickListener {
                listener?.onBtnClick(complaintItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.complaint_item, parent, false)
        tvDescription = view.findViewById(R.id.tv_description)
        tvUser = view.findViewById(R.id.tv_user)
        tvCreatedAt = view.findViewById(R.id.tv_createdAt)
        btnResolve = view.findViewById(R.id.btn_resolve)
        return ViewHolder(view)
    }

    override fun getItemCount() = complaints.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(complaints[position], listener)
    }

}