package com.example.cookit.util

import android.content.Context
import android.content.SharedPreferences

class PrefManager private constructor(context: Context){
    private val sharedPreferences: SharedPreferences

    companion object {
        private const val PREFS_NAME = "AuthAppPrev"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD = "password"
        private const val KEY_ROLE = "user"

        @Volatile
        private var instance: PrefManager? = null
        fun getInstance(context: Context): PrefManager {
            return instance ?: synchronized(this) {
                instance ?: PrefManager(context.applicationContext).also {
                        prev -> instance = prev
                }
            }
        }
    }

    init {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun isLoggedIn(): Boolean{
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun saveUsername(username: String) {
        sharedPreferences.edit().also {
            it.putString(KEY_NAME, username)
            it.apply()
        }
    }

    fun savePassword(password: String) {
        sharedPreferences.edit().also {
            it.putString(KEY_PASSWORD, password)
            it.apply()
        }
    }

    fun saveEmail(email: String) {
        sharedPreferences.edit().also {
            it.putString(KEY_EMAIL, email)
            it.apply()
        }
    }

    fun getUsername(): String {
        return sharedPreferences.getString(KEY_NAME, "") ?: ""
    }

    fun getPassword(): String {
        return sharedPreferences.getString(KEY_PASSWORD, "") ?: ""
    }

    fun getEmail(): String {
        return sharedPreferences.getString(KEY_EMAIL, "") ?: ""
    }

    fun clear(){
        sharedPreferences.edit().also {
            it.clear()
            it.apply()
        }
    }

    fun saveRole(role: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_ROLE, role)
        editor.apply()
    }

    fun getRole(): String {
        return sharedPreferences.getString(KEY_ROLE, "") ?: ""
    }

}