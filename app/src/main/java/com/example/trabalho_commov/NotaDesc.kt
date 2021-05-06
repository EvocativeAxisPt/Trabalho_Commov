package com.example.trabalho_commov

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.trabalho_commov.api.EndPoints
import com.example.trabalho_commov.api.Note
import com.example.trabalho_commov.api.ServiceBuilder
import com.google.android.gms.maps.model.LatLng
import retrofit2.*

class NotaDesc : AppCompatActivity() {
    //Shared Preferences
    lateinit var preferences: SharedPreferences

    private lateinit var Titulo: TextView
    private lateinit var Tipo: TextView
    private lateinit var Descricao: TextView
    //private lateinit var imagem: ImageView

    private var iddoUserMarker = 0
    private var IdMarker = 0
    private lateinit var TituloValor: String
    private lateinit var DescValor: String
    private lateinit var TipoValor: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nota_desc)


        Titulo = findViewById<TextView>(R.id.titulo)
        Descricao = findViewById<TextView>(R.id.descicao)
        Tipo = findViewById<TextView>(R.id.tipo)
        //

        IdMarker = intent.getIntExtra("idDoMarker", 0)

        if(IdMarker == 0){
            IdMarker = intent.getIntExtra("idDoMarkerDoEdit", 0)
        }

        carregar_pontos(IdMarker)



        val button = findViewById<Button>(R.id.buttonedit)
        button.setOnClickListener{
            preferences = getSharedPreferences("SharedLogin", Context.MODE_PRIVATE);
            val idPref = preferences.getInt("ID_PESSOA", -1)

            if(idPref.equals(iddoUserMarker)){
                val intent = Intent(this@NotaDesc, Edit_Ponto::class.java)
                intent.putExtra("idDoMarkerEdit", IdMarker)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this@NotaDesc, R.string.cantedit, Toast.LENGTH_SHORT).show()
            }
        }

        val button1 = findViewById<Button>(R.id.buttonback)
        button1.setOnClickListener{

            val intent = Intent(this@NotaDesc, Logged_In_Activity::class.java)
            startActivity(intent)
            finish()
        }



        val buttondelete: Button = findViewById(R.id.buttondelte)

        buttondelete.setOnClickListener {
            preferences = getSharedPreferences("SharedLogin", Context.MODE_PRIVATE);
            val idPref = preferences.getInt("ID_PESSOA", -1)

            if(idPref.equals(iddoUserMarker)){
                deletenota(IdMarker)
            }
            else{
                Toast.makeText(this@NotaDesc, R.string.cantdelete, Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun carregar_pontos(id: Int?) {

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getNota(id)

        var position: LatLng

        call.enqueue(object : Callback<Note> {

            override fun onResponse(call: Call<Note>, response: Response<Note>) {

                if (response.isSuccessful){
                    val n: Note = response.body()!!

                    Titulo.setText(n.titulo)
                    Tipo.setText(n.tipo)
                    Descricao.setText(n.descricao)

                    TituloValor = n.titulo
                    DescValor = n.descricao
                    TipoValor = n.tipo

                    iddoUserMarker = n.id_pessoa

                }else{
                    Toast.makeText(this@NotaDesc, "${response}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Note>, t: Throwable) {
                Toast.makeText(this@NotaDesc, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun deletenota(id: Int?) {

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.deletenota(id)

        call.enqueue(object : Callback<Note> {

            override fun onResponse(call: Call<Note>, response: Response<Note>) {

                Toast.makeText(this@NotaDesc, R.string.deletenote, Toast.LENGTH_SHORT).show()

                val intent = Intent(this@NotaDesc, Logged_In_Activity::class.java)
                startActivity(intent)
                finish()

            }

            override fun onFailure(call: Call<Note>, t: Throwable) {
                Toast.makeText(this@NotaDesc, R.string.exists, Toast.LENGTH_SHORT).show()
                val intent = Intent(this@NotaDesc, Logged_In_Activity::class.java)
                startActivity(intent)
                finish()
                //Toast.makeText(this@marker_desc, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}