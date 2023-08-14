package com.example.weatherapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherapp.R
import com.example.weatherapp.mvp.presenter.IMainPresenter
import com.example.weatherapp.mvp.presenter.MainPresenter
import com.example.weatherapp.mvp.view.IMainView

class MainActivity : AppCompatActivity(), IMainView {

    private lateinit var presenter: IMainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        presenter = MainPresenter()
        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}