package com.example.cookit.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.cookit.AdminHomePage
import com.example.cookit.data.model.User
import com.example.cookit.data.network.ApiClient
import com.example.cookit.databinding.ActivityLoginBinding
import com.example.cookit.ui.sayHi.SayHiActivity
import com.example.cookit.util.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        prefManager = PrefManager.getInstance(this)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val  client = ApiClient.instance

        with(binding){
            loginBtLogin.setOnClickListener {
                val response = client.getAllUsers()
                response.enqueue(object : Callback<List<User>> {
                    override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                        if (response.isSuccessful && response.body() != null) {
                            response.body()?.forEach { i ->
                                if (i.email == loginEtEmail.text.toString() && i.password == loginEtPassword.text.toString()) {
                                    prefManager.setLoggedIn(true)
                                    prefManager.saveEmail(i.email)
                                    prefManager.saveUsername(i.name)
                                    prefManager.savePassword(i.password)
                                    prefManager.saveRole(i.role)
                                    checkLoginStatus()
                                    finish()
                                }
                            }
                        }
                    }
                    override fun onFailure(call: Call<List<User>>, t: Throwable) {
                        showToast("Failed to fetch users: ${t.message}")
                        Log.e("LoginActivity", "Error fetching users", t)
                    }
                })
            }

            loginBtnSignup.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun checkLoginStatus(){
        if(prefManager.isLoggedIn()){
            if(prefManager.getRole() == "admin"){
                val intentToHome = Intent(this@LoginActivity, AdminHomePage::class.java)
                startActivity(intentToHome)
            }else if(prefManager.getRole() == "user"){
                val intentToHome = Intent(this@LoginActivity, SayHiActivity::class.java)
                startActivity(intentToHome)
            }
        }
    }
}