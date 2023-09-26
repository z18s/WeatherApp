package com.example.weatherapp.mvp.view

import androidx.compose.runtime.Composable

interface IMainView {
    fun setSearchText(text: String)
    fun setWeatherData(city: String, weather: String, icon: Pair<String, String>)
    fun setFavoriteIcon(state: Boolean)
    fun setFavoritesList(list: List<Pair<String, String>>)
    fun showToast(text: String)
}