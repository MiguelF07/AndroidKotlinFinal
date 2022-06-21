package com.example.androidkotlinfinal.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.androidkotlinfinal.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private val mAuth: FirebaseAuth? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val homeViewModel = ViewModelProvider(this).get(
            HomeViewModel::class.java
        )
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        val textView = binding!!.textHome
        homeViewModel.text.observe(
            viewLifecycleOwner
        ) { text: String? ->
            textView.text = text
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}