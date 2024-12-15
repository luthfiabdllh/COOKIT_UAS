package com.example.cookit.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cookit.data.model.Notification

@Database(entities = [Notification::class], version = 1)
abstract class NotificationRoomDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDAO

    companion object {
        @Volatile
        private var INSTANCE: NotificationRoomDatabase? = null

        fun getDatabase(context: Context): NotificationRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotificationRoomDatabase::class.java,
                    "notification_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}