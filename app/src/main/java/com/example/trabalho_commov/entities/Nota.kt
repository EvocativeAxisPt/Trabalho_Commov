package com.example.trabalho_commov.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_table")

class Nota(
    // Int? = null so when creating instance id has not to be passed as argument
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "country") val country: String
)