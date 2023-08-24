package com.example.weatherapp.mvp.model.database.room

import com.example.weatherapp.mvp.model.database.data.RoomFavorite
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class RoomConnection(private val database: AppDatabase) : IDatabaseConnection {

    override fun insertFavorite(favorite: RoomFavorite): Completable = Completable.fromAction {
        database.favoritesDao().insert(favorite)
    }.subscribeOn(Schedulers.io())

    override fun deleteFavorite(favorite: RoomFavorite): Completable = Completable.fromAction {
        database.favoritesDao().delete(favorite)
    }.subscribeOn(Schedulers.io())

    override fun isFavorite(id: Int): Single<Boolean> = Single.fromCallable {
        database.favoritesDao().isTitleExists(id)
    }.subscribeOn(Schedulers.io())

    override fun getFavorites(): Single<List<RoomFavorite>> = Single.fromCallable {
        database.favoritesDao().getAll()
    }.subscribeOn(Schedulers.io())
}