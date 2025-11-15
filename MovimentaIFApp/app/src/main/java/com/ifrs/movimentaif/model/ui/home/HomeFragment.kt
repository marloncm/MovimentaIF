package com.ifrs.movimentaif.model.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.ifrs.movimentaif.R
import com.ifrs.movimentaif.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUserInfo()
        setupClickListeners()
    }

    private fun setupUserInfo() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userName = currentUser.displayName 
                ?: currentUser.email?.substringBefore('@') 
                ?: "Usuário"
            binding.textUserName.text = userName
        }
    }

    private fun setupClickListeners() {
        binding.cardMyWorkout.setOnClickListener {
            findNavController().navigate(R.id.nav_user_workouts)
        }

        binding.cardExercises.setOnClickListener {
            findNavController().navigate(R.id.nav_workout_list)
        }

        binding.cardProfile.setOnClickListener {
            findNavController().navigate(R.id.nav_profile)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}