package com.example.cookit.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.viewpager2.widget.ViewPager2
import com.example.cookit.R
import com.example.cookit.databinding.ActivityWelcomeBinding
import com.example.cookit.ui.auth.LoginActivity
import com.google.android.material.tabs.TabLayoutMediator

class WelcomeActivity : AppCompatActivity() {
    private lateinit var mViewPager: ViewPager2
    private lateinit var btnCreateAccount: Button
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        btnCreateAccount = binding.btnCreateAccount
        // Mengatur onClickListener untuk tombol membuat akun.
        btnCreateAccount.setOnClickListener {
           val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        mViewPager = findViewById(R.id.viewPager)
        // Mengatur adapter untuk ViewPager dengan menggunakan OnBoardingViewPagerAdapter.
        mViewPager.adapter = OnBoardingViewPagerAdapter(this, this)
        // Menyematkan TabLayout ke ViewPager.
        TabLayoutMediator(binding.pageIndicator, mViewPager) { _, _ -> }.attach()
        mViewPager.offscreenPageLimit = 1
    }
}