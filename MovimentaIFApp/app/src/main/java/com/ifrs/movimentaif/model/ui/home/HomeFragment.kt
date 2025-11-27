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
        loadAcademyInfo()
        loadUserStatus()
    }

    private fun loadAcademyInfo() {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getAcademyInfo()
                if (response.isSuccessful) {
                    val academyInfo = response.body()
                    academyInfo?.let {
                        // Formatar datas
                        val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                        val startDateStr = dateFormat.format(it.startDate)
                        val endDateStr = dateFormat.format(it.endDate)
                        
                        binding.textAcademyPeriod.text = "$startDateStr - $endDateStr"
                        binding.textAcademyHours.text = "${it.openHour} - ${it.closeHour}"
                        binding.textAcademyAdditionalInfo.text = it.additionalInfo
                    }
                }
            } catch (e: Exception) {
                // Mantém valores padrão em caso de erro
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadAcademyInfo()
        loadUserStatus()
    }

    private fun loadUserStatus() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            lifecycleScope.launch {
                try {
                    val response = RetrofitInstance.api.getUserById(currentUser.uid)
                    if (response.isSuccessful) {
                        val user = response.body()
                        user?.let {
                            binding.textUserStatus.visibility = android.view.View.VISIBLE
                            if (it.isActive == true) {
                                binding.textUserStatus.text = "✓ Ativo"
                                binding.textUserStatus.setTextColor(
                                    resources.getColor(android.R.color.holo_green_light, null)
                                )
                            } else {
                                binding.textUserStatus.text = "⏳ Aguardando ativação da academia"
                                binding.textUserStatus.setTextColor(
                                    resources.getColor(android.R.color.holo_orange_light, null)
                                )
                            }
                            
                            // Mostrar data de entrevista apenas se não foi entrevistado ainda
                            if (!it.isInterviewed) {
                                binding.textInterviewDate.visibility = android.view.View.VISIBLE
                                val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                                if (it.interviewDate != null) {
                                    binding.textInterviewDate.text = "📋 Entrevista: ${dateFormat.format(it.interviewDate)}"
                                } else {
                                    binding.textInterviewDate.text = "📋 Entrevista: Aguardando agendamento"
                                }
                            } else {
                                binding.textInterviewDate.visibility = android.view.View.GONE
                            }
                            
                            // Mostrar data do primeiro treino apenas se não foi realizado ainda
                            if (!it.isDidFirstWorkout) {
                                binding.textFirstWorkoutDate.visibility = android.view.View.VISIBLE
                                val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                                if (it.firstWorkoutDate != null) {
                                    binding.textFirstWorkoutDate.text = "💪 Primeiro treino: ${dateFormat.format(it.firstWorkoutDate)}"
                                } else {
                                    binding.textFirstWorkoutDate.text = "💪 Primeiro treino: Aguardando agendamento"
                                }
                            } else {
                                binding.textFirstWorkoutDate.visibility = android.view.View.GONE
                            }
                        }
                    }
                } catch (e: Exception) {
                    binding.textUserStatus.visibility = android.view.View.GONE
                    binding.textInterviewDate.visibility = android.view.View.GONE
                    binding.textFirstWorkoutDate.visibility = android.view.View.GONE
                }
            }
        }
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