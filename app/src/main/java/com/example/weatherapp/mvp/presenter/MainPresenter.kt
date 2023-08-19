package com.example.weatherapp.mvp.presenter

import android.os.Handler
import android.os.Looper
import com.example.weatherapp.mvp.model.api.DataRequest
import com.example.weatherapp.mvp.model.api.data.Sys
import com.example.weatherapp.mvp.model.api.retrofit.IApiConnection
import com.example.weatherapp.mvp.model.database.data.RoomFavorite
import com.example.weatherapp.mvp.model.database.room.IDatabaseConnection
import com.example.weatherapp.mvp.model.tools.Language
import com.example.weatherapp.mvp.model.tools.tempCelsiusToString
import com.example.weatherapp.mvp.view.IMainView
import com.example.weatherapp.utils.logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPresenter(private val api: IApiConnection, private val db: IDatabaseConnection) : IMainPresenter {

    private val logger by logger()
    private var language = Language.Russian

    private var view: IMainView? = null
    private val favoritesPresenter: IFavoritesPresenter = FavoritesPresenter(this)

    private var lastQuery: RoomFavorite? = null

    override fun attachView(view: IMainView) {
        this.view = view
    }

    override fun onClick(request: String) = getWeatherByCity(request)

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
        api.loadWeatherByCity(request, language.code).enqueue(object : Callback<DataRequest> {
            override fun onResponse(call: Call<DataRequest>, response: Response<DataRequest>) {
                view?.apply {
                    if (response.isSuccessful) {
                        val data = response.body()
                        val id = data?.id
                        val city = data?.name
                        val country = data?.sys?.country
                        val temperature = data?.main?.run { tempCelsiusToString(temp) }

                        val result = "$city ($country): $temperature"
                        if (id != null && city != null && country != null) {
                            lastQuery = RoomFavorite(id, city, country)
                                .also { logger.log("lastQuery = $result") }
                        }
                        setText(result)

                        val state = id?.let { isCityFavorite(it) }
                        setFavoriteState(state)
                    } else {
                        val msg = "${response.code()}: ${response.message()}"
                        logger.log(msg)
                        showToast(msg)
                    }
                }
            }

            override fun onFailure(call: Call<DataRequest>, t: Throwable) {
                val msg = "getWeatherByCity.onFailure"
                logger.log(msg, 5, t)
                view?.showToast(t.message ?: msg)
            }
        })

    private fun getFavoritesList(): MutableList<DataRequest> {
        val roomFavorites = Handler(Looper.getMainLooper())
            .runCatching { db.getFavorites() }
            .getOrDefault(listOf())
        val favorites = mutableListOf<DataRequest>()
        roomFavorites.forEach {
            favorites.add(DataRequest(name = it.city, sys = Sys(id = it.id, country = it.country)))
        }
        return favorites
    }

//    private fun getFavorite(id: Int): DataRequest {
//        val roomFavorite = Handler(Looper.getMainLooper())
//            .runCatching { db.getFavoriteById(id) }
//            .getOrNull()
//        return DataRequest(name = roomFavorite?.city, sys = Sys(id = roomFavorite?.id, country = roomFavorite?.country))
//    }

    private fun isCityFavorite(id: Int): Boolean = Handler(Looper.getMainLooper())
        .runCatching { db.isFavorite(id) }
        .getOrDefault(false)

    private fun addFavorite(favorite: RoomFavorite) = Handler(Looper.getMainLooper())
        .run { db.insertFavorite(favorite) }

    private fun deleteFavorite(favorite: RoomFavorite) = Handler(Looper.getMainLooper())
        .run { db.deleteFavorite(favorite) }

    private fun setFavoriteState(state: Boolean?) =
        state?.let { view?.setFavoriteIcon(it) } ?: let { view?.setFavoriteIcon(false) }

    override fun setRecyclerData() {
        favoritesPresenter.favorites.clear()
        favoritesPresenter.favorites = getFavoritesList()
        view?.updateRecyclerData()
    }

    override fun detachView() {
        view = null
    }
}