package com.example.trendify.ui.onboarding

import Constants
import SharedPrefManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.example.trendify.databinding.ActivityOnboardingBinding
import com.example.trendify.ui.home.HomeActivity

class OnboardingActivity : AppCompatActivity() {
    lateinit var binding: ActivityOnboardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = OnboardingAdapter(Constants.boards)
        val viewPager = binding.viewPager
        viewPager.adapter = adapter
        val indicator = binding.dotsIndicator
        indicator.attachTo(viewPager)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == Constants.boards.size - 1) {
                    binding.startBtn.isVisible = true
                    binding.dotsIndicator.isVisible = false

                } else {
                    binding.startBtn.isVisible = false
                    binding.dotsIndicator.isVisible = true
                }
            }
        })
        startHomeActivity()
    }

    private fun startHomeActivity() {
        binding.startBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
            SharedPrefManager.put(Constants.IS_FIRST, false)
        }
    }

}