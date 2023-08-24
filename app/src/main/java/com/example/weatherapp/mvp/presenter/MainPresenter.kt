package com.example.weatherapp.mvp.presenter

import android.annotation.SuppressLint
import com.example.weatherapp.mvp.model.api.DataRequest
import com.example.weatherapp.mvp.model.api.data.Sys
import com.example.weatherapp.mvp.model.api.retrofit.IApiConnection
import com.example.weatherapp.mvp.model.database.data.RoomFavorite
import com.example.weatherapp.mvp.model.database.room.IDatabaseConnection
import com.example.weatherapp.mvp.model.tools.Language
import com.example.weatherapp.mvp.model.tools.tempCelsiusToString
import com.example.weatherapp.mvp.view.IMainView
import com.example.weatherapp.utils.logger
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

class MainPresenter(private val api: IApiConnection, private val db: IDatabaseConnection) : IMainPresenter {

    private val logger by logger()
    private var language = Language.Russian

    private var view: IMainView? = null
    private val favoritesPresenter: IFavoritesPresenter = FavoritesPresenter(this)

    private var lastQuery: RoomFavorite? = null

    override fun attachView(view: IMainView) {
        this.view = view
    }

    override fun onClick(request: String) {
        getWeatherByCity(request)
    }

    override fun onClickFavorite(request: String) {
        view?.setSearchText(request)
        onClick(request)
    }

    override fun onFavoriteIconClick() {
        lastQuery?.let {
            val state = isCityFavorite(lastQuery!!.id)
            if (!state) {
                addFavorite(it).run { logger.log("addFavorite(${it.city})") }
            } else {
                deleteFavorite(it).run { logger.log("deleteFavorite(${it.city})") }
            }
            setFavoriteState(!state)
        }
        setRecyclerData()
    }

    override fun getFavoritesPresenter(): IFavoritesPresenter = favoritesPresenter

    private fun getWeatherByCity(request: String) =
        api.loadWeatherByCity(request, language.code).observeOn(AndroidSchedulers.mainThread()).subscribe({ data ->
            view?.apply {
                val id = data.id
                val city = data.name
                val country = data.sys?.country
                val temperature = data.main?.let { it.tempCelsiusToString(it.temp) }

                val result = "$city ($country): $temperature"
                if (id != null && city != null && country != null) {
                    lastQuery = RoomFavorite(id, city, country)
                        .also { logger.log("lastQuery = $result") }
                }
                setText(result)

                val state = id?.let { isCityFavorite(it) }
                setFavoriteState(state)
            }
        }, { showError(it) })


    @SuppressLint("CheckResult")
    override fun setRecyclerData() {
        favoritesPresenter.favorites.clear()
        db.getFavorites().observeOn(AndroidSchedulers.mainThread()).subscribe({ list ->
            list.forEach {
                favoritesPresenter.favorites.add(DataRequest(name = it.city, sys = Sys(id = it.id, country = it.country)))
            }
            view?.updateRecyclerData()
        }, { showError(it) })
    }

    private fun isCityFavorite(id: Int): Boolean = db.isFavorite(id).blockingGet()

    private fun addFavorite(favorite: RoomFavorite) =
        db.insertFavorite(favorite).observeOn(AndroidSchedulers.mainThread()).subscribe { setRecyclerData() }

    private fun deleteFavorite(favorite: RoomFavorite) =
        db.deleteFavorite(favorite).observeOn(AndroidSchedulers.mainThread()).subscribe { setRecyclerData() }

    private fun setFavoriteState(state: Boolean?) =
        state?.let { view?.setFavoriteIcon(it) } ?: let { view?.setFavoriteIcon(false) }

    private fun showError(t: Throwable) {
        t.message?.let {
            logger.log(it)
            view?.showToast(it)
        }
    }

    override fun detachView() {
        view = null
    }
}