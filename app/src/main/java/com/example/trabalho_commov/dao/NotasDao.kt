package com.example.trabalho_commov.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.trabalho_commov.entities.Nota

@Dao
interface NotasDao {

    @Query("SELECT * from city_table ORDER BY city ASC")
    fun getAllCities(): LiveData<List<Nota>>

    @Query("SELECT * FROM city_table WHERE country == :country")
    fun getCitiesByCountry(country: String): LiveData<List<Nota>>

    @Query("SELECT * FROM city_table WHERE city == :city")
    fun getCountryFromCity(city: String): LiveData<Nota>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(nota: Nota)

    @Update
    suspend fun updateCity(nota: Nota)

    @Query("DELETE FROM city_table")
    suspend fun deleteAll()

    @Query("DELETE FROM city_table where city == :city")
    suspend fun deleteByCity(city: String)

    @Query("UPDATE city_table SET country=:country WHERE city == :city")
    suspend fun updateCountryFromCity(city: String, country: String)
}