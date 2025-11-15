package com.ifrs.movimentaif

import android.content.Intent
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

                    // Pegar o UID do usuário criado no Firebase
                    val firebaseUser = auth.currentUser
                    val userId = firebaseUser?.uid ?: ""
                    
                    Log.d("Firebase", "UID criado: $userId")
                    Log.d("Firebase", "Email: $email")
                    Log.d("Firebase", "Nome: $username")

                    // Crie o objeto User com o UID do Firebase
                    val newUser = User(userId, email)
                    newUser.userName = username
                    newUser.isAppUser = true
                    newUser.setActive(true)
                    
                    Log.d("RegisterActivity", "User object criado - UID: ${newUser.userId}, Email: ${newUser.email}, Nome: ${newUser.userName}, isAppUser: ${newUser.isAppUser}, isActive: ${newUser.isActive}")

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
                Log.d("API", "Enviando usuário para API: userId=${user.userId}, email=${user.email}, userName=${user.userName}")
                Log.d("API", "isActive=${user.isActive}, isAppUser=${user.isAppUser}")
                Log.d("API", "interviewed=${user.isInterviewed}, didFirstWorkout=${user.isDidFirstWorkout}")
                Log.d("API", "scheduledFirstWorkout=${user.isScheduledFirstWorkout}")
                Log.d("API", "createdAt=${user.createdAt}")
                
                val response = RetrofitInstance.api.registerUser(user)

                Log.d("API", "Response code: ${response.code()}")
                Log.d("API", "Response message: ${response.message()}")
                Log.d("API", "Response headers: ${response.headers()}")
                
                if (response.isSuccessful) {
                    // SUCESSO NA API INTERNA
                    val responseBody = response.body()
                    Log.d("API", "Usuário registrado na API interna: $responseBody")
                    Toast.makeText(baseContext, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()

                    // Login automático após cadastro - redireciona para HomeActivity
                    val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()

                } else {
                    // FALHA NA API INTERNA
                    val errorBody = response.errorBody()?.string()
                    Log.e("API", "Erro ao registrar na API - Code: ${response.code()}, Error: $errorBody")
                    
                    // Mostrar erro mais detalhado ao usuário
                    val errorMessage = when(response.code()) {
                        400 -> "Dados inválidos: $errorBody"
                        401 -> "Não autorizado. Faça login novamente."
                        409 -> "Este email já está cadastrado."
                        500 -> "Erro no servidor. Tente novamente."
                        else -> "Erro ${response.code()}: $errorBody"
                    }
                    
                    Toast.makeText(baseContext, errorMessage, Toast.LENGTH_LONG).show()
                    registerButton.isEnabled = true
                }

            } catch (e: com.google.gson.JsonSyntaxException) {
                // Erro de parsing JSON (resposta vazia ou inválida)
                Log.e("API", "JsonSyntaxException - Resposta vazia ou JSON inválido", e)
                e.printStackTrace()
                Toast.makeText(baseContext, "Erro: Resposta vazia do servidor. Verifique sua conexão.", Toast.LENGTH_LONG).show()
                registerButton.isEnabled = true
            } catch (e: java.net.UnknownHostException) {
                // Sem conexão com internet
                Log.e("API", "UnknownHostException - Sem conexão", e)
                Toast.makeText(baseContext, "Sem conexão com a internet", Toast.LENGTH_LONG).show()
                registerButton.isEnabled = true
            } catch (e: java.net.SocketTimeoutException) {
                // Timeout
                Log.e("API", "SocketTimeoutException - Timeout", e)
                Toast.makeText(baseContext, "Tempo de espera esgotado. Tente novamente.", Toast.LENGTH_LONG).show()
                registerButton.isEnabled = true
            } catch (e: Exception) {
                // FALHA DE CONEXÃO ou outro erro
                Log.e("API", "Falha de conexão: ${e.javaClass.simpleName} - ${e.message}", e)
                e.printStackTrace()
                Toast.makeText(baseContext, "Erro: ${e.javaClass.simpleName}: ${e.message}", Toast.LENGTH_LONG).show()
                registerButton.isEnabled = true
            }
        }
    }
}