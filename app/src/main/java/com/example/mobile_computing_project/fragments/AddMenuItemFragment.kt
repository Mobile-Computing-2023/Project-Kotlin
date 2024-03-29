package com.example.mobile_computing_project.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.example.mobile_computing_project.R
import com.example.mobile_computing_project.databinding.FragmentAddItemBinding
import com.example.mobile_computing_project.models.MenuItem
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class AddMenuItemFragment: DialogFragment() {
    private lateinit var binding: FragmentAddItemBinding

    var isSpl = false
    private var photoURI: Uri? = null
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK){
                photoURI = it.data?.data
                Log.i("data", "$photoURI")
                binding.ivImgSrc.setImageURI(photoURI)
            }
            else{
                Toast.makeText(context, "Image Picking Action Canceled", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isSpl = requireArguments().getBoolean("isSpl", false)
        binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDismiss.setOnClickListener {
            dismiss()
        }

        binding.btnAddItem.setOnClickListener {
            if(binding.etName.text.isEmpty()){
                Toast.makeText(context, "Enter name!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(binding.etPrice.text.isEmpty()){
                Toast.makeText(context, "Enter price!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(photoURI == null){
                Toast.makeText(context, "Select an Image!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            binding.btnChooseFile.isEnabled = false
            addItemToMenu()
        }

        binding.rbVeg.setOnCheckedChangeListener { _, isChecked ->
            binding.rbNonVeg.isChecked = !isChecked
        }
        binding.rbNonVeg.setOnCheckedChangeListener { _, isChecked ->
            binding.rbVeg.isChecked = !isChecked
        }

        binding.btnChooseFile.setOnClickListener {
            val imagePickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            imagePickerIntent.type = "image/*"
            getResult.launch(imagePickerIntent)
        }
    }

    private fun addItemToMenu(){
        val storageRef = Firebase.storage.reference
        val photoRef = storageRef.child("images/${UUID.randomUUID()}--photo.jpg")
        val photoTask = photoRef.putFile(photoURI!!)
        photoTask.continueWithTask { photoRef.downloadUrl }.continueWithTask {
            val menuItem = MenuItem(
                mid = UUID.randomUUID().toString(),
                name = binding.etName.text.toString(),
                price = binding.etPrice.text.toString().toInt(),
                isVeg = binding.rbVeg.isChecked,
                special = isSpl,
                imgSrc = it.result.toString()
            )
            val db  = Firebase.firestore
            db.collection("Menu").document(menuItem.mid).set(menuItem)
                .addOnSuccessListener {
                    Toast.makeText(context, "Added Item", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "There was some error while adding item", Toast.LENGTH_SHORT).show()
                }
        }
    }
}