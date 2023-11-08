package com.example.guardher

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale


class A3_Userpage : AppCompatActivity() {
    private val PERMISSION_REQUEST_CODE = 1
    lateinit var btn_alertFriends: Button
    lateinit var btn_userPb: Button
    lateinit var txt_toMapView: TextView

    var list: List<Address>? = null
    lateinit var database : UserBookDatabase
    val mAuth = FirebaseAuth.getInstance()
    private lateinit var mFusedLocationClient : FusedLocationProviderClient
    private val permissionId = 2

    lateinit var sharedPreferences: SharedPreferences
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a3_userpage)


        takePermissionsFromUser()
        btn_alertFriends = findViewById(R.id.btn_alertFriends)
        btn_userPb = findViewById(R.id.btn_userPb)
        txt_toMapView = findViewById(R.id.txt_toMapView)

        sharedPreferences = getSharedPreferences("SharedPref_GuardHer", MODE_PRIVATE)

        database = UserBookDatabase.getDatabase(this)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()
        btn_userPb.setOnClickListener {
            val i = Intent(this@A3_Userpage, A4_UserBook::class.java)
            startActivity(i)
        }

        btn_alertFriends.setOnClickListener {

            getNumber()
            Toast.makeText(this@A3_Userpage, "Alert Sent! Help Coming!!!", Toast.LENGTH_SHORT).show()

        }
        txt_toMapView.setOnClickListener {
            openMap()
        }
    }
    fun getNumber()
    {
        val userbookDao = database.UserBookDao()

        var uNumber = ""
        if(sharedPreferences.contains("uNum"))
        {
            uNumber = sharedPreferences.getString("uNum", null)!!
        }
        else
        {
            uNumber = mAuth.currentUser!!.phoneNumber.toString()
        }
        userbookDao.getPhoneNumbers(uNumber).observe(this){
            for(num in it)
            {
                sendSMS(num)
            }
        }
    }

    fun sendSMS(num : String)
    {
        val myMsg = StringBuffer()
        val mySmsManager = android.telephony.SmsManager.getDefault()
        val userLat: String = list!![0].latitude.toString()
        val userLong: String = list!![0].longitude.toString()
        myMsg.append("http://maps.google.com?q=");
        myMsg.append(userLat);
        myMsg.append(",");
        myMsg.append(userLong);
        mySmsManager.sendTextMessage("+91 ${num}",null, myMsg.toString(),null, null)


    }
    @SuppressLint("MissingPermission")
    private fun getLocation()
    {
        if(checkPermissions())
        {
            if(isLocationEnabled())
            {
                mFusedLocationClient.lastLocation.addOnSuccessListener {
                        location: Location? ->
                    location?.let {
                        val geoCoder = Geocoder(this, Locale.getDefault())
                        list = geoCoder.getFromLocation(location.latitude, location.longitude, 1)!!
                    }
                }
            }
            else
            {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
        else
        {
            requestPermissions()
        }
    }
    private fun isLocationEnabled() : Boolean {
        val locationManager : LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermissions() : Boolean
    {
        if(ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
        {
            return true
        }
        return false
    }
    private fun requestPermissions()
    {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION),
            permissionId
        )
    }
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if(requestCode == permissionId)
        {
            if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED))
            {
                getLocation()
            }
        }
    }

    fun takePermissionsFromUser()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_DENIED
            ) {
                Log.d("permission", "permission denied to SEND_SMS - requesting it")
                val permissions = arrayOf<String>(android.Manifest.permission.SEND_SMS)
                requestPermissions(permissions, PERMISSION_REQUEST_CODE)
            }
        }
    }

    fun openMap()
    {
        val gmmIntentUri = Uri.parse("geo:${list!![0].latitude},${list!![0].longitude}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)

    }

}