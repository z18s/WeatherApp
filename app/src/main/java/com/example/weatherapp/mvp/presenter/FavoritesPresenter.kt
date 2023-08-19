package com.example.weatherapp.mvp.presenter

import com.example.weatherapp.mvp.model.api.DataRequest
import com.example.weatherapp.mvp.view.IFavoritesItemView

class FavoritesPresenter(private val mainPresenter: IMainPresenter) : IFavoritesPresenter {

    override var favorites = mutableListOf<DataRequest>()

    override fun onItemClick(view: IFavoritesItemView) {
        val index: Int = view.getPos()
        favorites[index].name?.let { mainPresenter.onClickFavorite(it) }
    }

    override fun bindView(view: IFavoritesItemView) {
        val index: Int = view.getPos()
        setData(view, favorites[index])
    }

    private fun setData(view: IFavoritesItemView, data: DataRequest) {
        data.name?.let { view.setCity(it) }
        data.sys?.country?.let { view.setCountry(it) }
    }

    override fun getCount(): Int = favorites.size
}