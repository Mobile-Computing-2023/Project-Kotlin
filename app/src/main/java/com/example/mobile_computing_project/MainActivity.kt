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
import com.example.mobile_computing_project.models.OrderItem
import com.example.mobile_computing_project.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject
import kotlin.math.roundToInt
import kotlin.properties.Delegates

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(), PaymentResultListener {
//    private val CHANNEL_ID = "Notification Channel"
//    private val notificationId = 101

    private lateinit var binding: ActivityMainBinding
    private var signedInUser: User? = null
    private lateinit var auth: FirebaseAuth
    var listInMainActivity: MutableList<CartItem> = mutableListOf()
    private var menuItem: MenuItem? = null
    var total: Int = 0
    lateinit var orderItem: OrderItem
    private var firstTime by Delegates.notNull<Boolean>()
    private var currSplItems by Delegates.notNull<Int>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Firebase.firestore

        firstTime = true
        createNotificationChannel()

        val colorDrawable = ColorDrawable(Color.parseColor("#FFFFFF"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

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

        db.collection("Menu").whereEqualTo("special", true).addSnapshotListener { snapshot, error ->
            if(error != null || snapshot == null){
                Log.i("MenuFragment", "Error when querying items", error)
            }
            if (snapshot != null) {
                val specialList = snapshot.toObjects(com.example.mobile_computing_project.models.MenuItem::class.java)
//                if (!firstCall) {
////                    val mainActivity = MainActivity()
////                    mainActivity.sendNotification()
////                    if(context != null){
////                        sendNotification()
////                    }
//                }
//                else {
//                    firstCall = false
//                }
                val count = snapshot.size()
                if (firstTime) {
                    currSplItems = count
                    firstTime = false
                }
                else if (count > currSplItems) {
                    sendNotification()
                }
//                Toast.makeText(applicationContext, "Count = $count CurrSplItems = $currSplItems", Toast.LENGTH_SHORT).show()
                currSplItems = count
            }
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendNotification() {
        val builder = Notification.Builder(applicationContext, "CHANNEL_ID")
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Special Item")
            .setContentText("Added a new Special Item!!")
            .setPriority(Notification.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(applicationContext)) {
            if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }

//            if (!firstTime) {
//                notify(101, builder.build())
//            }
//            else {
//                firstTime = false
//            }
            notify(101, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun savePayments(total: Float) {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_9KR02wS5SYpTKE")
        try {
            val amountValue = (total * 100).roundToInt()

            val options = JSONObject()
            options.put("name", "Canteen Hub")
            options.put("description", "Your one stop shop for delicious campus eats.")
            options.put("theme.color", "#1004581")
            options.put("currency", "INR")
            options.put("amount", amountValue)

            val prefill = JSONObject()
            prefill.put("email", "aryan20499@iiitd.ac.in")
            prefill.put("contact", "9711171503")
            options.put("prefill", prefill)

            checkout.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "SUN BKL: " + e.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        val db = Firebase.firestore
        db.collection("Orders").document(orderItem.oid).set(orderItem).addOnSuccessListener {
            listInMainActivity.clear()
            total = 0
            orderItem = OrderItem()
            replaceFragment(MenuFragment())
            Toast.makeText(this, "Order Placed Successfully!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "There was some error in placing your order!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Error in Payment", Toast.LENGTH_LONG).show()
    }
}