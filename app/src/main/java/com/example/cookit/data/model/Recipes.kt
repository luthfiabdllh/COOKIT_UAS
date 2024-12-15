package com.example.cookit.data.model

import com.google.gson.annotations.SerializedName
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes_table")
data class Recipes(
    @PrimaryKey
    @SerializedName("_id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("ingredients")
    val ingredients: List<String>,
    @SerializedName("steps")
    val steps: List<String>,
)
