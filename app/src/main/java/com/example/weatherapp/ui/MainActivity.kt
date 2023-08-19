package com.example.weatherapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.application.App
import com.example.weatherapp.mvp.model.api.retrofit.RetrofitConnection
import com.example.weatherapp.mvp.model.database.room.RoomConnection
import com.example.weatherapp.mvp.presenter.IMainPresenter
import com.example.weatherapp.mvp.presenter.MainPresenter
import com.example.weatherapp.mvp.view.IMainView
import com.example.weatherapp.ui.adapter.FavoritesAdapter
import com.example.weatherapp.utils.toTag

class MainActivity : AppCompatActivity(), IMainView {

    private lateinit var presenter: IMainPresenter

    private lateinit var sharedPref: SharedPreferences
    private val prefKey: String = "LAST_QUERY"
    private val prefDefaultString: String = ""

    private lateinit var etSearch: EditText
    private lateinit var btnSearch: Button
    private lateinit var tvMain: TextView
    private lateinit var ivMain: ImageView
    private lateinit var rvFavorites: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        etSearch = findViewById(R.id.et_search)
        btnSearch = findViewById(R.id.btn_search)
        tvMain = findViewById(R.id.tv_main)
        ivMain = findViewById(R.id.iv_main)
        rvFavorites = findViewById(R.id.rv_favorites)

        etSearch.setText(sharedPref.getString(prefKey, prefDefaultString) ?: prefDefaultString)

        rvFavorites.layoutManager = LinearLayoutManager(baseContext)
        rvFavorites.adapter = FavoritesAdapter(presenter.getFavoritesPresenter())
        presenter.setRecyclerData()
    }

    private fun initListeners() {
        etSearch.setOnKeyListener { _, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                val query = etSearch.text.toString()
                presenter.onClick(query)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        btnSearch.setOnClickListener {
            val query = etSearch.text.toString()
            presenter.onClick(query)
            sharedPref.edit().putString(prefKey, query).apply()
        }

        ivMain.setOnClickListener {
            presenter.onFavoriteIconClick()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(this.toTag(), tvMain.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun setText(text: String) {
        tvMain.text = text
    }

    override fun setSearchText(text: String) {
        etSearch.setText(text)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun setFavoriteIcon(state: Boolean) = if (state) {
        ivMain.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_true))
    } else {
        ivMain.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_false))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun updateRecyclerData() {
        rvFavorites.adapter?.notifyDataSetChanged()
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