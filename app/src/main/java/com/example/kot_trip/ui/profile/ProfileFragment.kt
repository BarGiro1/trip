package com.example.kot_trip.ui.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.kot_trip.R
import com.example.kot_trip.base.Status
import com.example.kot_trip.base.Utils
import com.example.kot_trip.databinding.FragmentProfileBinding
import com.example.kot_trip.model.User
import com.example.kot_trip.viewmodel.ProfileViewModel
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels() // Initialize ViewModel
    private var selectedImageUri: Uri? = null

    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fetch user data from ViewModel
        viewModel.getUserProfile().observe(viewLifecycleOwner, { user ->
            this.user = user
            binding.editUsername.setText(user.name)
            user.profileImageUrl?.let {
                Picasso.get().load(it).into(binding.ivProfileImage)
            }
        })

        binding.btnUpdateProfile.setOnClickListener {
            val updatedUser = user.copy(
                name = binding.editUsername.text.toString(),
            )
            viewModel.updateUser(updatedUser, Utils.getBitmapFromImageView(binding.ivProfileImage))
        }

        viewModel.status.observe(viewLifecycleOwner) { status ->
            status?.message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        binding.ivProfileImage.setOnClickListener {
            selectImageLauncher.launch("image/*")

        }
    }

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            binding.ivProfileImage.setImageURI(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}