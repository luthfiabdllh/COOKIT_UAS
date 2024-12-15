package com.example.cookit.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.cookit.R
import com.example.cookit.data.database.NotificationRoomDatabase
import com.example.cookit.data.model.Notification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.d("NotificationReceiver", "Broadcast received with action: $action")
        if (action == "com.example.cookit.BOOKMARK_ACTION") {
            val recipeTitle = intent.getStringExtra("recipe_title")
            Toast.makeText(context, "Bookmarked: $recipeTitle", Toast.LENGTH_SHORT).show()
            Log.d("NotificationReceiver", "Received broadcast for recipe: $recipeTitle")

            val notification = Notification(message = "Bookmarked: $recipeTitle")
            val db = NotificationRoomDatabase.getDatabase(context)
            CoroutineScope(Dispatchers.IO).launch {
                db.notificationDao().insert(notification)
            }

            createNotificationChannel(context)
            Log.d("NotificationReceiver", "Notification channel created")

            val builder = NotificationCompat.Builder(context, "bookmark_channel")
                .setSmallIcon(R.drawable.baseline_favorite_24)
                .setContentTitle("Recipe Bookmarked")
                .setContentText("Bookmarked: $recipeTitle")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(context)) {
                notify(1, builder.build())
                Log.d("NotificationReceiver", "Notification displayed")
            }
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Bookmark Channel"
            val descriptionText = "Channel for bookmark notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("bookmark_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}