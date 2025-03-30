package com.example.kot_trip.ui.editpost

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kot_trip.R
import com.example.kot_trip.databinding.FragmentEditPostBinding

import com.example.kot_trip.model.Post
import com.example.kot_trip.viewmodel.EditPostViewModel
import com.squareup.picasso.Picasso

class EditPostFragment : Fragment(R.layout.fragment_edit_post) {

    private var _binding: FragmentEditPostBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditPostViewModel by viewModels() // Initialize ViewModel
    private var selectedImageUri: Uri? = null

    private val args: EditPostFragmentArgs by navArgs()
    private lateinit var post: Post

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            binding.editpostImageView.setImageURI(it)
            binding.editpostImageView.visibility = View.VISIBLE
        }
    }
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

        binding.editpostEditTextTitle.setText(post.title)
        binding.editpostEditTextDescription.setText(post.content)
        binding.editpostEditTextCity.setText(post.city)
        binding.editpostEditTextCountry.setText(post.country)
        if(post.imageUrl.isNullOrEmpty()) {
            binding.editpostImageView.setImageResource(R.drawable.ic_default_avatar)
        } else {
            Picasso.get().load(post.imageUrl).into(binding.editpostImageView)
        }

        binding.editpostButtonUpdate.setOnClickListener {
            val updatedPost = post.copy(
                title = binding.editpostEditTextTitle.text.toString(),
                content = binding.editpostEditTextDescription.text.toString(),
                city = binding.editpostEditTextCity.text.toString(),
                country = binding.editpostEditTextCountry.text.toString()
            )
            viewModel.updatePost(updatedPost)


        }

        viewModel.status.observe(viewLifecycleOwner) { status ->
           status?.message?.let {
               Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
           }

            // Navigate back
            findNavController().popBackStack()
        }
    }

}
