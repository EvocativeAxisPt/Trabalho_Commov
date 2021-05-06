package com.example.trabalho_commov

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.trabalho_commov.api.EndPoints
import com.example.trabalho_commov.api.Note
import com.example.trabalho_commov.api.ServiceBuilder
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Edit_Ponto : AppCompatActivity() {
    private lateinit var pontoTit: EditText
    private lateinit var pontoDesc: EditText
    private lateinit var pontoTipo: EditText

    private var IdMarker = 0

    //Shared Preferences
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_nota)



        pontoTit = findViewById(R.id.titulo)
        pontoDesc = findViewById(R.id.descicao)
        pontoTipo = findViewById(R.id.tipo)

        IdMarker = intent.getIntExtra("idDoMarkerEdit", -1)

        carregar_pontos(IdMarker)



        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
           editPonto(IdMarker)
        }
    }


    fun carregar_pontos(id: Int?) {

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getNota(id)

        call.enqueue(object : Callback<Note> {

            override fun onResponse(call: Call<Note>, response: Response<Note>) {

                if (response.isSuccessful) {
                    val n: Note = response.body()!!
                    pontoTit.setText(n.titulo)
                    pontoTipo.setText(n.tipo)
                    pontoDesc.setText(n.descricao)

                } else {
                    Toast.makeText(this@Edit_Ponto, "${response}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Note>, t: Throwable) {
                Toast.makeText(this@Edit_Ponto, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun editPonto(idPonto: Int) {

        //Gets the values inserted in the EditText Inputs
        val tit = pontoTit.text.toString()
        val desc = pontoDesc.text.toString()
        val tipo = pontoTipo.text.toString()

        if (tit.isBlank() || desc.isBlank()) {
            Toast.makeText(this, getResources().getString(R.string.Preencher), Toast.LENGTH_SHORT)
                .show()
        } else {
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.editPonto(idPonto, tit, desc, tipo)

            call.enqueue(object : Callback<Note> {

                override fun onResponse(call: Call<Note>, response: Response<Note>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@Edit_Ponto,
                            getResources().getString(R.string.alterado),
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@Edit_Ponto, NotaDesc::class.java)
                        intent.putExtra("idDoMarkerDoEdit", IdMarker)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@Edit_Ponto,
                           // getResources().getString(R.string.erroEditar),
                            "${response}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Note>, t: Throwable) {
                    val intent = Intent(this@Edit_Ponto, NotaDesc::class.java)
                    intent.putExtra("idDoMarkerDoEdit", IdMarker)
                    startActivity(intent)
                    finish()
                    //Toast.makeText(this@edit_Ponto, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}