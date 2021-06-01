package com.projectbelajar.yuukbelajar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.projectbelajar.yuukbelajar.fragment.CourseFragment
import com.projectbelajar.yuukbelajar.fragment.ProfileFragment
import com.projectbelajar.yuukbelajar.fragment.UksFragment
import com.projectbelajar.yuukbelajar.model.User_Parent

class Tabbed : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private val PERMISSION_ID = 1010
    private var database = FirebaseDatabase.getInstance()
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    private var myRef = database.getReference("Users")

    var id: String? = null

    var doubleBackToExitPressedOnce = false
    private var preference: Preferences? = null

    init {
        preference = Preferences(this)
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment? = null
        when (item.itemId) {
            R.id.nav_profile -> selectedFragment = ProfileFragment()
            R.id.nav_home -> selectedFragment = CourseFragment()
            R.id.nav_more -> selectedFragment = UksFragment()
        }
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                selectedFragment!!).commit()
        //                    }
        true
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (preference?.getValues("level") == "SISWA") {

            initVar()
            initFirebase()
            getLastLocation()

//            var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//                    || !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
//                showAlertLocation()
//            }
//            locationManager.isLocationEnabled

            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                Log.d("Debug fused Location", "${location}")
                Log.d("Location Provide", "berhasil masuk location Provide")
                myRef.child(id ?: "").child("long").setValue(location?.longitude)
                myRef.child(id ?: "").child("lat").setValue(location?.latitude)
            }
        }


        //        if (getIntent().getExtras().getString("level").equals("DOKTER")){
//            setContentView(R.layout.activity_tabbed_dokter);
//            BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_dokter);
//            bottomNav.setOnNavigationItemSelectedListener(navListener);
//            bottomNav.setSelectedItemId(R.id.nav_more);
//        } else {
        setContentView(R.layout.activity_tabbed2)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(navListener)
        bottomNav.selectedItemId = R.id.nav_home
        //        }
//        spaceNavigationView = findViewById(R.id.space);
//        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
//        spaceNavigationView.setCentreButtonSelectable(true);
//        spaceNavigationView.setCentreButtonSelected();
//        spaceNavigationView.setCentreButtonIcon(R.drawable.ic_home_24);
//        spaceNavigationView.addSpaceItem(new SpaceItem("UKS", R.drawable.icon_uks));
//        spaceNavigationView.addSpaceItem(new SpaceItem("PROFILE", R.drawable.ic_person_24));
//        spaceNavigationView.showTextOnly();
//        if (savedInstanceState == null && getIntent().getExtras().getString("level").equals("DOKTER")) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_dokter,
//                    new UksFragment()).commit();
//        }else
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    CourseFragment()).commit()
        }
        //        Intent intent;

//        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
//            @Override
//            public void onCentreButtonClick() {
//                setFragment(new CourseFragment());
//            }
//
//            @Override
//            public void onItemClick(int itemIndex, String itemName) {
//                switch (itemIndex) {
//                    case 0:
//                        setFragment(new UksFragment());
//                        break;
//                    case 1:
//                        setFragment(new ProfileFragment());
//                        break;
//                }
//                Toast.makeText(Tabbed.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onItemReselected(int itemIndex, String itemName) {
//                Toast.makeText(Tabbed.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
//            }
//        });
//        intent = new Intent().setClass(this, CourseActivity.class);//content pada tab yang akan kita buat
//        spec = tabhost.newTabSpec("Beranda").setIndicator("Beranda",null).setContent(intent);//mengeset nama tab dan mengisi content pada menu tab anda.
//        tabhost.addTab(spec);//untuk membuat tabbaru disini bisa diatur sesuai keinginan anda
//
//        intent = new Intent().setClass(this, tampilan.class);
//        spec = tabhost.newTabSpec("Profil").setIndicator("Profil",null).setContent(intent);
//        tabhost.addTab(spec);
    }

//    private fun showAlertLocation() {
//        val dialog = AlertDialog.Builder(this)
//        dialog.setMessage("Your location settings is set to Off, Please enable location to use this application")
//        dialog.setPositiveButton("Settings") { _, _ ->
//            val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//            startActivity(myIntent)
//        }
//        dialog.setNegativeButton("Cancel") { _, _ ->
//            finish()
//        }
//        dialog.setCancelable(false)
//        dialog.show()
//    }

    private fun initVar() {
        id = FirebaseAuth.getInstance().currentUser?.uid
    }

    private fun initFirebase() {

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser
    }


    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    private fun restart() {
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }


    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        if (CheckPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        NewLocationData()
                    } else {
                        Log.d("Debug:", "Your Location:" + location.longitude)
                    }
                }
            } else {
                Toast.makeText(this, "Please Turn on Your device Location", Toast.LENGTH_SHORT).show()
            }
        } else {
            RequestPermission()
        }
    }


    @SuppressLint("MissingPermission")
    fun NewLocationData() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.myLooper()
        )
    }


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            Log.d("Debug:", "your last last location: " + lastLocation.longitude.toString())
//            tv_Text.text = "You Last Location is : Long: "+ lastLocation.longitude + " , Lat: " + lastLocation.latitude + "\n" + getCityName(lastLocation.latitude,lastLocation.longitude)
        }
    }

    private fun CheckPermission(): Boolean {
        //this function will return a boolean
        //true: if we have permission
        //false if not
        if (
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }

        return false

    }

    fun RequestPermission() {
        //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
        ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_ID
        )
    }

    fun isLocationEnabled(): Boolean {
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)
    }


    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Debug:", "You have the Permission")
                restart()
            }
        }
    }
}