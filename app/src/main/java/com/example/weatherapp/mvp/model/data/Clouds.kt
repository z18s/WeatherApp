package com.example.weatherapp.mvp.model.data

import com.google.gson.annotations.SerializedName

data class Clouds (
    @SerializedName("all" ) var all : Int? = null
)