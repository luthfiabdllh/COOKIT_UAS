package com.example.cookit.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.cookit.ui.user.MainActivity
import com.example.cookit.R
import com.example.cookit.databinding.ActivityDetailBinding
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        with(binding) {
            val image = intent.getStringExtra("image")
            val title = intent.getStringExtra("title")
            val description = intent.getStringExtra("description")
            val author = intent.getStringExtra("author")
            val ingredients = intent.getStringArrayExtra("ingredients")
            val steps = intent.getStringArrayExtra("steps")

            itemTitle.text = title
            itemDescription.text = description
            itemAuthor.text = author

            Picasso.get()
                .load(image)
                .placeholder(R.drawable.baseline_error_24)
                .error(R.drawable.testimg)
                .into(itemImages)

            itemIngredient.text = ingredients?.joinToString(separator = "\n") { "- $it" }
            itemStep.text = steps?.mapIndexed { index, step -> "${index + 1}. $step" }
                ?.joinToString(separator = "\n")

            btBack.setOnClickListener {
                finish()
            }
        }
    }
}