package com.example.kot_trip.ui.editpost

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kot_trip.R
import com.example.kot_trip.databinding.FragmentEditPostBinding
import com.example.kot_trip.viewmodel.EditPostViewModel

class EditPostFragment : Fragment(R.layout.fragment_edit_post) {

    private var _binding: FragmentEditPostBinding? = null
    private val binding get() = _binding!!
    private val args: EditPostFragmentArgs by navArgs()
    private val viewModel: EditPostViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditPostBinding.bind(view)

        val post = args.post

        binding.editTitle.setText(post.title)
        binding.editDescription.setText(post.content)
        binding.editCity.setText(post.city)
        binding.editCountry.setText(post.country)

        binding.buttonUpdate.setOnClickListener {
            val updated = post.copy(
                title = binding.editTitle.text.toString().trim(),
                content = binding.editDescription.text.toString().trim(),
                city = binding.editCity.text.toString().trim(),
                country = binding.editCountry.text.toString().trim()
            )

            viewModel.updatePost(updated)
            Toast.makeText(requireContext(), "Post updated", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
