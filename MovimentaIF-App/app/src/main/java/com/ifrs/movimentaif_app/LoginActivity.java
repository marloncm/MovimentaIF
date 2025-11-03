package com.ifrs.movimentaif_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.TextPaint;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Credential Manager Imports
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ifrs.movimentaif_app.service.UserService;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

import java.util.Objects;
import java.util.concurrent.Executors;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private CredentialManager credentialManager;

    private UserService userService;

    // !!! CRÍTICO: SUBSTITUA PELO SEU WEB CLIENT ID REAL do google-services.json !!!
    private static final String WEB_CLIENT_ID = "SEU_WEB_CLIENT_ID_DO_GOOGLE_SERVICES_JSON";

    private EditText emailInput;
    private EditText passwordInput;
    private Button emailPassLoginBtn;
    private Button registerBtn;
    private Button googleLoginBtn;
    private TextView myTextGradient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        userService = new UserService();
        credentialManager = CredentialManager.create(this);

        // 1. Inicializa views
        emailInput = findViewById(R.id.editTextTextEmailAddress);
        passwordInput = findViewById(R.id.editTextTextPassword);
        emailPassLoginBtn = findViewById(R.id.emailPassLoginBtn);
        registerBtn = findViewById(R.id.button3);
        googleLoginBtn = findViewById(R.id.meu_botao_google_customizado);
        myTextGradient = findViewById(R.id.movimentaifTitleTxt);


        // 2. Aplica o gradiente
        myTextGradient.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        myTextGradient.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        aplicarGradiente(myTextGradient, LoginActivity.this);
                    }
                }
        );

        // 3. Listeners
        emailPassLoginBtn.setOnClickListener(v -> loginWithEmailPassword());
        //googleLoginBtn.setOnClickListener(v -> signInWithGoogle());
        registerBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Funcionalidade de Cadastro pendente.", Toast.LENGTH_SHORT).show();
        });

        // Aplica padding do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            checkUserRoleAndRedirect(currentUser);
        }
    }

    // --- Métodos de UI e Gradiente ---

    private void aplicarGradiente(TextView textView, Context context) {
        TextPaint paint = textView.getPaint();
        float textWidth = paint.measureText(textView.getText().toString());

        int initialColor = ContextCompat.getColor(context, R.color.movimentaif_logo_sub_color);
        int endColor = ContextCompat.getColor(context, R.color.movimentaif_logo_main_color);

        Shader textShader = new LinearGradient(
                0, 0,
                textWidth, 0,
                new int[]{
                        initialColor,
                        endColor
                },
                null,
                Shader.TileMode.CLAMP
        );
        paint.setShader(textShader);
        textView.invalidate();
    }


    // --- Fluxo de Login Email/Senha ---
    private void loginWithEmailPassword() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha o email e a senha.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "A senha deve ter no mínimo 6 caracteres.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                        finish();
                        //checkUserRoleAndRedirect(user);
                    } else {
                        Log.w(TAG, "signInWithEmail: Falha", task.getException());
                        Toast.makeText(LoginActivity.this, "Credenciais inválidas ou conta não existe.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    // --- Fluxo de Login Google (Credential Manager) ---



    // 5. Manipula a Credential obtida (Chamado por Credential Manager)
    private void handleSignIn(String credentialType, Bundle data) {
        if (credentialType.equals(GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
            // Se for um token do Google, extrai o ID Token
            GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(data);
            if (googleIdTokenCredential != null) {
                //firebaseAuthWithGoogle(googleIdTokenCredential.getIdToken());
            }
        } else {
            Log.w(TAG, "Credencial não é do tipo Google ID: " + credentialType);
            Toast.makeText(this, "Tipo de credencial incompatível.", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    // 6. Autentica no Firebase com o ID Token
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Obtém o providerId da Credential para o Upsert no backend
                        AuthCredential authCredential = Objects.requireNonNull(task.getResult().getCredential());
                        String providerId = authCredential.getProviderId();

                        // 1. Chama o backend para o "Upsert"
                        userService.upsertUser(user, providerId);

                        // 2. Redireciona
                        checkUserRoleAndRedirect(user);
                    } else {
                        Toast.makeText(LoginActivity.this, "Autenticação Firebase falhou.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    */

    // --- Lógica de Permissão e Redirecionamento ---
    private void checkUserRoleAndRedirect(FirebaseUser user) {
        userService.getUserRole(user.getUid())
                .thenAccept(role -> {
                    if (role.equals("USER")) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else if (role.equals("ADMIN")) {
                        Toast.makeText(LoginActivity.this, "Acesso negado. Administradores devem usar o sistema Web.", Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                    } else {
                        Toast.makeText(LoginActivity.this, "Conta não configurada. Contate a administração.", Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                    }
                })
                .exceptionally(e -> {
                    Log.e(TAG, "Falha ao verificar role do usuário: ", e);
                    Toast.makeText(LoginActivity.this, "Erro de comunicação com o servidor. Tente mais tarde.", Toast.LENGTH_LONG).show();
                    mAuth.signOut();
                    return null;
                });
    }
}
