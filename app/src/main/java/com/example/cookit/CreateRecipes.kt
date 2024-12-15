package com.example.cookit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cookit.data.model.Recipes
import com.example.cookit.data.network.ApiClient
import com.example.cookit.data.network.ApiService
import com.example.cookit.databinding.ActivityCreateRecipesBinding
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateRecipes : AppCompatActivity() {
    private lateinit var binding: ActivityCreateRecipesBinding
    private lateinit var client: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateRecipesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        client = ApiClient.instance

        binding.btnCreate.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val author = binding.etAuthor.text.toString()
            val image = binding.etImage.text.toString()
            val description = binding.etDescription.text.toString()
            val ingredients = binding.etIngredients.text.toString().split(",").map { it.trim() }
            val steps = binding.etSteps.text.toString().split(",").map { it.trim() }

            val jsonData = Gson().toJson(
                mapOf(
                    "title" to title,
                    "author" to author,
                    "image" to image,
                    "description" to description,
                    "ingredients" to ingredients,
                    "steps" to steps
                )
            )

            val requestBody = jsonData.toRequestBody("application/json".toMediaTypeOrNull())

            val client = ApiClient.instance
            val response = client.createRecipes(requestBody)

            response.enqueue(object : Callback<Recipes> {
                override fun onResponse(call: Call<Recipes>, response: Response<Recipes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CreateRecipes, "berhasil menambah data", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else {
                        Log.e("API Error", "Response not successful or body is null")
                    }
                }
                override fun onFailure(call: Call<Recipes>, t: Throwable) {
                    Toast.makeText(this@CreateRecipes, "Koneksi error", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}