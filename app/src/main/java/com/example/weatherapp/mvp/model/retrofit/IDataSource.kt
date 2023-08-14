package com.example.weatherapp.mvp.model.retrofit

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.mvp.model.DataRequest
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IDataSource {
    @GET("data/2.5/weather")
    fun loadWeatherByCity(
        @Query("q") city: String,
        @Query("lang") lang: String,
        @Query("appid") apiKey: String = BuildConfig.ApiKey
    ): Call<DataRequest>
}