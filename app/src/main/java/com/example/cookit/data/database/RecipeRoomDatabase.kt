package com.example.cookit.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cookit.data.model.Recipes

@Database(entities = [Recipes::class],
    version = 2,
    exportSchema = false)

@TypeConverters(Converters::class)

abstract class RecipeRoomDatabase : RoomDatabase() {

    abstract fun RecipeDAO() : RecipeDAO?

    companion object {
        @Volatile
        private var INSTANCE : RecipeRoomDatabase ? = null
        fun getDatabase(context: Context) : RecipeRoomDatabase? {
            if(INSTANCE == null){
                synchronized(RecipeRoomDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        RecipeRoomDatabase::class.java, "recipes_table"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}
