package com.example.weatherapp.mvp.model.api.retrofit

import com.example.weatherapp.mvp.model.api.DataRequest
import io.reactivex.rxjava3.core.Single

interface IApiConnection {
    fun loadWeatherByCity(request: String, langCode: String): Single<DataRequest>
    fun loadWeatherById(cityId: Int, langCode: String): Single<DataRequest>
}