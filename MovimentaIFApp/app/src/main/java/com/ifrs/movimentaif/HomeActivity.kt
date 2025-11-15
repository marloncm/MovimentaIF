package com.ifrs.movimentaif

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.ifrs.movimentaif.databinding.ActivityHomeBinding
import com.ifrs.movimentaif.utils.SoundManager

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Inicializar SoundManager
        SoundManager.init(applicationContext)

        setSupportActionBar(binding.appBarHome.toolbar)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Solicitar permissão de notificação para Android 13+
        requestNotificationPermission()

        // Obter token FCM
        getFCMToken()

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        
        // Configurar navegação com IDs corretos do menu
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, 
                R.id.nav_profile,
                R.id.nav_user_workouts,
                R.id.nav_workout_list,
                R.id.nav_about
            ), drawerLayout
        )
        
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        
        // Carregar dados do usuário no header
        updateNavigationHeader(navView)

        // Verificar se deve abrir um fragmento específico (via notificação)
        intent.getStringExtra("openFragment")?.let { fragment ->
            when (fragment) {
                "userWorkouts" -> navController.navigate(R.id.nav_user_workouts)
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_CODE
                )
            }
        }
    }

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d("FCM", "FCM Token: $token")
            
            // Aqui você pode enviar o token para seu servidor se necessário
        }
    }

    companion object {
        private const val NOTIFICATION_PERMISSION_CODE = 1001
    }

    private fun updateNavigationHeader(navigationView: NavigationView) {
        val headerView = navigationView.getHeaderView(0)
        val userNameTextView = headerView.findViewById<TextView>(R.id.nav_header_user_name)
        val userEmailTextView = headerView.findViewById<TextView>(R.id.nav_header_user_email)
        
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Mostrar nome do usuário ou email se nome não estiver disponível
            val displayName = currentUser.displayName
            userNameTextView.text = if (!displayName.isNullOrEmpty()) {
                displayName
            } else {
                currentUser.email?.substringBefore('@') ?: getString(R.string.nav_header_title)
            }
            
            // Mostrar email
            userEmailTextView.text = currentUser.email ?: getString(R.string.nav_header_subtitle)
        } else {
            // Valores padrão se não houver usuário logado
            userNameTextView.text = getString(R.string.nav_header_title)
            userEmailTextView.text = getString(R.string.nav_header_subtitle)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        SoundManager.release()
    }
}