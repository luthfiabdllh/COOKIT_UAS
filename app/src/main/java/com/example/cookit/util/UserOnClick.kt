package com.example.cookit.util

import com.example.cookit.data.model.Recipes

interface UserOnClick {
    fun bookmark(recipes: Recipes);
}