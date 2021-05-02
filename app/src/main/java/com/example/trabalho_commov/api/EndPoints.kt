package com.example.trabalho_commov.api

import retrofit2.http.*
import retrofit2.Call

interface EndPoints {
    @GET("pessoas")
    fun getPessoas(): Call<List<User>>

    @FormUrlEncoded
    @POST("pessoas/post")
    fun getpessoa(@Field("username") username: String?): Call<User>

    @GET("notasdetalhes")
    fun getNotas(): Call<List<Note>>
}