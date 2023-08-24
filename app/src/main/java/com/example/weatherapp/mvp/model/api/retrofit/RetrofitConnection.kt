package com.example.weatherapp.mvp.model.api.retrofit

import com.example.weatherapp.mvp.model.api.DataRequest
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class RetrofitConnection(private val dataSource: IDataSource) : IApiConnection {

    override fun loadWeatherByCity(request: String, langCode: String): Single<DataRequest> =
        dataSource.loadWeatherByCity(request, langCode).subscribeOn(Schedulers.io())

    override fun loadWeatherById(cityId: Int, langCode: String): Single<DataRequest> =
        dataSource.loadWeatherById(cityId, langCode).subscribeOn(Schedulers.io())
}