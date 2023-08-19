package com.example.weatherapp.mvp.presenter

import com.example.weatherapp.mvp.model.api.DataRequest
import com.example.weatherapp.mvp.presenter.base.IListPresenter
import com.example.weatherapp.mvp.view.IFavoritesItemView

interface IFavoritesPresenter : IListPresenter<IFavoritesItemView> {
    var favorites: MutableList<DataRequest>
}