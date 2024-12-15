package com.example.cookit.ui.sayHi

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cookit.ui.user.MainActivity
import com.example.cookit.R
import com.example.cookit.databinding.ActivitySayHiBinding
import com.example.cookit.util.PrefManager

class SayHiActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySayHiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySayHiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val prefManager = PrefManager.getInstance(this)
        val username = prefManager.getUsername()

        with(binding){
            tvName.text = "Hi, $username"

            view.setOnClickListener{
                val intent = Intent(this@SayHiActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SayHiActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500)
    }
}