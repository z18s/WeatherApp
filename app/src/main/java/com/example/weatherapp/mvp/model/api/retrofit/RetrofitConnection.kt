package com.example.weatherapp.mvp.model.api.retrofit

import com.example.weatherapp.mvp.model.api.DataRequest
import retrofit2.Call

class RetrofitConnection(private val dataSource: IDataSource) : IApiConnection {

    override fun loadWeatherByCity(request: String, langCode: String): Call<DataRequest> =
        dataSource.loadWeatherByCity(request, langCode)

    override fun loadWeatherById(cityId: Int, langCode: String): Call<DataRequest> =
        dataSource.loadWeatherById(cityId, langCode)
}