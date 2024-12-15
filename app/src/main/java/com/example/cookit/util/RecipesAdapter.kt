package com.example.cookit.util

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cookit.OnClickBook
import com.example.cookit.R
import com.example.cookit.data.model.Recipes
import com.example.cookit.databinding.RecipesItemBinding
import com.squareup.picasso.Picasso

class RecipesAdapter(
    private val listRecipes: ArrayList<Recipes>,
    private val listBookmarkedRecipes : ArrayList<Recipes>,
    private val userOnClick: UserOnClick,
    private val onClickBook: OnClickBook
) :

    RecyclerView.Adapter<RecipesAdapter.ItemBookViewHolder>() {
    inner class ItemBookViewHolder(private val binding: RecipesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Recipes) {
            with(binding) {
                itemTitle.text = data.title
                itemAuthor.text = data.author
                itemDesciptions.text = data.description

                Picasso.get()
                    .load(data.image)
                    .placeholder(R.drawable.baseline_error_24) // Gambar sementara saat loading
                    .error(R.drawable.testimg)             // Gambar error jika gagal memuat
                    .into(binding.itemImages)


                if(listBookmarkedRecipes.contains(data)){
                    ivBookmark.setBackgroundResource(R.drawable.baseline_favorite_24)
                }else{
                    ivBookmark.setBackgroundResource(R.drawable.baseline_favorite_border_24)
                }
                ivBookmark.setOnClickListener {
                    userOnClick.bookmark(data)
                }
                itemView.setOnClickListener{
                    onClickBook(data)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ItemBookViewHolder {
        val binding =
            RecipesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemBookViewHolder(binding)
    }

    override fun getItemCount(): Int = listRecipes.size

    override fun onBindViewHolder(holder: ItemBookViewHolder, position: Int) {
        holder.bind(listRecipes[position])
        Log.i("data", listRecipes[position].title)
    }

    fun updateData(newData: ArrayList<Recipes>) {
        listBookmarkedRecipes.clear()
        listBookmarkedRecipes.addAll(newData)
        notifyDataSetChanged()
    }

}