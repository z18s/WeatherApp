package com.example.weatherapp.mvp.view

interface IMainView {
    fun setSearchText(text: String)
    fun setWeatherText(text: String)
    fun setFavoriteIcon(state: Boolean)
    fun setFavoritesList(list: List<Pair<String, String>>)
    fun showToast(text: String)
}