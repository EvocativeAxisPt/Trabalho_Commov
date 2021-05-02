package com.example.trabalho_commov

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trabalho_commov.api.EndPoints
import com.example.trabalho_commov.api.Note
import com.example.trabalho_commov.api.ServiceBuilder
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged__in_)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        preferences = getSharedPreferences("SharedLogin", Context.MODE_PRIVATE);
        val myIntValue: Int = preferences.getInt("ID_PESSOA", -1)

        val buttonall: Button = findViewById(R.id.button)
        val button: Button = findViewById(R.id.button2)
        val buttonOut: Button = findViewById(R.id.buttonSIGNOUT)


        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getNotas()
        var position: LatLng

        call.enqueue(object : Callback<List<Note>> {

            override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>) {
             if(response.isSuccessful){
                 notas= response.body()!!
                 for(nota in notas){
                     if(nota.id_pessoa==(myIntValue)){
                         position= LatLng(nota.lat.toString().toDouble(),nota.lng.toString().toDouble())
                         mMap.addMarker(MarkerOptions().position(position).title((nota.titulo + " - " + nota.descricao)).icon(
                             BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                     }else{
                     position= LatLng(nota.lat.toString().toDouble(),nota.lng.toString().toDouble())
                     mMap.addMarker(MarkerOptions().position(position).title((nota.titulo + " - " + nota.descricao )))}
                 }
             }
            }

            override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                Toast.makeText(this@Logged_In_Activity,"Credencias Incorretos", Toast.LENGTH_SHORT).show()
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


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}