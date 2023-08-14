package com.example.weatherapp.mvp.model.retrofit

import com.example.weatherapp.mvp.model.DataRequest
import retrofit2.Call

class RetrofitConnection(private var dataSource: IDataSource): IRetrofit {

    override fun loadWeatherByCity(request: String, lang: String): Call<DataRequest> = dataSource.loadWeatherByCity(request, lang)
}