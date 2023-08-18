package com.example.weatherapp.mvp.model.database.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class RoomFavorite(@PrimaryKey val id: Int, val city: String, val country: String)