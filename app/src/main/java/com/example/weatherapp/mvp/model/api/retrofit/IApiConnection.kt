package com.example.weatherapp.mvp.model.api.retrofit

import com.example.weatherapp.mvp.model.api.DataRequest
import retrofit2.Call

interface IApiConnection {
    fun loadWeatherByCity(request: String, langCode: String): Call<DataRequest>
    fun loadWeatherById(cityId: Int, langCode: String): Call<DataRequest>
}