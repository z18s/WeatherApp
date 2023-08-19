package com.example.weatherapp.mvp.view

interface IMainView {
    fun setText(text: String)
    fun setSearchText(text: String)
    fun setFavoriteIcon(state: Boolean)
    fun updateRecyclerData()
    fun showToast(text: String)
}