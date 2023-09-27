package com.example.weatherapp.mvp.view

interface IMainView {
    fun setWeatherData(city: String, weather: String, icon: Pair<String, String>)
    fun setFavoriteIcon(state: Boolean)
    fun setFavoritesList(list: List<Pair<String, String>>)
    fun showToast(text: String)
}