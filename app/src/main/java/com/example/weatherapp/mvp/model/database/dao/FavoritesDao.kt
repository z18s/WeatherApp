package com.example.weatherapp.mvp.model.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weatherapp.mvp.model.database.data.RoomFavorite

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite: RoomFavorite)

    @Insert
    fun insert(favoriteList: List<RoomFavorite>)

    @Update
    fun update(favorite: RoomFavorite)

    @Update
    fun update(favoriteList: List<RoomFavorite>)

    @Delete
    fun delete(favorite: RoomFavorite)

    @Delete
    fun delete(favoriteList: List<RoomFavorite>)

    @Query("SELECT EXISTS(SELECT id FROM favorites WHERE id = :id)")
    fun isTitleExists(id: Int): Boolean

    @Query("SELECT * FROM favorites")
    fun getAll(): List<RoomFavorite>

    @Query("SELECT * FROM favorites WHERE id = :id LIMIT 1")
    fun findById(id: Int): RoomFavorite
}