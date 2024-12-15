package com.example.cookit.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.cookit.ui.sayHi.SayHiActivity
import com.example.cookit.data.model.User
import com.example.cookit.data.network.ApiClient
import com.example.cookit.databinding.ActivityRegisterBinding
import retrofit2.Call

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        with(binding){
            btSignup.setOnClickListener{
                val name = signupEtName.text.toString().trim()
                val email = signupEtEmail.text.toString().trim()
                val password = signupEtPassword.text.toString().trim()
                val user = User(name, email, password)

                if(email.isEmpty() || password.isEmpty() || name.isEmpty() ) {
                    showToast("Please fill in all fields.")
                    return@setOnClickListener
                }

                ApiClient.instance.createUser(user).enqueue(object : retrofit2.Callback<User> {
                    override fun onResponse(call: Call<User>, response: retrofit2.Response<User>) {
                        if (response.isSuccessful) {
                            showToast("Registration successful!")
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            showToast("Registration failed: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        showToast("Error: ${t.message}")
                    }
                })
            }

            signupBtnLogin.setOnClickListener{
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}