package com.example.kot_trip.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.kot_trip.databinding.FragmentAddPostBinding

class AddPostFragment : Fragment() {

    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddPostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPostBinding.inflate(inflater, container, false)

        binding.addpostButtonSubmit.setOnClickListener {
            viewModel.addPost(
                binding.addpostEditTextTitle.text.toString(),
                binding.addpostEditTextDescription.text.toString(),
                binding.addpostEditTextCity.text.toString(),
                binding.addpostEditTextCountry.text.toString()
            )
        }

        viewModel.status.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearStatus()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
