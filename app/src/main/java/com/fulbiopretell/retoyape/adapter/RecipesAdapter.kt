package com.fulbiopretell.retoyape.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fulbiopretell.retoyape.core.extensions.loadUrl
import com.fulbiopretell.retoyape.databinding.ItemRepiceBinding
import com.fulbiopretell.retoyape.model.Recipe

class RecipesAdapter(
    val onClick: (Recipe) -> Unit
) : ListAdapter<Recipe, RecipesAdapter.ViewHolder>(RecipeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemRepiceBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.ivImage.loadUrl(item.image)
        holder.tvName.text = item.name
        holder.tvDescription.text = item.description

        holder.clContainer.setOnClickListener {
            onClick(item)
        }
    }

    inner class ViewHolder(binding: ItemRepiceBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivImage: ImageView = binding.ivImage
        val tvName: TextView = binding.tvName
        val tvDescription: TextView = binding.tvDescription
        val clContainer: ConstraintLayout = binding.clContainer
    }

    class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) = oldItem.name == newItem.name
        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) = oldItem == newItem
    }
}