package com.example.weatherapp.mvp.model.api.data

import com.google.gson.annotations.SerializedName

data class Clouds (
    @SerializedName("all" ) var all : Int? = null
)