package com.example.cookit.ui.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookit.R
import com.example.cookit.data.database.RecipeDAO
import com.example.cookit.data.database.RecipeRoomDatabase
import com.example.cookit.data.model.Recipes
import com.example.cookit.data.network.ApiClient
import com.example.cookit.data.network.ApiService
import com.example.cookit.databinding.ActivityAdminHomePageBinding
import com.example.cookit.ui.detail.DetailActivity
import com.example.cookit.util.PrefManager
import com.example.cookit.util.RecipesAdapterAdmin
import com.example.cookit.util.UserOnClick
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AdminHomePage : AppCompatActivity(), UserOnClick {
    private lateinit var binding: ActivityAdminHomePageBinding
    private lateinit var recipesAdapterAdmin: RecipesAdapterAdmin
    private lateinit var recipeDao: RecipeDAO
    private lateinit var executorService: ExecutorService
    private lateinit var client: ApiService
    private val listRecipes = arrayListOf<Recipes>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAdminHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val prefManager = PrefManager.getInstance(this)

        binding.logoutBtn.setOnClickListener {
            prefManager.clear()
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        executorService = Executors.newSingleThreadExecutor()
        val db = RecipeRoomDatabase.getDatabase(this)
        recipeDao = db!!.RecipeDAO()!!

        client = ApiClient.instance

        recipesAdapterAdmin = RecipesAdapterAdmin(listRecipes, ArrayList(), this, client ) { recipes ->
            Toast.makeText(this, "Item clicked", Toast.LENGTH_SHORT).show()
            val intentToDetail = Intent(this, DetailActivity::class.java).apply {
                putExtra("id", recipes.id)
                putExtra("title", recipes.title)
                putExtra("author", recipes.author)
                putExtra("description", recipes.description)
                putExtra("ingredients", recipes.ingredients.toTypedArray())
                putExtra("steps", recipes.steps.toTypedArray())
                putExtra("image", recipes.image)
            }
            startActivity(intentToDetail)
        }

        binding.rvAdmin.layoutManager = LinearLayoutManager(this)
        binding.rvAdmin.adapter = recipesAdapterAdmin

        recipeDao.allRecipes.observe(this) { arrayBookmark ->
            recipesAdapterAdmin.updateData(ArrayList(arrayBookmark))
        }

        loadRecipes()

        binding.addBtn.setOnClickListener {
            val intentToAdd = Intent(this, CreateRecipes::class.java)
            startActivityForResult(intentToAdd, REQUEST_CODE_CREATE_RECIPE)
        }
    }

    private fun loadRecipes() {
        val response = client.getAllRecipes()

        response.enqueue(object : Callback<List<Recipes>> {
            override fun onResponse(call: Call<List<Recipes>>, response: Response<List<Recipes>>) {
                if (response.isSuccessful && response.body() != null) {
                    binding.progressBar.visibility = View.GONE
                    listRecipes.clear() // Clear the list before adding new data
                    response.body()?.forEach { i ->
                        val newRecipes = Recipes(
                            id = i.id,
                            title = i.title ?: "No Title",
                            author = i.author ?: "Unknown Author",
                            description = i.description ?: "",
                            image = i.image ?: "",
                            ingredients = i.ingredients ?: emptyList(),
                            steps = i.steps ?: emptyList()
                        )
                        Log.d("item", i.title.toString())
                        listRecipes.add(newRecipes)
                    }
                    recipesAdapterAdmin.notifyDataSetChanged()
                    Log.i("item list", listRecipes.size.toString())
                } else {
                    Log.e("API Error", "Response not successful or body is null")
                }
            }

            override fun onFailure(call: Call<List<Recipes>>, t: Throwable) {
                Log.e("cannot get API", t.message.toString())
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CREATE_RECIPE && resultCode == RESULT_OK) {
            loadRecipes() // Refresh the data
        }
    }

    override fun bookmark(recipes: Recipes) {
        // Handle bookmark action
    }

    companion object {
        private const val REQUEST_CODE_CREATE_RECIPE = 1
    }
}