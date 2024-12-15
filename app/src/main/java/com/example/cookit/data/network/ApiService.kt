package com.example.cookit.data.network

import com.example.cookit.data.model.Recipes
import com.example.cookit.data.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("table-users") // endpoint sesuai API Anda
    fun createUser(@Body user: User): Call<User>

    @GET("table-users") // endpoint sesuai API Anda
    fun getAllUsers(): Call<List<User>>

    @GET("table-recipess")
    fun getAllRecipes(): Call<List<Recipes>>


}