package com.example.kot_trip.ui.home

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kot_trip.databinding.ItemPostBinding
import com.example.kot_trip.model.Post
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import com.example.kot_trip.utils.GeminiHelper


import com.squareup.picasso.Picasso

class PostAdapter(
    private val onEditClick: (Post) -> Unit = {},
    private val onDeleteClick: (Post) -> Unit = {},
    private val enableEdit: Boolean = false
) : ListAdapter<Post, PostAdapter.PostViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, enableEdit)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    inner class PostViewHolder(private val binding: ItemPostBinding, private val enableEdit: Boolean) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            Log.d("PostAdapter", "bind: $post")
            binding.textViewTitle.text = post.title
            binding.textViewDescription.text = post.content
            binding.textViewCity.text = post.city
            binding.textViewCountry.text = post.country

            Picasso.get().load(post.imageUrl.toUri()).into(binding.imageViewPost)

            binding.buttonEdit.setOnClickListener { onEditClick(post) }
            binding.buttonDelete.setOnClickListener { onDeleteClick(post) }
            binding.buttonAiTip.setOnClickListener {
                GeminiHelper.fetchDailyTipForCountry(post.country) { tip ->
                    Handler(Looper.getMainLooper()).post {
                        AlertDialog.Builder(binding.root.context)
                            .setTitle("טיפ ל-${post.country}")
                            .setMessage(tip)
                            .setPositiveButton("סגור", null)
                            .show()
                    }
                }
            }

            if (!enableEdit) {
                binding.buttonEdit.visibility = RecyclerView.GONE
                binding.buttonDelete.visibility = RecyclerView.GONE
            }
        }
    }
}
