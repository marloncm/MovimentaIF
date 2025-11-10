package com.ifrs.movimentaif

import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.ifrs.movimentaif.model.User
import com.ifrs.movimentaif.api.RetrofitInstance
import kotlinx.coroutines.launch


class RegisterActivity : AppCompatActivity() {

    //carregando elementos da tela
    private lateinit var nameInputField: EditText
    private lateinit var emailInputField: EditText
    private lateinit var passwordInputField: EditText
    private lateinit var registerButton: Button
    private lateinit var cancelButton: Button

    //Firebase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        val logoTextView = findViewById<TextView>(R.id.logoTxt)

        nameInputField = findViewById(R.id.inputNameRegister)
        emailInputField = findViewById(R.id.inputEmailRegister)
        passwordInputField = findViewById(R.id.inputPassRegister)
        registerButton = findViewById(R.id.btnConfirmRegister)
        cancelButton = findViewById(R.id.btnCancelRegister)

        applyLogoGradient(logoTextView)

        cancelButton.setOnClickListener {
            cancelRegistration()
        }

        registerButton.setOnClickListener {
            val username = nameInputField.text.toString()
            val email = emailInputField.text.toString()
            val password = passwordInputField.text.toString()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            registerAccount(username, email, password)
        }

    }



    fun applyLogoGradient(logoTextView: TextView) {
        logoTextView.post {
            val paint = logoTextView.paint
            val texto = logoTextView.text.toString()
            val textWidth = paint.measureText(texto)
            val x0 = (logoTextView.width - textWidth) / 2f
            val x1 = x0 + textWidth
            val corInicio = ContextCompat.getColor(this, R.color.maisMovimentoSecondary)
            val corFim = ContextCompat.getColor(this, R.color.maisMovimentoPrimary)

            val textShader = LinearGradient(
                x0, 0f, x1, 0f, // (x0, y0, x1, y1) -> Alinhado ao texto
                corInicio,
                corFim,
                Shader.TileMode.CLAMP
            )

            logoTextView.paint.shader = textShader
            logoTextView.invalidate()
        }
    }

    fun cancelRegistration() {
        finish()
    }

    fun registerAccount(username: String, email: String, password: String){
// Desabilita o botão para evitar cliques duplos
        registerButton.isEnabled = false
        Toast.makeText(this, "Registrando...", Toast.LENGTH_SHORT).show()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // SUCESSO NO FIREBASE
                    Log.d("Firebase", "createUserWithEmail:success")

                    // Crie o objeto User para enviar à sua API
                    val newUser = User(username, email, false)

                    // Etapa 2: Chamar sua API interna
                    registerUserInInternalApi(newUser)

                } else {
                    // FALHA NO FIREBASE
                    Log.w("Firebase", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Falha no registro: ${task.exception?.message}",
                        Toast.LENGTH_LONG,
                    ).show()

                    // Reabilita o botão
                    registerButton.isEnabled = true
                }
            }
    }


    private fun registerUserInInternalApi(user: User) {
        // Use Coroutines para a chamada de rede
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.registerUser(user)

                if (response.isSuccessful) {
                    // SUCESSO NA API INTERNA
                    Log.d("API", "Usuário registrado na API interna: ${response.body()}")
                    Toast.makeText(baseContext, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()

                    // Envia o usuário de volta para a tela de Login
                    finish()

                } else {
                    // FALHA NA API INTERNA (Ex: email já existe no seu DB)
                    Log.e("API", "Erro ao registrar na API: ${response.errorBody()?.string()}")
                    Toast.makeText(baseContext, "Erro ao salvar dados (API).", Toast.LENGTH_LONG).show()
                    registerButton.isEnabled = true

                    // (Opcional avançado): Se a API falhar, você deveria deletar
                    // o usuário recém-criado no Firebase para evitar inconsistência.
                }

            } catch (e: Exception) {
                // FALHA DE CONEXÃO (Ex: API offline ou IP errado)
                Log.e("API", "Falha de conexão: ${e.message}")
                Toast.makeText(baseContext, "Falha de conexão com o servidor.", Toast.LENGTH_LONG).show()
                registerButton.isEnabled = true
            }
        }
    }
}