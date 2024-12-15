package com.example.cookit

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cookit.data.model.Recipes
import com.example.cookit.databinding.RecipesItemBinding
import com.squareup.picasso.Picasso

typealias OnClickBook = (Recipes) -> Unit

