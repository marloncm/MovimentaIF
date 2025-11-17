package com.ifrs.movimentaif.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.ifrs.movimentaif.R
import com.ifrs.movimentaif.api.RetrofitInstance
import com.ifrs.movimentaif.utils.setOnClickListenerWithSound
import com.ifrs.movimentaif.ui.parq.ParQActivity
import com.ifrs.movimentaif.ui.anamnese.AnamneseActivity
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var tvUserStatus: TextView
    private lateinit var btnEditProfile: Button
    private lateinit var cardParQ: MaterialCardView
    private lateinit var cardAnamnese: MaterialCardView
    private lateinit var tvParQStatus: TextView
    private lateinit var tvAnamneseStatus: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        tvUserName = view.findViewById(R.id.tvUserName)
        tvUserEmail = view.findViewById(R.id.tvUserEmail)
        tvUserStatus = view.findViewById(R.id.tvUserStatus)
        btnEditProfile = view.findViewById(R.id.btnEditProfile)
        cardParQ = view.findViewById(R.id.cardParQ)
        cardAnamnese = view.findViewById(R.id.cardAnamnese)
        tvParQStatus = view.findViewById(R.id.tvParQStatus)
        tvAnamneseStatus = view.findViewById(R.id.tvAnamneseStatus)

        loadUserData()

        btnEditProfile.setOnClickListenerWithSound {
            val intent = Intent(requireContext(), ProfileEditActivity::class.java)
            startActivity(intent)
        }

        cardParQ.setOnClickListener {
            com.ifrs.movimentaif.utils.SoundManager.playClickSound()
            val intent = Intent(requireContext(), ParQActivity::class.java)
            startActivity(intent)
        }

        cardAnamnese.setOnClickListener {
            com.ifrs.movimentaif.utils.SoundManager.playClickSound()
            val intent = Intent(requireContext(), AnamneseActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadUserData()
    }

    private fun loadUserData() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            lifecycleScope.launch {
                try {
                    val response = RetrofitInstance.api.getUserById(currentUser.uid)
                    if (response.isSuccessful) {
                        val user = response.body()
                        if (user != null) {
                            tvUserName.text = user.userName ?: "Nome não informado"
                            tvUserEmail.text = user.email ?: currentUser.email
                            
                            // Exibir status baseado em isActive
                            if (user.isActive) {
                                tvUserStatus.text = "Usuário Ativo"
                                tvUserStatus.setTextColor(resources.getColor(android.R.color.holo_green_dark, null))
                            } else {
                                tvUserStatus.text = "Aguardando aprovação da academia"
                                tvUserStatus.setTextColor(resources.getColor(android.R.color.holo_orange_dark, null))
                            }

                            if (user.parqId != null && user.parqId.isNotEmpty()) {
                                tvParQStatus.text = "✓ Preenchido"
                                tvParQStatus.setTextColor(resources.getColor(android.R.color.holo_green_dark, null))
                            } else {
                                tvParQStatus.text = "Preencher"
                                tvParQStatus.setTextColor(resources.getColor(android.R.color.holo_orange_dark, null))
                            }

                            if (user.anamneseId != null && user.anamneseId.isNotEmpty()) {
                                tvAnamneseStatus.text = "✓ Preenchido"
                                tvAnamneseStatus.setTextColor(resources.getColor(android.R.color.holo_green_dark, null))
                            } else {
                                tvAnamneseStatus.text = "Preencher"
                                tvAnamneseStatus.setTextColor(resources.getColor(android.R.color.holo_orange_dark, null))
                            }
                        }
                    } else {
                        Toast.makeText(context, "Erro ao carregar dados do usuário", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("ProfileFragment", "Erro ao buscar usuário", e)
                    Toast.makeText(context, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}