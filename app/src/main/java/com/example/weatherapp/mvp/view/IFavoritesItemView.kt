package com.example.weatherapp.mvp.view

import com.example.weatherapp.mvp.view.base.IItemView

interface IFavoritesItemView : IItemView {
    fun setCity(cityName: String)
    fun setCountry(countryName: String)
}