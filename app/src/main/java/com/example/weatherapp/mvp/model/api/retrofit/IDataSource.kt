package com.example.weatherapp.mvp.model.api.retrofit

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.mvp.model.api.DataRequest
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IDataSource {
    @GET("data/2.5/weather") fun loadWeatherByCity(
        @Query("q") request: String,
        @Query("lang") langCode: String,
        @Query("appid") apiKey: String = BuildConfig.ApiKey
    ): Call<DataRequest>

    @GET("data/2.5/weather") fun loadWeatherById(
        @Query("id") cityId: Int,
        @Query("lang") langCode: String,
        @Query("appid") apiKey: String = BuildConfig.ApiKey
    ): Call<DataRequest>
}