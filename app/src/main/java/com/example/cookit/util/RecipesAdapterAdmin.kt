package com.example.cookit.util

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cookit.OnClickBook
import com.example.cookit.R
import com.example.cookit.data.model.Recipes
import com.example.cookit.data.network.ApiService
import com.example.cookit.databinding.RecipesItemAdminBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipesAdapterAdmin(
    private val listRecipes: ArrayList<Recipes>,
    private val listBookmarkedRecipes: ArrayList<Recipes>,
    private val userOnClick: UserOnClick,
    private val client: ApiService,
    private val onClickBook: OnClickBook
) : RecyclerView.Adapter<RecipesAdapterAdmin.ItemBookViewHolder>() {

    inner class ItemBookViewHolder(private val binding: RecipesItemAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Recipes) {
            with(binding) {
                itemTitle.text = data.title
                itemAuthor.text = data.author
                itemDesciptions.text = data.description

                Picasso.get()
                    .load(data.image)
                    .placeholder(R.drawable.baseline_error_24) // Placeholder image while loading
                    .error(R.drawable.testimg) // Error image if loading fails
                    .into(binding.itemImages)

                itemView.setOnClickListener {
                    onClickBook(data)
                }
                ivDelete.setOnClickListener {
                    AlertDialog.Builder(itemView.context).apply {
                        setTitle("Delete Confirmation")
                        setMessage("Are you sure you want to delete ${data.title}?")
                        setPositiveButton("Yes") { _, _ ->
                            val response = data.id?.let { it1 -> client.deleteRecipes(it1) }

                            if (response != null) {
                                response.enqueue(object : Callback<Recipes> {
                                    override fun onResponse(call: Call<Recipes>, response: Response<Recipes>) {
                                        if (response.isSuccessful) {
                                            Toast.makeText(
                                                itemView.context,
                                                "data buku ${data.title} berhasil di hapus",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            val position = adapterPosition
                                            if (position != RecyclerView.NO_POSITION) {
                                                removeItem(position)
                                            }
                                        } else {
                                            Log.e("API Error", "Response not successful or body is null")
                                        }
                                    }

                                    override fun onFailure(call: Call<Recipes>, t: Throwable) {
                                        Toast.makeText(itemView.context, "Koneksi error", Toast.LENGTH_LONG)
                                            .show()
                                    }
                                })
                            }
                        }
                        setNegativeButton("No", null)
                    }.show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemBookViewHolder {
        val binding = RecipesItemAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    fun removeItem(position: Int) {
        listRecipes.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, listRecipes.size)
    }
}