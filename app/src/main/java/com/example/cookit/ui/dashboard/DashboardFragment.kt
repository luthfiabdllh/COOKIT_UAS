package com.example.cookit.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookit.ui.detail.DetailActivity
import com.example.cookit.util.RecipesAdapter
import com.example.cookit.util.UserOnClick
import com.example.cookit.data.database.RecipeDAO
import com.example.cookit.data.database.RecipeRoomDatabase
import com.example.cookit.data.model.Recipes
import com.example.cookit.data.network.ApiClient
import com.example.cookit.data.network.ApiService
import com.example.cookit.databinding.FragmentDashboardBinding
import com.example.cookit.util.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DashboardFragment : Fragment(), UserOnClick {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var recipesAdapter: RecipesAdapter
    private lateinit var recipeDao: RecipeDAO
    private lateinit var executorService: ExecutorService
    private lateinit var client: ApiService
    private var recipesList: ArrayList<Recipes> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val prefManager = PrefManager.getInstance(requireContext())
        val username = prefManager.getUsername()

        binding.txtUsername.text = "Hi!, $username"

        executorService = Executors.newSingleThreadExecutor()
        val db = RecipeRoomDatabase.getDatabase(requireContext())
        recipeDao = db!!.RecipeDAO()!!

        recipesAdapter = RecipesAdapter(recipesList, ArrayList(), this) { recipes ->
            Toast.makeText(requireContext(), "pencett", Toast.LENGTH_SHORT).show()
            val intentToDetail = Intent(requireContext(), DetailActivity::class.java)
            intentToDetail.putExtra("id", recipes.id)
            intentToDetail.putExtra("title", recipes.title)
            intentToDetail.putExtra("author", recipes.author)
            intentToDetail.putExtra("description", recipes.description)
            intentToDetail.putExtra("ingredients", recipes.ingredients.toTypedArray())
            intentToDetail.putExtra("steps", recipes.steps.toTypedArray())
            intentToDetail.putExtra("image", recipes.image)
            startActivity(intentToDetail)
        }

        binding.rvRecipes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecipes.adapter = recipesAdapter

        recipeDao.allRecipes.observe(viewLifecycleOwner) { arrayBookmark ->
            recipesAdapter.updateData(ArrayList(arrayBookmark))
        }

        client = ApiClient.instance
        val response = client.getAllRecipes()

        response.enqueue(object : Callback<List<Recipes>> {
            override fun onResponse(call: Call<List<Recipes>>, response: Response<List<Recipes>>) {
                if (response.isSuccessful && response.body() != null) {
                    binding.progressBar.visibility = View.GONE
                    recipesList.clear() // Clear the list before adding new data
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
                        recipesList.add(newRecipes)
                    }
                    recipesAdapter.notifyDataSetChanged()
                    Log.i("item list", recipesList.size.toString())
                } else {
                    Log.e("API Error", "Response not successful or body is null")
                }
            }

            override fun onFailure(call: Call<List<Recipes>>, t: Throwable) {
                Log.e("cannot get API", t.message.toString())
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun bookmark(recipes: Recipes) {
        executorService.execute { recipeDao.insert(recipes) }
        Toast.makeText(requireContext(), "Bookmarked", Toast.LENGTH_SHORT).show()
        val intent = Intent("com.example.cookit.BOOKMARK_ACTION")
        intent.putExtra("recipe_title", "Test Recipe")
        context?.sendBroadcast(intent)
    }
}