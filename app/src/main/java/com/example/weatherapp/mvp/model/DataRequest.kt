package com.example.weatherapp.mvp.model

import com.example.weatherapp.mvp.model.data.Clouds
import com.example.weatherapp.mvp.model.data.Coord
import com.example.weatherapp.mvp.model.data.Main
import com.example.weatherapp.mvp.model.data.Sys
import com.example.weatherapp.mvp.model.data.Weather
import com.example.weatherapp.mvp.model.data.Wind
import com.google.gson.annotations.SerializedName

data class DataRequest (
    @SerializedName("coord"      ) var coord      : Coord?             = Coord(),
    @SerializedName("weather"    ) var weather    : ArrayList<Weather> = arrayListOf(),
    @SerializedName("base"       ) var base       : String?            = null,
    @SerializedName("main"       ) var main       : Main?              = Main(),
    @SerializedName("visibility" ) var visibility : Int?               = null,
    @SerializedName("wind"       ) var wind       : Wind?              = Wind(),
    @SerializedName("clouds"     ) var clouds     : Clouds?            = Clouds(),
    @SerializedName("dt"         ) var dt         : Int?               = null,
    @SerializedName("sys"        ) var sys        : Sys?               = Sys(),
    @SerializedName("timezone"   ) var timezone   : Int?               = null,
    @SerializedName("id"         ) var id         : Int?               = null,
    @SerializedName("name"       ) var name       : String?            = null,
    @SerializedName("cod"        ) var cod        : Int?               = null
)