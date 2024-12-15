package com.example.cookit.util

import android.content.Context
import android.content.SharedPreferences

object NotificationHistoryManager {
    private const val PREFS_NAME = "notification_history"
    private const val KEY_HISTORY = "history"

    fun saveNotification(context: Context, notification: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME,
            Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        val history = getNotificationHistory(context).toMutableList()
        history.add(notification)
        editor.putStringSet(KEY_HISTORY, history.toSet())
        editor.apply()
    }

    fun getNotificationHistory(context: Context): List<String> {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME,
            Context.MODE_PRIVATE
        )
        return prefs.getStringSet(KEY_HISTORY, emptySet())?.toList() ?: emptyList()
    }
}