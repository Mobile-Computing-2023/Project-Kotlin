package com.example.mobile_computing_project

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.mobile_computing_project.databinding.ActivityMainBinding
import com.example.mobile_computing_project.fragments.*
import com.example.mobile_computing_project.models.CartItem
import com.example.mobile_computing_project.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private val CHANNEL_ID = "Notification Channel"
    private val notificationId = 101

    private lateinit var binding: ActivityMainBinding
    private var signedInUser: User? = null
    private lateinit var auth: FirebaseAuth
    var listInMainActivity: MutableList<CartItem> = mutableListOf()
    private var menuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        val colorDrawable = ColorDrawable(Color.parseColor("#FFFFFF"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        val db = Firebase.firestore

        db.collection("Users").document(auth.currentUser?.uid as String).get().addOnSuccessListener { it ->
            signedInUser = it.toObject(User::class.java)!!
            Log.i(TAG, "Signed In User: $signedInUser")
            if(signedInUser?.canteen == true){
                binding.bottomNavCanteen.visibility = View.VISIBLE
                replaceFragment(OrdersFragment())
            }
            else{
                binding.bottomNavUser.visibility = View.VISIBLE
                replaceFragment(MenuFragment())
            }
        }.addOnFailureListener {error ->
            Log.i(TAG, "Failure in fetching current user", error)
        }

        binding.bottomNavCanteen.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu -> replaceFragment(CanteenMenuFragment())
                R.id.orders -> replaceFragment(OrdersFragment())
                R.id.complaint -> replaceFragment(CanteenComplaintFragment())
                R.id.specials -> replaceFragment(SpecialsFragment())
                else -> {}
            }
            true
        }

        binding.bottomNavUser.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu -> replaceFragment(MenuFragment())
                R.id.cart -> replaceFragment(CartFragment())
                R.id.complaint -> replaceFragment(ComplaintFragment())
                R.id.profile -> {
                    replaceFragment(ProfileFragment())
                }
                else -> {}
            }
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout, menu)
        menuItem = menu?.findItem(R.id.btn_logout)!!
        menuItem?.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.btn_logout) {
            auth.signOut()
            goToLandingActivity()
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    fun showButtonInActionBar(show: Boolean) {
        menuItem?.isVisible = show
    }

    private fun goToLandingActivity(){
        Log.i(TAG, "goToLandingActivity")
        val intent = Intent(this, LandingActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendNotification() {
        val builder = Notification.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Example Title")
            .setContentText("Example Description")
            .setPriority(Notification.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(notificationId, builder.build())
        }
    }
}