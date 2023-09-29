package com.example.weatherapp.mvp.view

interface IMainView {
    fun setWeatherData(city: String, weather: String, humidity: Int?, cloudy: Int?, icon: Pair<String, String>)
    fun setFavoriteIcon(state: Boolean)
    fun setFavoritesList(list: List<Pair<String, String>>)
    fun showToast(text: String)
}