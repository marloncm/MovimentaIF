package com.ifrs.movimentaif

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.ifrs.movimentaif.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        startAnimation()
    }

    private fun startAnimation() {
        // Animação de Zoom (escala)
        val scaleX = ObjectAnimator.ofFloat(binding.splashIcon, View.SCALE_X, 0.5f, 1.5f)
        val scaleY = ObjectAnimator.ofFloat(binding.splashIcon, View.SCALE_Y, 0.5f, 1.5f)
        
        // Animação de Fade Out
        val fadeOut = ObjectAnimator.ofFloat(binding.splashIcon, View.ALPHA, 1f, 0f)
        fadeOut.startDelay = 1500 // Começa depois de 1.5 segundos
        fadeOut.duration = 500 // Dura 0.5 segundos
        
        // Animação do texto
        val textFadeOut = ObjectAnimator.ofFloat(binding.splashText, View.ALPHA, 1f, 0f)
        textFadeOut.startDelay = 1500
        textFadeOut.duration = 500

        // Combinar animações
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY, fadeOut, textFadeOut)
        animatorSet.duration = 2000
        animatorSet.interpolator = AccelerateInterpolator()
        
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            
            override fun onAnimationEnd(animation: Animator) {
                navigateToNextScreen()
            }
            
            override fun onAnimationCancel(animation: Animator) {}
            
            override fun onAnimationRepeat(animation: Animator) {}
        })
        
        animatorSet.start()
    }

    private fun navigateToNextScreen() {
        val currentUser = auth.currentUser
        
        val intent = if (currentUser != null) {
            // Usuário já está logado, ir para Home
            Intent(this, HomeActivity::class.java)
        } else {
            // Usuário não está logado, ir para Login
            Intent(this, LoginActivity::class.java)
        }
        
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
