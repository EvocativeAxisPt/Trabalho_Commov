package com.example.trabalho_commov.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.trabalho_commov.entities.Nota

@Dao
interface NotasDao {

    @Query("SELECT * from nota_table ORDER BY titulo ASC")
    fun getAllNotas(): LiveData<List<Nota>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(nota: Nota)

    @Update
    suspend fun updateNota(nota: Nota)

    @Query("DELETE FROM nota_table")
    suspend fun deleteAll()

   @Delete
   fun deleteNota(nota: Nota?)

}