package com.example.kot_trip.ui.saved

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kot_trip.R
import com.example.kot_trip.databinding.FragmentHomeBinding
import com.example.kot_trip.model.Post
import com.example.kot_trip.ui.home.PostAdapter

class SavedFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    private val viewModel: SavedViewModel by viewModels()
    private lateinit var adapter: PostAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        adapter = PostAdapter(
            onEditClick = { post -> navigateToEditPost(post) },
            onDeleteClick = { post -> deletePost(post) },
            enableEdit = true
        )

        binding.recyclerViewPosts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPosts.adapter = adapter

        viewModel.allPosts.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }

        viewModel.refreshPosts()
    }

    private fun navigateToEditPost(post: Post) {
        val action = SavedFragmentDirections.actionHomeToEdit(post)
        findNavController().navigate(action)
    }

    private fun deletePost(post: Post) {
        viewModel.deletePost(post)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
