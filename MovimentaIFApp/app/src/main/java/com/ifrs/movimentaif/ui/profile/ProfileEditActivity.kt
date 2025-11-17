package com.ifrs.movimentaif.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.ifrs.movimentaif.R
import com.ifrs.movimentaif.api.RetrofitInstance
import com.ifrs.movimentaif.model.User
import com.ifrs.movimentaif.utils.setOnClickListenerWithSound
import kotlinx.coroutines.launch
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class ProfileEditActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etUserName: TextInputEditText
    private lateinit var etPhoneNumber: TextInputEditText
    private lateinit var etAge: TextInputEditText
    private lateinit var actvAffiliationType: MaterialAutoCompleteTextView
    private lateinit var btnSave: MaterialButton
    private lateinit var btnCancel: MaterialButton
    private lateinit var progressBar: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        auth = FirebaseAuth.getInstance()

        etUserName = findViewById(R.id.etUserName)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        etAge = findViewById(R.id.etAge)
        actvAffiliationType = findViewById(R.id.actvAffiliationType)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)
        progressBar = findViewById(R.id.progressBar)

        setupAffiliationTypeDropdown()
        loadUserData()

        btnSave.setOnClickListenerWithSound {
            saveUserData()
        }

        btnCancel.setOnClickListenerWithSound {
            finish()
        }
    }

    private fun setupAffiliationTypeDropdown() {
        val affiliationTypes = arrayOf("Aluno", "Docente", "Comunidade")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, affiliationTypes)
        actvAffiliationType.setAdapter(adapter)
    }

    private fun loadUserData() {
        val currentUser = auth.currentUser ?: return

        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getUserById(currentUser.uid)
                if (response.isSuccessful) {
                    val user = response.body()
                    user?.let {
                        etUserName.setText(it.userName)
                        etPhoneNumber.setText(it.phoneNumber)
                        it.age?.let { date ->
                            val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                            etAge.setText(dateFormat.format(date))
                        }
                        actvAffiliationType.setText(it.affiliationType ?: "Aluno", false)
                    }
                } else {
                    Toast.makeText(this@ProfileEditActivity, "Erro ao carregar dados", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProfileEditActivity, "Erro de conexão", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun saveUserData() {
        val currentUser = auth.currentUser ?: return

        val userName = etUserName.text.toString().trim()
        val phoneNumber = etPhoneNumber.text.toString().trim()
        val ageText = etAge.text.toString().trim()
        val affiliationType = actvAffiliationType.text.toString()

        if (userName.isEmpty()) {
            etUserName.error = "Nome é obrigatório"
            return
        }
        
        var ageDate: java.util.Date? = null
        if (ageText.isNotEmpty()) {
            try {
                val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                ageDate = dateFormat.parse(ageText)
            } catch (e: Exception) {
                etAge.error = "Data inválida. Use DD/MM/AAAA"
                return
            }
        }

        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getUserById(currentUser.uid)
                if (response.isSuccessful) {
                    val user = response.body() ?: return@launch

                    user.userName = userName
                    user.phoneNumber = phoneNumber
                    user.age = ageDate
                    user.affiliationType = affiliationType

                    val updateResponse = RetrofitInstance.api.updateUser(currentUser.uid, user)
                    if (updateResponse.isSuccessful) {
                        Toast.makeText(this@ProfileEditActivity, "Perfil atualizado com sucesso", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@ProfileEditActivity, "Erro ao atualizar perfil", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProfileEditActivity, "Erro de conexão", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}
