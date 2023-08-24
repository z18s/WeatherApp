package com.example.weatherapp.mvp.model.database.room

import com.example.weatherapp.mvp.model.database.data.RoomFavorite
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface IDatabaseConnection {
    fun insertFavorite(favorite: RoomFavorite): Completable
    fun deleteFavorite(favorite: RoomFavorite): Completable
    fun isFavorite(id: Int): Single<Boolean>
    fun getFavorites(): Single<List<RoomFavorite>>
}