package com.example.weatherapp.mvp.presenter

import com.example.weatherapp.mvp.view.IMainView

interface IMainPresenter {
    fun attachView(view: IMainView)
    fun detachView()
    fun onClick(request: String)
    fun onClickFavorite(request: String)
    fun onFavoriteIconClick()
    fun getFavoritesPresenter(): IFavoritesPresenter
    fun setRecyclerData()
}