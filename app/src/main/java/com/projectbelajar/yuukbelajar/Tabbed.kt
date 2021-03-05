package com.projectbelajar.yuukbelajar

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
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

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    private val PERMISSION_ID = 1010
    private var database = FirebaseDatabase.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private var myRef = database.getReference("Users")
    private var user_parent = ArrayList<User_Parent>()

    var email: String? = null
    var id: String? = null
    var imgUrl: String? = null
    var kelas: String? = null
    var kodeSekolah: String? = null
    var level: String? = null
    var nama: String? = null
    var namaSekolah: String? = null
    var nis: String? = null
    var password: String? = null
    var sort_nama: String? = null
    var status: String? = null
    var time: String? = null
    var username: String? = null


    var doubleBackToExitPressedOnce = false
    var getIntent : String ?= null

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



        getIntent = intent.getStringExtra("level")
        Log.d("Test Intent", "$getIntent")

        if (getIntent == "SISWA") {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

            initFirebase()
            getLastLocation()

            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                Log.d("Debug fused Location", "${location}")
                Log.d("Location Provide", "berhasil masuk location Provide")
                myRef.child(id ?: "")
                        .setValue(User_Parent(location?.longitude, location?.latitude,
                                email, id, imgUrl, kelas, kodeSekolah,
                                level, nama, namaSekolah, nis, password, sort_nama, status, time, username))
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

    private fun initFirebase() {

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("Error Database", error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    if (data.child("email").value.toString() == user?.email ?: false) {
                        val key = data.key

                        email = data.child("email").value.toString()
                        id = data.child("id").value.toString()
                        imgUrl = data.child("imgUrl").value.toString()
                        kelas = data.child("kelas").value.toString()
                        kodeSekolah = data.child("kodeSekolah").value.toString()
                        level = data.child("level").value.toString()
                        nama = data.child("nama").value.toString()
                        namaSekolah = data.child("namaSekolah").value.toString()
                        nis = data.child("nis").value.toString()
                        password = data.child("password").value.toString()
                        sort_nama = data.child("sort_nama").value.toString()
                        status = data.child("status").value.toString()
                        time = data.child("time").value.toString()
                        username = data.child("username").value.toString()

                    }
                }
            }
        })
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
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
        fusedLocationProviderClient!!.requestLocationUpdates(
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