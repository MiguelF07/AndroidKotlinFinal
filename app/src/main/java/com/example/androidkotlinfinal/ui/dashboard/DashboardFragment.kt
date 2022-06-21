package com.example.androidkotlinfinal.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.androidkotlinfinal.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
    private var binding: FragmentDashboardBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val dashboardViewModel = ViewModelProvider(this).get(
            DashboardViewModel::class.java
        )
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        val textView = binding!!.textDashboard
        dashboardViewModel.text.observe(
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