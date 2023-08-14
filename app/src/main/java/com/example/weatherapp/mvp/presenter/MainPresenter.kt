package com.example.weatherapp.mvp.presenter

import com.example.weatherapp.utils.logger
import com.example.weatherapp.mvp.model.DataRequest
import com.example.weatherapp.mvp.model.retrofit.IRetrofit
import com.example.weatherapp.mvp.view.IMainView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class MainPresenter(private var retrofit: IRetrofit) : IMainPresenter {

    private val logger by logger()

    private var view: IMainView? = null

    override fun onClick(request: String) = getWeatherByCity(request)

    private fun getWeatherByCity(request: String) =
        retrofit.loadWeatherByCity(request).enqueue(object : Callback<DataRequest> {
            override fun onResponse(call: Call<DataRequest>, response: Response<DataRequest>) {
                view?.apply {
                    if (response.isSuccessful) {
                        val city = response.body()?.name.toString()
                        val country = response.body()?.sys?.country.toString()
                        val temperature = response.body()?.main?.temp
                            ?.minus(273.15)?.times(10)?.roundToInt()?.div(10.0f).toString()
                        val result = "$city, $country: $temperature ℃".also { logger.log(it) }
                        setText(result)
                    } else {
                        val msg = response.code().toString()
                        logger.log(msg)
                        showToast(msg)
                    }
                }
            }

            override fun onFailure(call: Call<DataRequest>, t: Throwable) {
                val msg = "getWeatherByCity.onFailure"
                logger.log(msg, 5, t)
                view?.showToast(t.message ?: msg)
            }
        })

    override fun attachView(view: IMainView) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }
}