package com.example.trabalho_commov

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.trabalho_commov.api.EndPoints
import com.example.trabalho_commov.api.Note
import com.example.trabalho_commov.api.ServiceBuilder
import kotlin.math.log
import retrofit2.*

class AddPonto : AppCompatActivity() {
    private lateinit var tituloText: EditText
    private lateinit var descricaoText: EditText
    private lateinit var spinner: Spinner

    lateinit var preferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ponto)

        tituloText = findViewById(R.id.titulo)
        descricaoText = findViewById(R.id.descicao)

        val button = findViewById<Button>(R.id.button_save)
        val languages = resources.getStringArray(R.array.planets_array)
        //Spinner para seleção de dados
        val spinner: Spinner = findViewById(R.id.spinner)


        var LocLat : Double
        var LocLon : Double

        LocLat = intent.getDoubleExtra("LocLat",0.0)
        LocLon = intent.getDoubleExtra("LocLon",0.0)

        preferences = getSharedPreferences("SharedLogin", Context.MODE_PRIVATE);
        val myIntValue: Int = preferences.getInt("ID_PESSOA", -1)




        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, languages)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    Toast.makeText(this@AddPonto,
                        getString(R.string.selected_item) + " " +
                                "" + languages[position], Toast.LENGTH_SHORT).show()

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        button.setOnClickListener {
            inserePonto(LocLat,LocLon,myIntValue,spinner.selectedItem.toString())
        }




    }
    private fun inserePonto(LocLat:Double, LocLon:Double,myIntValue: Int ,currentchose: String) {

        val spinner: Spinner = findViewById(R.id.spinner)
        //Gets the values inserted in the EditText Inputs
        val tit = tituloText.text.toString()
        val desc = descricaoText.text.toString()


        if (tit.isBlank() || desc.isBlank()) {
            Toast.makeText(
                this,
                getResources().getString(R.string.Preencher),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.inserirPonto(
                tit,
                desc,
                myIntValue,
                LocLat.toString(),
                LocLon.toString(),
                currentchose

            )

            call.enqueue(object : Callback<Note> {

                override fun onResponse(call: Call<Note>, response: Response<Note>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@AddPonto,
                            getResources().getString(R.string.inserido),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                     /*   Toast.makeText(
                            this@AddPonto,
                            "${tit} - ${desc} - ${myIntValue} - ${LocLat} - ${LocLon} - ${currentchose}",
                            Toast.LENGTH_SHORT
                        ).show()
    */

                        Toast.makeText(
                            this@AddPonto,
                            "${response}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Note>, t: Throwable) {
                    Toast.makeText(this@AddPonto, "${t.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }
    }

}
