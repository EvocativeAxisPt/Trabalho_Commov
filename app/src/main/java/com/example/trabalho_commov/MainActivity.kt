package com.example.trabalho_commov

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho_commov.adapters.NotasAdapter
import com.example.trabalho_commov.entities.Nota
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ipvc.estg.room.viewModel.NotaViewModel

class MainActivity : AppCompatActivity(),NotasAdapter.RowClickListener {

    private lateinit var notaViewModel: NotaViewModel
    private val newWordActivityRequestCode = 1
    private val INTENT_REQUEST_EDIT_NOTE = 2
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NotasAdapter(this,this@MainActivity)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // view model
        notaViewModel = ViewModelProvider(this).get(NotaViewModel::class.java)
        notaViewModel.allnotas.observe(this, Observer { notas ->
            // Update the cached copy of the words in the adapter.
            notas?.let { adapter.setNotas(it) }
        })

        //Fab
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddNota::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val ptitulo = data?.getStringExtra(AddNota.EXTRA_REPLY_TITULO)
            val pdescricao = data?.getStringExtra(AddNota.EXTRA_REPLY_DESCRICAO)

            if (ptitulo!= null && pdescricao != null) {
                val nota = Nota(
                    titulo = ptitulo,
                    descricao = pdescricao
                )
                notaViewModel.insert(nota)
            }

        }

        if (requestCode == INTENT_REQUEST_EDIT_NOTE && resultCode == RESULT_OK && data != null) {
            val id = data.getIntExtra(EditNota.EXTRA_ID, -1)
            if(id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show()
                return
            }

            val title = data.getStringExtra(EditNota.EXTRA_REPLY_TITULO) ?: ""
            val description = data.getStringExtra(EditNota.EXTRA_REPLY_DESCRICAO) ?: ""

            val nota = Nota(
                    id = id,
                    titulo = title,
                    descricao = description
            )
            notaViewModel.updateNota(nota)

            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show()
            }

        else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.apagartudo -> {
                notaViewModel.deleteAll()
                true
            }



            R.id.todasCidades -> {

                // recycler view
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
                val adapter =
                    NotasAdapter(this,this@MainActivity)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)

                // view model
                notaViewModel = ViewModelProvider(this).get(NotaViewModel::class.java)
                notaViewModel.allnotas.observe(this, Observer { notas ->
                    // Update the cached copy of the words in the adapter.
                    notas?.let { adapter.setNotas(it) }
                })


                true
            }


            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDeleteNotaClickListener(nota: Nota) {
        //Toast.makeText(this, nota.titulo, Toast.LENGTH_SHORT).show()
        //todo adicionar verificação se quer apagar
        notaViewModel.deleteNota(nota)
    }

    override fun onItemClick(nota: Nota) {
      //  Toast.makeText(this, nota.titulo, Toast.LENGTH_SHORT).show()
        val intent = Intent(this@MainActivity, EditNota::class.java)
        intent.putExtra(EditNota.EXTRA_ID, nota.id)
        intent.putExtra(EditNota.EXTRA_REPLY_TITULO, nota.titulo)
        intent.putExtra(EditNota.EXTRA_REPLY_DESCRICAO, nota.descricao)
       startActivityForResult(intent, INTENT_REQUEST_EDIT_NOTE)


    }


}