package com.example.weatherapp.application

import android.app.Application

class App : Application() {

    companion object {

        @Volatile
        private var instance: App? = null

        val INSTANCE = instance ?: synchronized(this) {
            instance ?: App().also { instance = it }
        }
    }
}