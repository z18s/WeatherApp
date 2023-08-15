package com.example.weatherapp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.R
import com.example.weatherapp.application.App
import com.example.weatherapp.mvp.model.retrofit.RetrofitConnection
import com.example.weatherapp.mvp.presenter.IMainPresenter
import com.example.weatherapp.mvp.presenter.MainPresenter
import com.example.weatherapp.mvp.view.IMainView
import com.example.weatherapp.utils.toTag

class MainActivity : AppCompatActivity(), IMainView {

    private lateinit var presenter: IMainPresenter

    private lateinit var tvMain: TextView
    private lateinit var etMain: EditText
    private lateinit var btnMain: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        presenter = MainPresenter(RetrofitConnection(App.INSTANCE.dataSource))
        presenter.attachView(this)

        tvMain = findViewById(R.id.tv_main)
        etMain = findViewById(R.id.et_main)
        btnMain = findViewById(R.id.btn_main)

        btnMain.setOnClickListener {
            presenter.onClick(etMain.text.toString())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(this.toTag(), tvMain.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun setText(text: String) {
        tvMain.text = text
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