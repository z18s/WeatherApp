package com.example.weatherapp.mvp.model.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.mvp.model.database.dao.FavoritesDao
import com.example.weatherapp.mvp.model.database.data.RoomFavorite

@Database(entities = [RoomFavorite::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private const val DB_NAME: String = "database.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .build()
                .also { INSTANCE = it }
        }
    }

    abstract fun favoritesDao(): FavoritesDao
}