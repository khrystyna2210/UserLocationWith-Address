package com.example.userlocationwithmap

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.media.audiofx.Equalizer.Settings
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class MainActivity : AppCompatActivity() {

    private lateinit var tvLongitude: TextView
    private lateinit var tvLatitude: TextView
    private lateinit var btnShowMap: Button
    private var lat: Double = 0.0
    private var lng: Double = 0.0

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    private fun init(){
        tvLongitude = findViewById(R.id.tvLongitude)
        tvLatitude = findViewById(R.id.tvLatitude)
        btnShowMap = findViewById(R.id.btnMap)
    }

    fun onClickShowMap(view: View){
        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra("lat", lat)
        intent.putExtra("lng", lng)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getCurrentLocation()

    }

    private fun getCurrentLocation() {


        if(checkPermission()){
            if(isLocationEnabled()){

                fusedLocationProviderClient.getLastLocation().addOnCompleteListener {task ->
                    val location: Location? = task.result
                    if(location==null){
                        Toast.makeText(this, "Null received", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "Get success", Toast.LENGTH_SHORT).show()
                        lat = location.latitude
                        lng = location.longitude
                        tvLatitude.text = location.latitude.toString()
                        tvLongitude.text = location.longitude.toString()
                    }

                }

            }else{
                //setting open here
                Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }else{
            //request permission here
            requestPermission()
        }

    }

    private fun isLocationEnabled():Boolean{
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    companion object{
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    private fun checkPermission(): Boolean{
        if(ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION)
            ==PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
            ==PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode== PERMISSION_REQUEST_ACCESS_LOCATION){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            }else{
                Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}