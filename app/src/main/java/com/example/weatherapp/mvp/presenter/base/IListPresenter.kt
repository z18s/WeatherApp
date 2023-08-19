package com.example.weatherapp.mvp.presenter.base

import com.example.weatherapp.mvp.view.base.IItemView

interface IListPresenter<in V: IItemView> {
    fun onItemClick(view: V)
    fun bindView(view: V)
    fun getCount(): Int
}