package com.projectbelajar.yuukbelajar.maps

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.projectbelajar.yuukbelajar.R
import com.projectbelajar.yuukbelajar.model.User_Parent


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var marker : Marker
    private lateinit var cameraPosition: CameraPosition
    var defaultLongitude = -122.088426
    var defaultLatitude  = 37.388064

    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.getReference("Users")
    private var user = FirebaseAuth.getInstance().currentUser
    private var datas =  ArrayList<User_Parent>()

    private var long : Double ?= 0.0
    private var lat : Double ?= 0.0
    private var nis : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

//        markerOptions = MarkerOptions()
//        val latLng = LatLng(defaultLatitude,defaultLongitude)
//        markerOptions.position(latLng)
//        cameraPosition = CameraPosition.Builder()
//            .target(latLng)
//            .zoom(17f).build()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        val height = 100
        val width = 100

        val assetMap = BitmapFactory.decodeResource(resources, R.drawable.boy1)
        val assetModify = Bitmap.createScaledBitmap(assetMap, width, height, false)

        val defaultLocation = LatLng(defaultLatitude, defaultLongitude)
        mMap = googleMap
        marker = mMap.addMarker(MarkerOptions().position(defaultLocation)
                .icon(BitmapDescriptorFactory.fromBitmap(assetModify)))





        myRef.addValueEventListener(object  : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                datas.clear()
                for (data in snapshot.children){
                    if (user?.email == data.child("email").value.toString()){
                        nis  = data.child("nis").value.toString()
                        
                    }else if (nis == data.child("nis").value.toString()
                            && data.child("level").value.toString() == "SISWA"){
                        val name = data.child("nama").value.toString()
                        long = data.child("long").value.toString().toDouble()
                        lat = data.child("lat").value.toString().toDouble()

                        marker.title = "Posisi Ananda $name"
                        marker.position = LatLng(lat!!, long!!)
                        cameraPosition = CameraPosition.Builder()
                                .target(LatLng(lat!!, long!!))
                                .zoom(17f).build()
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    }
                }
            }
        })

    }
}