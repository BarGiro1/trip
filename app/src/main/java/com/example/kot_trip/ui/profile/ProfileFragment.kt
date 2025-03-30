package com.example.kot_trip.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.kot_trip.R
import com.example.kot_trip.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.tvUserName.text = user.name
            binding.tvUserEmail.text = user.email
            //binding.ivProfileImage.setImageURI(user.profileImageUri)
        }


        binding.btnUpdateProfile.setOnClickListener {
            // Implement profile update logic
        }

        binding.ivProfileImage.setOnClickListener {
            // Implement profile image update logic
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}