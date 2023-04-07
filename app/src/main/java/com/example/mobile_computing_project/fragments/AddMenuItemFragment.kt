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
import com.example.mobile_computing_project.models.MenuItem
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class AddMenuItemFragment: DialogFragment() {
    private lateinit var btnDismiss: Button
    private lateinit var btnConfirmAddItem: Button
    private lateinit var etItemName: EditText
    private lateinit var etItemPrice: EditText
    private lateinit var btnChooseFile: Button
    private lateinit var rbVeg: RadioButton
    private lateinit var rbNonVeg: RadioButton
    private lateinit var ivImage: ImageView
    var isSpl = false
    private var photoURI: Uri? = null
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK){
                photoURI = it.data?.data
                Log.i("data", "$photoURI")
                ivImage.setImageURI(photoURI)
            }
            else{
                Toast.makeText(context, "Image Picking Action Canceled", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_item, container, false)
        btnDismiss = view.findViewById(R.id.btn_dismiss)
        btnConfirmAddItem = view.findViewById(R.id.btn_add_item)
        btnChooseFile = view.findViewById(R.id.btn_choose_file)
        etItemName = view.findViewById(R.id.et_name)
        etItemPrice = view.findViewById(R.id.et_price)
        rbVeg = view.findViewById(R.id.rb_veg)
        rbNonVeg = view.findViewById(R.id.rb_non_veg)
        isSpl = requireArguments().getBoolean("isSpl", false)
        ivImage = view.findViewById(R.id.iv_img_src)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnDismiss.setOnClickListener {
            dismiss()
        }

        btnConfirmAddItem.setOnClickListener {
            if(etItemName.text.isEmpty()){
                Toast.makeText(context, "Enter name!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(etItemPrice.text.isEmpty()){
                Toast.makeText(context, "Enter price!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(photoURI == null){
                Toast.makeText(context, "Select an Image!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            btnChooseFile.isEnabled = false
            addItemToMenu()
        }

        rbVeg.setOnCheckedChangeListener { _, isChecked ->
            rbNonVeg.isChecked = !isChecked
        }
        rbNonVeg.setOnCheckedChangeListener { _, isChecked ->
            rbVeg.isChecked = !isChecked
        }

        btnChooseFile.setOnClickListener {
            val imagePickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            imagePickerIntent.type = "image/*"
            getResult.launch(imagePickerIntent)
        }
    }

    private fun addItemToMenu(){
        val mid = UUID.randomUUID().toString()
        val storageRef = Firebase.storage.reference
        val photoRef = storageRef.child("images/${mid}--photo.jpg")
        val photoTask = photoRef.putFile(photoURI!!)
        photoTask.continueWithTask { photoRef.downloadUrl }.continueWithTask {
            val menuItem = MenuItem(
                mid = mid,
                name = etItemName.text.toString(),
                price = etItemPrice.text.toString().toInt(),
                isVeg = rbVeg.isChecked,
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