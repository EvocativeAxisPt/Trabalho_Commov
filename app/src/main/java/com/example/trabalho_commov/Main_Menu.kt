package ipvc.estg.room

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trabalho_commov.ALLNotas
import com.example.trabalho_commov.Logged_In_Activity
import com.example.trabalho_commov.MainActivity
import com.example.trabalho_commov.R
import com.example.trabalho_commov.api.EndPoints
import com.example.trabalho_commov.api.Note
import com.example.trabalho_commov.api.ServiceBuilder
import com.example.trabalho_commov.api.User
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main__menu.*
import retrofit2.*

class Main_Menu : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var notas: List<Note>
    lateinit var preferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main__menu)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



        preferences = getSharedPreferences("SharedLogin", Context.MODE_PRIVATE);
        val userPref = preferences.getString("USERNAME", null)
        val passPref = preferences.getString("PASSWORD", null)



        val buttonall: Button = findViewById(R.id.button)
        val button: Button = findViewById(R.id.button2)
        val buttonLogin: Button = findViewById(R.id.buttonLogin)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getNotas()
        var position: LatLng

        call.enqueue(object : Callback<List<Note>> {

            override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>) {
                if(response.isSuccessful){
                    notas= response.body()!!
                    for(nota in notas){
                        position= LatLng(nota.lat.toString().toDouble(),nota.lng.toString().toDouble())
                        mMap.addMarker(MarkerOptions().position(position).title((nota.titulo + " - " + nota.descricao)))
                    }
                }
            }

            override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                Toast.makeText(this@Main_Menu,"${t.message}", Toast.LENGTH_SHORT).show()
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


        if (userPref.isNullOrBlank() || passPref.isNullOrBlank()) {

            //Abrir Login
            buttonLogin.setOnClickListener {

                var username = findViewById<EditText>(R.id.editTextTextPersonName)
                var password = findViewById<EditText>(R.id.editTextTextPassword)

                val usernameStr = username.text.toString()
                val passwordStr = password.text.toString()

                if(usernameStr.isNullOrBlank() || passwordStr.isNullOrBlank()){
                    Toast.makeText(this@Main_Menu, getResources().getString(R.string.missingdata), Toast.LENGTH_SHORT).show()
                }
                else{
                    Login(usernameStr, passwordStr)
                }
            }
        }

        else {

            val intent = Intent(this, Logged_In_Activity::class.java)
            startActivity(intent)
            finish()

        }
    }








    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


    }

    fun Login(user: String, pass: String) {

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getpessoa(user)

        val intent = Intent(this, Logged_In_Activity::class.java)

        call.enqueue(object : Callback<User> {

            override fun onResponse(call: Call<User>, response: Response<User>) {

                if (response.isSuccessful){


                    val u: User = response.body()!!

                    if(u.password.equals(pass)) {

                        val editor: SharedPreferences.Editor = preferences.edit()

                        editor.putString("USERNAME", user)
                        editor.putString("PASSWORD", pass)
                        editor.putInt("ID_PESSOA", u.id_pessoa)
                        editor.apply()

                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this@Main_Menu, getResources().getString(R.string.passworderrada), Toast.LENGTH_SHORT).show()
                    }

                }else{
                   Toast.makeText(this@Main_Menu,getResources().getString(R.string.credenciais), Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@Main_Menu, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })






    }
}