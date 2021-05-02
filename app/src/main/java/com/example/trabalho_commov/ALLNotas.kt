package com.example.trabalho_commov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho_commov.adapters.WebNotasAdapter
import com.example.trabalho_commov.api.EndPoints
import com.example.trabalho_commov.api.Note
import com.example.trabalho_commov.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Response

class ALLNotas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a_l_l_notas)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getNotas()

        call.enqueue(object : retrofit2.Callback<List<Note>>{

            override fun onFailure(call: Call<List<Note>>, t: Throwable) {
               Toast.makeText(this@ALLNotas,"${t.message}",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>) {
               recyclerView.apply{
                   setHasFixedSize(true)
                   layoutManager = LinearLayoutManager(this@ALLNotas)
                   adapter = WebNotasAdapter(response.body()!!)

               }
            }

        })
    }
}