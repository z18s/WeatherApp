package com.example.weatherapp.application

import android.app.Application
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.mvp.model.api.retrofit.IDataSource
import com.example.weatherapp.mvp.model.database.room.AppDatabase
import com.example.weatherapp.utils.logger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    private val logger by logger()

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    val dataSource: IDataSource = Retrofit.Builder()
        .baseUrl(BuildConfig.BaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(IDataSource::class.java)
        .also { logger.log("dataSource = $it") }

    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
            .also { logger.log("database = $it") }
    }

    companion object {

        @Volatile private var INSTANCE: App? = null

        fun getInstance(): App = INSTANCE ?: synchronized(this) {
            App().also { INSTANCE = it }
        }
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this.also { logger.log("app = $it") }
    }
}