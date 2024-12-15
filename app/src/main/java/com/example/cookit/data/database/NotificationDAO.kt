package com.example.cookit.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.cookit.data.model.Notification

@Dao
interface NotificationDAO {
    @Insert
    suspend fun insert(notification: Notification)

    @Query("SELECT * FROM notification_history")
    suspend fun getAllNotifications(): List<Notification>
}