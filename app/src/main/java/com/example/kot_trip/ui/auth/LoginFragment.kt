package com.example.kot_trip.ui.auth

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.cloudinary.api.exceptions.ApiException
import com.example.kot_trip.R
import com.example.kot_trip.data.remote.FirebaseModel
import com.example.kot_trip.data.repository.UserRepository
import com.example.kot_trip.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var viewModel: AuthViewModel

    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data = result.data
        Log.d("LoginFragment", "onActivityResult: $result")
        if (result.resultCode == RESULT_OK && data != null) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                FirebaseModel.googleSignIn(account.idToken!!, onSuccess = { user ->
                    findNavController().navigate(R.id.action_logInFragment_to_profileFragment)
                }, onFailure = {
                    Toast.makeText(requireContext(), "Google sign in failed", Toast.LENGTH_SHORT).show()
                })
            } catch (e: ApiException) {
                Log.w("LoginFragment", "Google sign in failed", e)
                Toast.makeText(requireContext(), "Google sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)


        _binding = FragmentLoginBinding.inflate(inflater, container, false)

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

        binding.btnGoogleSignIn.setOnClickListener {
            signInWithGoogle()
        }

        viewModel.loginSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "התחברת בהצלחה", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.homeFragment)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.btnLogin.isEnabled = !isLoading
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

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
