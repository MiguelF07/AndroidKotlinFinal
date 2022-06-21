package com.example.androidkotlinfinal.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.androidkotlinfinal.LoginActivity
import com.example.androidkotlinfinal.R
import com.example.androidkotlinfinal.databinding.FragmentNotificationsBinding
import com.google.firebase.auth.FirebaseAuth

class NotificationsFragment : Fragment() {
    private var binding: FragmentNotificationsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val notificationsViewModel = ViewModelProvider(this).get(
            NotificationsViewModel::class.java
        )
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        val textView = binding!!.textNotifications
        notificationsViewModel.text.observe(
            viewLifecycleOwner
        ) { text: String? ->
            textView.text = text
        }
        val logoutButton = root.findViewById<View>(R.id.logoutButton) as Button
        logoutButton.setOnClickListener { view: View? ->
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}