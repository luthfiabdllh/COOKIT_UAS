package com.example.cookit.data.network

import com.example.cookit.data.model.Recipes
import com.example.cookit.data.model.User
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("table-users") // endpoint sesuai API Anda
    fun createUser(@Body user: User): Call<User>

    @GET("table-users") // endpoint sesuai API Anda
    fun getAllUsers(): Call<List<User>>

    @GET("table-recipess")
    fun getAllRecipes(): Call<List<Recipes>>

    @DELETE("table-recipess/{id}")
    fun deleteRecipes(@Path("id") recipeId: String): Call<Recipes>

    @POST("table-recipess") // endpoint sesuai API Anda
    fun createRecipes(@Body rawJson: RequestBody): Call<Recipes>


}