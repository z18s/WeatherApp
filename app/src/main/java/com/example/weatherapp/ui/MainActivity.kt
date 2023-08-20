package com.example.weatherapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.application.App
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.mvp.model.api.retrofit.RetrofitConnection
import com.example.weatherapp.mvp.model.database.room.RoomConnection
import com.example.weatherapp.mvp.presenter.IMainPresenter
import com.example.weatherapp.mvp.presenter.MainPresenter
import com.example.weatherapp.mvp.view.IMainView
import com.example.weatherapp.ui.adapter.FavoritesAdapter
import com.example.weatherapp.utils.toTag

class MainActivity : AppCompatActivity(), IMainView {

    private lateinit var binding: ActivityMainBinding
    private lateinit var presenter: IMainPresenter

    private lateinit var sharedPref: SharedPreferences
    private val prefKey: String = "LAST_QUERY"
    private val prefDefaultString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        sharedPref = getPreferences(Context.MODE_PRIVATE)
        presenter = MainPresenter(RetrofitConnection(App.getInstance().dataSource), RoomConnection(App.getInstance().database))
        presenter.attachView(this)

        initView()
        initListeners()
    }

    private fun initView() {
        binding.etSearch.setText(sharedPref.getString(prefKey, prefDefaultString) ?: prefDefaultString)

        binding.rvFavorites.layoutManager = LinearLayoutManager(baseContext)
        binding.rvFavorites.adapter = FavoritesAdapter(presenter.getFavoritesPresenter())
        presenter.setRecyclerData()
    }

    private fun initListeners() {
        binding.etSearch.setOnKeyListener { _, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                val query = binding.etSearch.text.toString()
                presenter.onClick(query)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        binding.btnSearch.setOnClickListener {
            val query = binding.etSearch.text.toString()
            presenter.onClick(query)
            sharedPref.edit().putString(prefKey, query).apply()
        }

        binding.ivMain.setOnClickListener {
            presenter.onFavoriteIconClick()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(this.toTag(), binding.tvMain.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun setText(text: String) {
        binding.tvMain.text = text
    }

    override fun setSearchText(text: String) {
        binding.etSearch.setText(text)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun setFavoriteIcon(state: Boolean) = if (state) {
        binding.ivMain.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_true))
    } else {
        binding.ivMain.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_false))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun updateRecyclerData() {
        binding.rvFavorites.adapter?.notifyDataSetChanged()
    }

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        setText(savedInstanceState.getString(this.toTag(), ""))
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}