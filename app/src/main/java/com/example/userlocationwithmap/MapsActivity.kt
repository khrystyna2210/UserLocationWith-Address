package com.example.userlocationwithmap

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.userlocationwithmap.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.Marker

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var myMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val lat = intent.getDoubleExtra("lat", -34.0)
        val lng = intent.getDoubleExtra("lng", 151.0)

        val location = LatLng(lat, lng)
        myMarker = mMap.addMarker(
            MarkerOptions()
                .position(location)
                .title(getAddress(location)))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        mMap.setOnMarkerClickListener(this)

    }

    override fun onMarkerClick(p0: Marker): Boolean {

        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("address", p0.title)
        clipboardManager.setPrimaryClip(clipData)

        return false
    }



    private fun getAddress(latLng: LatLng): String {
        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        return list!![0].getAddressLine(0)
    }


}