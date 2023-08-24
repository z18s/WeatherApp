package com.example.weatherapp.mvp.model.database.room

import com.example.weatherapp.mvp.model.database.data.RoomFavorite

interface IDatabaseConnection {
    fun insertFavorite(favorite: RoomFavorite)
    fun deleteFavorite(favorite: RoomFavorite)
    fun isFavorite(id: Int): Boolean
    fun getFavorites(): List<RoomFavorite>
}