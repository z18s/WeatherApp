package com.example.weatherapp.mvp.presenter

import com.example.weatherapp.mvp.view.IMainView

class MainPresenter : IMainPresenter {

    private var view: IMainView? = null

    override fun attachView(view: IMainView) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }
}