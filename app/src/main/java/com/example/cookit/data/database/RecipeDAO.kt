package com.example.cookit.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cookit.data.model.Recipes

@Dao
interface RecipeDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(recipe: Recipes)

    @Update
    fun update(recipe: Recipes)

    @Delete
    fun delete(recipe: Recipes)

    @get:Query("SELECT * FROM recipes_table")
    val allRecipes: LiveData<List<Recipes>>
}