package com.example.weatherapp.mvp.presenter

import android.annotation.SuppressLint
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
    private var currentQuery: RoomFavorite? = null

    override fun attachView(view: IMainView) {
        this.view = view
    }

    override fun onSearchClick(request: String) {
        getWeatherByCity(request)
    }

    override fun onFavoriteIconClick() {
        currentQuery?.let {
            val state = isCityFavorite(it.id)
            if (!state) {
                addFavorite(it)
                    .subscribe { updateFavoritesList() }
                    .run { logger.log("addFavorite(${it.city})") }
            } else {
                deleteFavorite(it)
                    .subscribe { updateFavoritesList() }
                    .run { logger.log("deleteFavorite(${it.city})") }
            }
            setFavoriteState(!state)
        }
    }

    override fun update() {
        updateFavoritesList()
    }

    private fun getWeatherByCity(request: String) =
        api.loadWeatherByCity(request, language.code).observeOn(AndroidSchedulers.mainThread()).subscribe({ data ->
            view?.apply {
                val id = data.id
                val city = data.name
                val country = data.sys?.country
                val temp = data.main?.let { it.tempCelsiusToString(it.temp) }
                val iconName = data.weather.first().icon ?: ""
                val iconDescription = data.weather.first().description ?: ""

                val place = "$city ($country)"
                val weather = "$temp"
                if (id != null && city != null && country != null) {
                    currentQuery = RoomFavorite(id, city, country)
                        .also { logger.log("lastQuery = $place $weather") }
                }
                setWeatherData(place, weather, iconName to iconDescription)

                val state = id?.let { isCityFavorite(it) }
                setFavoriteState(state)
            }
        }, { showError(it) })

    @SuppressLint("CheckResult")
    private fun updateFavoritesList() {
        db.getFavorites().observeOn(AndroidSchedulers.mainThread()).subscribe({ list ->
            val pairList = mutableListOf<Pair<String, String>>()
            list.forEach {
                pairList.add(it.city to it.country)
            }
            view?.setFavoritesList(pairList)
        }, { showError(it) })
    }

    private fun isCityFavorite(id: Int): Boolean =
        db.isFavorite(id).blockingGet()

    private fun addFavorite(favorite: RoomFavorite) =
        db.insertFavorite(favorite).observeOn(AndroidSchedulers.mainThread())

    private fun deleteFavorite(favorite: RoomFavorite) =
        db.deleteFavorite(favorite).observeOn(AndroidSchedulers.mainThread())

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