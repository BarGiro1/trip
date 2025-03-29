package com.example.kot_trip.ui.home

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kot_trip.databinding.ItemPostBinding
import com.example.kot_trip.model.Post
import com.squareup.picasso.Picasso

class PostAdapter(
    private val onEditClick: (Post) -> Unit,
    private val onDeleteClick: (Post) -> Unit
) : ListAdapter<Post, PostAdapter.PostViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    inner class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            Log.d("PostAdapter", "bind: $post")
            binding.textViewTitle.text = post.title
            binding.textViewDescription.text = post.content
            binding.textViewCity.text = post.city
            binding.textViewCountry.text = post.country
            Picasso.get().load(post.imageUrl.toUri()).into(binding.imageViewPost)
            binding.buttonEdit.setOnClickListener { onEditClick(post) }
            binding.buttonDelete.setOnClickListener { onDeleteClick(post) }
        }
    }
}