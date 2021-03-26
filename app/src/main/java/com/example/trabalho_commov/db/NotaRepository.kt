package com.example.trabalho_commov.db

import androidx.lifecycle.LiveData
import com.example.trabalho_commov.entities.Nota
import com.example.trabalho_commov.dao.NotasDao

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class NotaRepository(private val notasDao: NotasDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allNotas: LiveData<List<Nota>> = notasDao.getAllNotas()

    suspend fun insert(nota: Nota) {
        notasDao.insert(nota)
    }

    suspend fun deleteAll(){
        notasDao.deleteAll()
    }


    suspend fun updateNota(nota: Nota) {
        notasDao.updateNota(nota)
    }

    suspend fun deleteNota(nota: Nota) {
        notasDao.deleteNota(nota)
    }
}