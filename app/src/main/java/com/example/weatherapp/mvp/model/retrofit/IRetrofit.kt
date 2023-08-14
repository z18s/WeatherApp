package com.example.weatherapp.mvp.model.retrofit

import com.example.weatherapp.mvp.model.DataRequest
import retrofit2.Call

interface IRetrofit {
    fun loadWeatherByCity(request: String, lang: String): Call<DataRequest>
}