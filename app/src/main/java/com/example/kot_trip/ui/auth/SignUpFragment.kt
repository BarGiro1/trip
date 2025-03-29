package com.example.kot_trip.ui.auth

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.kot_trip.R
import com.example.kot_trip.data.repository.UserRepository
import com.example.kot_trip.databinding.FragmentSignUpBinding
import com.example.kot_trip.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var userRepository: UserRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        userRepository = UserRepository(requireContext())

        binding.btnSignUp.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "נא למלא את כל השדות", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    val uid = result.user?.uid ?: return@addOnSuccessListener
                    val user = User(id = uid, name = name, email = email, profileImageUrl = null)

                    userRepository.saveUser(
                        user,
                        onSuccess = {
                            Toast.makeText(requireContext(), "נרשמת בהצלחה", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
                        },
                        onFailure = {
                            Toast.makeText(requireContext(), "שגיאה בשמירת המשתמש", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                .addOnFailureListener {
                    Log.e("SignUpFragment", "Error signing up: ${it.message}")
                    Toast.makeText(requireContext(), "שגיאה בהרשמה: ${it.message}", Toast.LENGTH_SHORT).show()
                }

        }

        binding.btnToLogin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
