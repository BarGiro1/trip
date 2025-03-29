package com.example.kot_trip.ui.editpost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kot_trip.R
import com.example.kot_trip.databinding.FragmentEditPostBinding

import com.example.kot_trip.model.Post
import com.example.kot_trip.viewmodel.EditPostViewModel

class EditPostFragment : Fragment(R.layout.fragment_add_post) {

    private var _binding: FragmentEditPostBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditPostViewModel by viewModels() // Initialize ViewModel

    private val args: EditPostFragmentArgs by navArgs()
    private lateinit var post: Post

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        post = args.post

        binding.editTitle.setText(post.title)
        binding.editDescription.setText(post.content)
        binding.editCity.setText(post.city)
        binding.editCountry.setText(post.country)

        binding.buttonUpdate.setOnClickListener {
            val updatedPost = post.copy(
                title = binding.editTitle.text.toString(),
                content = binding.editDescription.text.toString(),
                city = binding.editCity.text.toString(),
                country = binding.editCountry.text.toString()
            )
            viewModel.updatePost(updatedPost)

            // Navigate back to HomeFragment
            findNavController().popBackStack()
        }
    }

}
