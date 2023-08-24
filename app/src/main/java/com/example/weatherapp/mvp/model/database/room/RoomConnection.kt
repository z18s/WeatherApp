package com.example.weatherapp.mvp.model.database.room

import com.example.weatherapp.mvp.model.database.data.RoomFavorite

class RoomConnection(private val database: AppDatabase) : IDatabaseConnection {

    override fun insertFavorite(favorite: RoomFavorite) = database.favoritesDao().insert(favorite)

    override fun deleteFavorite(favorite: RoomFavorite) = database.favoritesDao().delete(favorite)

    override fun isFavorite(id: Int): Boolean = database.favoritesDao().isTitleExists(id)

    override fun getFavorites(): List<RoomFavorite> = database.favoritesDao().getAll()
}