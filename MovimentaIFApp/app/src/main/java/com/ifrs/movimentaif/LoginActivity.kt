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
import androidx.appcompat.app.AlertDialog
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
import com.ifrs.movimentaif.utils.BiometricManager
import com.ifrs.movimentaif.utils.SecurePreferences


class LoginActivity : AppCompatActivity() {

    // --- Firebase ---
    private lateinit var auth: FirebaseAuth

    // --- Google Sign-In ---
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    // --- Biometria ---
    private lateinit var biometricManager: BiometricManager
    private lateinit var securePreferences: SecurePreferences

    // --- Elementos de UI ---
    private lateinit var logoTextView: TextView
    private lateinit var registerButton: Button
    private lateinit var emailInputField: EditText
    private lateinit var passwordInputField: EditText
    private lateinit var loginButton: Button
    private lateinit var googleLoginButton: SignInButton
    private lateinit var btnBiometric: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1. Inicializar o Firebase Auth
        auth = Firebase.auth

        // Inicializar biometria e preferências
        biometricManager = BiometricManager(this)
        securePreferences = SecurePreferences(this)

        // 2. Carregar elementos da tela
        logoTextView = findViewById(R.id.logoTxt)
        registerButton = findViewById(R.id.btnRegister)
        emailInputField = findViewById(R.id.inputEmail)
        passwordInputField = findViewById(R.id.inputPassword)
        loginButton = findViewById(R.id.btnEmailLogin)
        googleLoginButton = findViewById(R.id.btnGoogleLogin)
        btnBiometric = findViewById(R.id.btnBiometric)

        // 3. Aplicar gradiente
        applyLogoGradient(logoTextView)

        // 4. Verificar se biometria está habilitada
        if (securePreferences.isBiometricEnabled() && 
            BiometricManager.isBiometricAvailable(this)) {
            btnBiometric.visibility = android.view.View.VISIBLE
            btnBiometric.setOnClickListener { authenticateWithBiometric() }
        } else {
            btnBiometric.visibility = android.view.View.GONE
        }

        // 5. Configurar Listeners
        registerButton.setOnClickListener { goToRegister() }
        loginButton.setOnClickListener { performLogin() }

        // --- ❗️ NOVO: Configuração do Google Login ---

        // 6. Criar o cliente de login do Google
        createGoogleSignInClient()

        // 7. Registrar o "ouvinte" para o resultado da tela de login do Google
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

        // 8. Configurar o clique do botão do Google
        googleLoginButton.setOnClickListener {
            googleSignIn()
        }
    }

    private fun authenticateWithBiometric() {
        biometricManager.authenticate(
            title = "Login MovimentaIF",
            subtitle = "Use sua biometria para entrar",
            negativeButtonText = "Usar senha",
            onSuccess = {
                // Autenticação bem-sucedida, fazer login automático
                val userId = securePreferences.getUserId()
                val email = securePreferences.getUserEmail()
                
                if (userId != null && email != null) {
                    Log.d("BiometricLogin", "Login via biometria - userId: $userId")
                    goToHome()
                } else {
                    Toast.makeText(this, "Erro ao recuperar dados do usuário", Toast.LENGTH_SHORT).show()
                }
            },
            onError = { error ->
                Toast.makeText(this, "Erro: $error", Toast.LENGTH_SHORT).show()
            },
            onFailed = {
                Toast.makeText(this, "Biometria não reconhecida", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun showBiometricDialog() {
        if (!BiometricManager.isBiometricAvailable(this)) {
            goToHome()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Habilitar Login Biométrico")
            .setMessage("Deseja usar sua biometria (impressão digital/reconhecimento facial) para fazer login mais rapidamente nas próximas vezes?")
            .setPositiveButton("Sim") { dialog, _ ->
                val user = auth.currentUser
                if (user != null) {
                    securePreferences.saveBiometricEnabled(true)
                    securePreferences.saveUserId(user.uid)
                    securePreferences.saveUserEmail(user.email ?: "")
                    Toast.makeText(this, "Login biométrico ativado!", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
                goToHome()
            }
            .setNegativeButton("Não") { dialog, _ ->
                dialog.dismiss()
                goToHome()
            }
            .show()
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
                    
                    // Verificar se já tem biometria configurada
                    if (!securePreferences.isBiometricEnabled()) {
                        showBiometricDialog()
                    } else {
                        goToHome()
                    }
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
                    
                    // Verificar se já tem biometria configurada
                    if (!securePreferences.isBiometricEnabled()) {
                        showBiometricDialog()
                    } else {
                        goToHome()
                    }
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