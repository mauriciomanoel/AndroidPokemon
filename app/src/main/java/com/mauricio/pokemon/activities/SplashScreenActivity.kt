package com.mauricio.pokemon.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.mauricio.pokemon.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        nextScreen()
    }

    private fun nextScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            val publicIntent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(publicIntent)
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }, SPLASH_SCREEN_DURATION_TIME)
    }

    companion object {
        const val SPLASH_SCREEN_DURATION_TIME:Long = 2000
    }
}