package com.example.kot_trip.ui.post

import androidx.fragment.app.viewModels
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.kot_trip.base.App
import com.example.kot_trip.base.Countries
import com.example.kot_trip.base.Utils
import com.example.kot_trip.databinding.FragmentAddPostBinding

class AddPostFragment : Fragment() {

    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddPostViewModel by viewModels()

    private var selectedImageUri: Uri? = null


    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            binding.addpostImageView.setImageURI(it)
            binding.addpostImageView.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPostBinding.inflate(inflater, container, false)

        binding.addpostButtonSelectImage.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }

        val adapter = ArrayAdapter(binding.root.context, android.R.layout.simple_dropdown_item_1line, Countries.list)
        binding.addpostEditTextCountry.setAdapter(adapter)

        binding.addpostButtonSubmit.setOnClickListener {
            Log.d("AddPostFragment", "onCreateView: submit clicked")
            viewModel.addPost(
                title = binding.addpostEditTextTitle.text.toString(),
                description = binding.addpostEditTextDescription.text.toString(),
                city = binding.addpostEditTextCity.text.toString(),
                country = binding.addpostEditTextCountry.text.toString(),
                userId = App().getUserId()!!,
                imageBitmap = Utils.getBitmapFromImageView(binding.addpostImageView)
            )
        }
        viewModel.status.observe(viewLifecycleOwner) { status ->
            status?.message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearStatus()
            }

            // back on status success
            if (status?.isSuccess == true) {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
