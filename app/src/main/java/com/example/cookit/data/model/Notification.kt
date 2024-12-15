package com.example.cookit.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_history")
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val message: String
)