package com.example.cookit.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.cookit.data.database.NotificationRoomDatabase
import com.example.cookit.data.model.Notification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == "com.example.cookit.BOOKMARK_ACTION") {
            val recipeTitle = intent.getStringExtra("recipe_title")
            Toast.makeText(context, "Bookmarked: $recipeTitle", Toast.LENGTH_SHORT).show()

            // Save the notification
            val notification = Notification(message = "Bookmarked: $recipeTitle")
            val db = NotificationRoomDatabase.getDatabase(context)
            CoroutineScope(Dispatchers.IO).launch {
                db.notificationDao().insert(notification)
            }
        }
    }
}