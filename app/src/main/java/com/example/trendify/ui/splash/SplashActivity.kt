package com.example.trendify.ui.splash

import Constants
import SharedPrefManager
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.trendify.databinding.ActivitySplashBinding
import com.example.trendify.ui.home.HomeActivity
import com.example.trendify.ui.onboarding.OnboardingActivity

class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        SharedPrefManager.init(this)

        val theme: Int = SharedPrefManager.get(Constants.THEME, AppCompatDelegate.MODE_NIGHT_NO)
        AppCompatDelegate.setDefaultNightMode(theme)

        val lang = SharedPrefManager.get(Constants.LANGUAGE, "en")
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(lang))

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val popOutAnimation = Constants.popOutAnimation(this)
        binding.title.startAnimation(popOutAnimation)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent: Intent
            if(SharedPrefManager.get(Constants.IS_FIRST, true)){
                intent = Intent(this, OnboardingActivity::class.java)
            }else{
                intent = Intent(this, HomeActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 2500)
    }

}
