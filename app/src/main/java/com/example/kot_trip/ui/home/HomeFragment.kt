package com.example.kot_trip.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kot_trip.R
import com.example.kot_trip.databinding.FragmentHomeBinding
import com.example.kot_trip.model.Post

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: PostAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)

        // ריענון הנתונים מה-Cloud
        viewModel.refreshPosts()

        adapter = PostAdapter()
        binding.recyclerViewPosts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPosts.adapter = adapter

        // חיפוש תוך כדי כתיבה
        binding.searchField.doOnTextChanged { text, _, _, _ ->
            adapter.submitList(viewModel.allPosts.value?.filter {
                it.title.contains(text.toString(), ignoreCase = true) ||
                        it.country.contains(text.toString(), ignoreCase = true) ||
                        it.city.contains(text.toString(), ignoreCase = true)
            })
        }

        // מעקב אחרי פוסטים
        viewModel.allPosts.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
