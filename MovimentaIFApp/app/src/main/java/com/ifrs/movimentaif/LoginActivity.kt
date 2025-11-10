package com.ifrs.movimentaif

import android.app.Activity
import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth


class LoginActivity : AppCompatActivity() {

    // --- Firebase ---
    private lateinit var auth: FirebaseAuth

    // --- ❗️ NOVO: Google Sign-In ---
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    // --- Elementos de UI ---
    private lateinit var logoTextView: TextView
    private lateinit var registerButton: Button
    private lateinit var emailInputField: EditText
    private lateinit var passwordInputField: EditText
    private lateinit var loginButton: Button
    private lateinit var googleLoginButton: SignInButton // ❗️ NOVO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1. Inicializar o Firebase Auth
        auth = Firebase.auth

        // 2. Carregar elementos da tela
        logoTextView = findViewById(R.id.logoTxt)
        registerButton = findViewById(R.id.btnRegister)
        emailInputField = findViewById(R.id.inputEmail)
        passwordInputField = findViewById(R.id.inputPassword)
        loginButton = findViewById(R.id.btnEmailLogin)
        googleLoginButton = findViewById(R.id.btnGoogleLogin) // ❗️ NOVO

        // 3. Aplicar gradiente
        applyLogoGradient(logoTextView)

        // 4. Configurar Listeners
        registerButton.setOnClickListener { goToRegister() }
        loginButton.setOnClickListener { performLogin() }

        // --- ❗️ NOVO: Configuração do Google Login ---

        // 5. Criar o cliente de login do Google
        createGoogleSignInClient()

        // 6. Registrar o "ouvinte" para o resultado da tela de login do Google
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    // Sucesso! Pegar o ID Token
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("GoogleLogin", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Falha
                    Log.w("GoogleLogin", "Google sign in failed", e)
                    Toast.makeText(this, "Login com Google falhou.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Login com Google cancelado.", Toast.LENGTH_SHORT).show()
            }
        }

        // 7. Configurar o clique do botão do Google
        googleLoginButton.setOnClickListener {
            googleSignIn()
        }
    }

    /**
     * ❗️ NOVA FUNÇÃO: Inicia a tela de seleção de conta do Google
     */
    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        activityResultLauncher.launch(signInIntent)
    }

    /**
     * ❗️ NOVA FUNÇÃO: Configura o cliente Google
     */
    private fun createGoogleSignInClient() {
        // Configura o login do Google para pedir o ID Token.
        // O R.string.default_web_client_id é pego automaticamente
        // do seu arquivo google-services.json
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    /**
     * ❗️ NOVA FUNÇÃO: Autentica com o FIREBASE usando o token do GOOGLE
     */
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sucesso no login do Firebase
                    Log.d("FirebaseGoogle", "signInWithCredential:success")
                    Toast.makeText(baseContext, "Login com Google bem-sucedido!", Toast.LENGTH_SHORT).show()
                    goToHome() // Redireciona para a tela principal
                } else {
                    // Falha no login do Firebase
                    Log.w("FirebaseGoogle", "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Falha na autenticação.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // --- Suas funções existentes ---

    private fun performLogin() {
        // (Seu código de login com e-mail/senha...)
        // ...
        val email = emailInputField.text.toString().trim()
        val password = passwordInputField.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha o e-mail e a senha.", Toast.LENGTH_SHORT).show()
            return
        }

        loginButton.isEnabled = false
        Toast.makeText(this, "Entrando...", Toast.LENGTH_SHORT).show()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseLogin", "signInWithEmail:success")
                    Toast.makeText(baseContext, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                    goToHome()
                } else {
                    Log.w("FirebaseLogin", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Falha na autenticação: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    loginButton.isEnabled = true
                }
            }
    }

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java) // ❗️ Certifique-se que HomeActivity existe
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    fun goToRegister() {
        val intent = android.content.Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun applyLogoGradient(logoTextView: TextView) {
        logoTextView.post {
            val paint = logoTextView.paint
            val texto = logoTextView.text.toString()
            val textWidth = paint.measureText(texto)
            val x0 = (logoTextView.width - textWidth) / 2f
            val x1 = x0 + textWidth
            val corInicio = ContextCompat.getColor(this, R.color.maisMovimentoSecondary)
            val corFim = ContextCompat.getColor(this, R.color.maisMovimentoPrimary)

            val textShader = LinearGradient(
                x0, 0f, x1, 0f,
                corInicio,
                corFim,
                Shader.TileMode.CLAMP
            )

            logoTextView.paint.shader = textShader
            logoTextView.invalidate()
        }
    }
}