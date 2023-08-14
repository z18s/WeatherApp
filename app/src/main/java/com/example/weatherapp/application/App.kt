package com.example.weatherapp.application

import android.app.Application
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.mvp.model.retrofit.IDataSource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    val dataSource: IDataSource = Retrofit.Builder()
        .baseUrl(BuildConfig.BaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(IDataSource::class.java)

    companion object {

        @Volatile
        private var instance: App? = null

        val INSTANCE = instance ?: synchronized(this) {
            instance ?: App().also { instance = it }
        }
    }
}