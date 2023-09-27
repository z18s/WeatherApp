package com.example.weatherapp.mvp.presenter

import com.example.weatherapp.mvp.view.IMainView

interface IMainPresenter {
    fun attachView(view: IMainView)
    fun detachView()
    fun onSearchClick(request: String)
    fun onFavoriteIconClick()
    fun update()
}