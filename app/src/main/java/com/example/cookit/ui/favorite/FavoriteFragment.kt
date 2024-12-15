package com.example.cookit.ui.favorite

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookit.ui.detail.DetailActivity
import com.example.cookit.util.RecipesAdapter
import com.example.cookit.util.UserOnClick
import com.example.cookit.data.database.RecipeDAO
import com.example.cookit.data.database.RecipeRoomDatabase
import com.example.cookit.data.model.Recipes
import com.example.cookit.databinding.FragmentFavoriteBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteFragment : Fragment(), UserOnClick {

    private var _binding: FragmentFavoriteBinding? = null
    private lateinit var recipeDao: RecipeDAO
    private lateinit var executorService: ExecutorService

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        executorService = Executors.newSingleThreadExecutor()
        val db = RecipeRoomDatabase.getDatabase(requireContext())
        recipeDao = db!!.RecipeDAO()!!

        // Set up the RecyclerView with a layout manager and an empty adapter
        binding.rvFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorites.adapter = RecipesAdapter(ArrayList(), ArrayList(), this) { recipes ->
            val intentToDetail = Intent(requireContext(), DetailActivity::class.java)
            intentToDetail.putExtra("id", recipes.id)
            intentToDetail.putExtra("title", recipes.title)
            intentToDetail.putExtra("author", recipes.author)
            intentToDetail.putExtra("ingredients", recipes.ingredients.toTypedArray())
            intentToDetail.putExtra("steps", recipes.steps.toTypedArray())
            intentToDetail.putExtra("image", recipes.image)
            startActivity(intentToDetail)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllRecipe()
    }

    private fun getAllRecipe() {
        recipeDao.allRecipes.observe(viewLifecycleOwner) { recipes ->
            val adapter = RecipesAdapter(
                listRecipes = ArrayList(recipes), ArrayList(recipes), this) { recipe ->
                val intentToDetail = Intent(requireContext(), DetailActivity::class.java)
                intentToDetail.putExtra("id", recipe.id)
                intentToDetail.putExtra("title", recipe.title)
                intentToDetail.putExtra("author", recipe.author)
                intentToDetail.putExtra("description", recipe.description)
                intentToDetail.putExtra("ingredients", recipe.ingredients.toTypedArray())
                intentToDetail.putExtra("steps", recipe.steps.toTypedArray())
                intentToDetail.putExtra("image", recipe.image)
                startActivity(intentToDetail)
            }
            binding.rvFavorites.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun bookmark(recipes: Recipes) {
        executorService.execute { recipeDao.delete(recipes) }
    }
}