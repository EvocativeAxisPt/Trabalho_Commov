package com.example.trabalho_commov.api

data class User(
        val id_pessoa: Int,
        val nome: String,
        val username: String,
        val password: String
)

data class Note(
        val id: Int,
        val titulo: String,
        val descricao: String,
        val id_pessoa: Int,
        val lat: String,
        var lng: String,
        var tipo: String
)