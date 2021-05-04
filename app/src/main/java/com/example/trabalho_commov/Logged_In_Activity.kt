package com.example.trabalho_commov

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.trabalho_commov.api.EndPoints
import com.example.trabalho_commov.api.Note
import com.example.trabalho_commov.api.ServiceBuilder
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ipvc.estg.room.Main_Menu
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Logged_In_Activity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var notas: List<Note>
    lateinit var preferences: SharedPreferences
    // add to implement last known location
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    //added to implement location periodic updates
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var locAdd: LatLng


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged__in_)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        preferences = getSharedPreferences("SharedLogin", Context.MODE_PRIVATE);
        val myIntValue: Int = preferences.getInt("ID_PESSOA", -1)

        // initialize fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        val buttonall: Button = findViewById(R.id.button)
        val button: Button = findViewById(R.id.button2)
        val buttonOut: Button = findViewById(R.id.buttonSIGNOUT)


        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getNotas()
        var position: LatLng















        call.enqueue(object : Callback<List<Note>> {
            override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>) {
                if (response.isSuccessful) {
                    notas = response.body()!!
                    for (nota in notas) {
                        if (nota.id_pessoa == (myIntValue)) {
                            position = LatLng(
                                nota.lat.toString().toDouble(),
                                nota.lng.toString().toDouble()
                            )
                            mMap.addMarker(
                                MarkerOptions().position(position)
                                    .title((nota.titulo + " - " + nota.descricao)).icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                                )
                            )
                        } else {
                            position = LatLng(
                                nota.lat.toString().toDouble(),
                                nota.lng.toString().toDouble()
                            )
                            mMap.addMarker(
                                MarkerOptions().position(position)
                                    .title((nota.titulo + " - " + nota.descricao))
                            )
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                Toast.makeText(this@Logged_In_Activity, "Credencias Incorretos", Toast.LENGTH_SHORT)
                    .show()
            }


        })












        button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        buttonall.setOnClickListener {
            val intent = Intent(this, ALLNotas::class.java)
            startActivity(intent)
        }

        buttonOut.setOnClickListener {
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.clear()
            editor.commit()
            val intent = Intent(this, Main_Menu::class.java)
            startActivity(intent)
        }



        //added to implement location periodic updates
        //falta add verificação no ismylocationtrue
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                var loc = LatLng(lastLocation.latitude, lastLocation.longitude)
                locAdd = loc
                mMap.isMyLocationEnabled = true
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15.0f))

                // preenche as coordenadas
                Log.d("Coords",loc.latitude.toString() )
                Log.d("Coords",loc.longitude.toString() )
                // reverse geocoding
                val address = getAddress(lastLocation.latitude, lastLocation.longitude)


                Log.d("** Hugo", "new location received - " + loc.latitude + " -" + loc.longitude)
            }
        }

        // request creation
        createLocationRequest()

    }

    override fun onStart() {
        super.onStart()

        val userPref = preferences.getString("USERNAME", null)
        if(userPref.isNullOrBlank()){
            val intent = Intent(this, Main_Menu::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()


        startLocationUpdates()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }



    //Alterado
    companion object {
        // add to implement last known location
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        //added to implement location periodic updates
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    //added to implement location periodic updates
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }

    private fun getAddress(lat: Double, lng: Double): String {
        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocation(lat, lng, 1)
        return list[0].getAddressLine(0)
    }



    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        // interval specifies the rate at which your app will like to receive updates.
        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
}