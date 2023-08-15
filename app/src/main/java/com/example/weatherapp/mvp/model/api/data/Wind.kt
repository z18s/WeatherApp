package com.example.weatherapp.mvp.model.api.data

import com.google.gson.annotations.SerializedName

data class Wind (
    @SerializedName("speed" ) var speed : Double? = null,
    @SerializedName("deg"   ) var deg   : Int?    = null
)