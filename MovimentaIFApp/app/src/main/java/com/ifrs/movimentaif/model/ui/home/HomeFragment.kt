package com.ifrs.movimentaif.model.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.ifrs.movimentaif.R
import com.ifrs.movimentaif.api.RetrofitInstance
import com.ifrs.movimentaif.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

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
        loadStatistics()
    }

    private fun loadStatistics() {
        val userId = auth.currentUser?.uid ?: return
        
        lifecycleScope.launch {
            try {
                // Carregar total de treinos completados
                val totalResponse = RetrofitInstance.api.getTotalWorkoutsCompleted(userId)
                if (totalResponse.isSuccessful) {
                    val total = totalResponse.body() ?: 0
                    binding.textWorkoutCount.text = total.toString()
                }
                
                // Carregar dias ativos
                val activeDaysResponse = RetrofitInstance.api.getActiveDaysCount(userId)
                if (activeDaysResponse.isSuccessful) {
                    val activeDays = activeDaysResponse.body() ?: 0
                    binding.textActiveDays.text = activeDays.toString()
                }
            } catch (e: Exception) {
                // Silenciosamente mantém valores padrão em caso de erro
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Recarregar estatísticas quando o fragment volta a ficar visível
        loadStatistics()
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
            com.ifrs.movimentaif.utils.SoundManager.playClickSound()
            findNavController().navigate(R.id.nav_user_workouts)
        }

        binding.cardExercises.setOnClickListener {
            com.ifrs.movimentaif.utils.SoundManager.playClickSound()
            findNavController().navigate(R.id.nav_workout_list)
        }

        binding.cardProfile.setOnClickListener {
            com.ifrs.movimentaif.utils.SoundManager.playClickSound()
            findNavController().navigate(R.id.nav_profile)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}