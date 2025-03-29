package com.example.kot_trip.ui.auth

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kot_trip.R
import com.example.kot_trip.data.repository.UserRepository
import com.example.kot_trip.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        // יצירת UserRepository וה-VIEWMODEL
        val repo = UserRepository(requireContext())
        viewModel = ViewModelProvider(this, AuthViewModel.Factory(repo))[AuthViewModel::class.java]

        binding.btnLogin.setOnClickListener {
            if (validate()) {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()

                viewModel.loginUser(email, password) { error ->
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnToSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)

        }

        viewModel.loginSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "התחברת בהצלחה", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_logInFragment_to_profileFragment)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.btnLogin.isEnabled = !isLoading
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        binding.btnGoogleSignIn.setOnClickListener() {
            // Handle Google Sign-In
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }


        return binding.root
    }

    private fun validate(): Boolean {
        if (binding.etEmail.text.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "הכנס מייל", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.etPassword.text.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "הכנס סיסמה", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
