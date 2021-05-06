package com.example.trabalho_commov

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.trabalho_commov.api.EndPoints
import com.example.trabalho_commov.api.Note
import com.example.trabalho_commov.api.ServiceBuilder
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
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


        //Fab
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, AddPonto::class.java)
            intent.putExtra("LocLat", locAdd.latitude)
            intent.putExtra("LocLon", locAdd.longitude)
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


                if (ActivityCompat.checkSelfPermission(
                        this@Logged_In_Activity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this@Logged_In_Activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
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


                mMap.isMyLocationEnabled = true


                
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15.0f))

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



        carregar_pontos(null,null,null);



        val buttonfilter: Button = findViewById(R.id.Filter)
        buttonfilter.setOnClickListener {
            //Limpar pontos
            mMap.clear();
            //Carrgar um raio? de 50000
            carregar_pontos(50000,locAdd.latitude,locAdd.longitude);
        }
        val buttonAcidente: Button = findViewById(R.id.FilterAcidente)
        buttonAcidente.setOnClickListener {
            //Limpar pontos
            mMap.clear();

            val mystring = resources.getString(R.string.Acidente)
            carregarFiltro(mystring)
        }
        val buttonObras: Button = findViewById(R.id.Obras)
        buttonObras.setOnClickListener {
            //Limpar pontos

            mMap.clear();
            val mystring = resources.getString(R.string.Obras)
            carregarFiltro(mystring)
        }
        val buttonEstrada: Button = findViewById(R.id.Estrada)
        buttonEstrada.setOnClickListener {
            //Limpar pontos
            mMap.clear();
            val mystring = resources.getString(R.string.Estrada)
            carregarFiltro(mystring)
        }
        val buttonPerigo: Button = findViewById(R.id.Petigo)
        buttonPerigo.setOnClickListener {
            //Limpar pontos
            mMap.clear();
            val mystring = resources.getString(R.string.Perigo)
            carregarFiltro(mystring)
        }






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
        Log.d("Valor:", "Resume")
    }

    //Parar de receber Coordenadas ao pausar
    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Log.d("Valor:", "Pause")
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap



        mMap.setOnInfoWindowClickListener( object: GoogleMap.OnInfoWindowClickListener {
            override fun onInfoWindowClick(p0: Marker) {

                val intent = Intent(this@Logged_In_Activity, NotaDesc::class.java)
                //Buscar valor do ID do marker
                val id: Int
                val split = TextUtils.split( "${p0.tag}", "-")

                //Valor do ID
                id = split[0].toInt()
                intent.putExtra("idDoMarker",id)

                Log.d("VALOR", "${p0.tag}")

                startActivity(intent)
                finish()
            }
        })
    }



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

    fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lng1, lat2, lng2, results)
        // distance in meter
        return results[0]
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        // interval specifies the rate at which your app will like to receive updates.
        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }


    fun carregar_pontos(distance: Int?,lastlat: Double?,lastlng: Double?){
        preferences = getSharedPreferences("SharedLogin", Context.MODE_PRIVATE);
        val myIntValue: Int = preferences.getInt("ID_PESSOA", -1)

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
                        //val distanceOf = 100
                            if (distance != null && lastlat != null && lastlng != null) {
                                val distanceOf = calculateDistance(lastlat, lastlng,nota.lat.toDouble(),nota.lng.toDouble())
                                if (distanceOf < distance) {
                                    //icon dentro da distancia
                                    val marker = mMap.addMarker(
                                        MarkerOptions().position(position)
                                            .title((nota.titulo + " - " + nota.descricao)).icon(
                                                BitmapDescriptorFactory.defaultMarker(
                                                    BitmapDescriptorFactory.HUE_YELLOW
                                                )

                                            )
                                    )
                                    marker.tag = "${nota.id}-true"
                                }
                            }else{
                                val marker = mMap.addMarker(
                                    MarkerOptions().position(position)
                                        .title((nota.titulo + " - " + nota.descricao)).icon(
                                            BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_AZURE
                                            )


                                        )

                                )
                                marker.tag = "${nota.id}-true"

                            }



                        }
                        //Não pertence ao utilizador
                        else {
                            position = LatLng(
                                nota.lat.toString().toDouble(),
                                nota.lng.toString().toDouble()
                            )

                            if (distance != null && lastlat != null && lastlng != null) {
                                val distanceOf = calculateDistance(lastlat, lastlng,nota.lat.toDouble(),nota.lng.toDouble())
                                if (distanceOf < distance) {

                                    //Icons de outros quando existe distancia
                                    val marker = mMap.addMarker(
                                        MarkerOptions().position(position)
                                            .title((nota.titulo + " - " + nota.descricao)).icon(
                                                BitmapDescriptorFactory.defaultMarker(
                                                    BitmapDescriptorFactory.HUE_GREEN
                                                )
                                            )
                                    )
                                    marker.tag = "${nota.id}-true"


                                }
                            }else{
                                //Icones de outros caso nao exista distancia
                                val marker = mMap.addMarker(
                                    MarkerOptions().position(position)
                                        .title((nota.titulo + " - " + nota.descricao))
                                )
                                marker.tag = "${nota.id}-true"
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                Toast.makeText(this@Logged_In_Activity, "Error", Toast.LENGTH_SHORT)
                    .show()
            }


        })
    }



    fun carregarFiltro(tipo: String){
        preferences = getSharedPreferences("SharedLogin", Context.MODE_PRIVATE);
        val myIntValue: Int = preferences.getInt("ID_PESSOA", -1)

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

                            if(nota.tipo==(tipo)) {
                                //icon dentro da distancia
                                val marker = mMap.addMarker(
                                    MarkerOptions().position(position)
                                        .title((nota.titulo + " - " + nota.descricao)).icon(
                                            BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_YELLOW
                                            )

                                        )
                                )
                                marker.tag = "${nota.id}-true"
                            }



                        }
                        //Não pertence ao utilizador
                        else {
                            position = LatLng(
                                nota.lat.toString().toDouble(),
                                nota.lng.toString().toDouble()
                            )


                            if(nota.tipo==(tipo)) {
                                //Icons de outros quando existe distancia
                                val marker = mMap.addMarker(
                                    MarkerOptions().position(position)
                                        .title((nota.titulo + " - " + nota.descricao)).icon(
                                            BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_GREEN
                                            )
                                        )
                                )
                                marker.tag = "${nota.id}-true"
                            }

                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                Toast.makeText(this@Logged_In_Activity, "Error", Toast.LENGTH_SHORT)
                    .show()
            }


        })
    }

}