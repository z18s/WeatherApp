package com.example.weatherapp.mvp.model.tools

import com.example.weatherapp.mvp.model.api.data.Main
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

private const val ABSOLUTE_ZERO_TEMP = -273.15

fun Main.tempCelsius(tempKelvin: Double): Double = ABSOLUTE_ZERO_TEMP.plus(tempKelvin).times(10).roundToInt().div(10.0)
fun Main.tempSign(tempCelsius: Double): Char = if (tempCelsius.roundToInt() >= 0) '+' else '–'

fun Main.tempCelsiusToString(tempKelvin: Double?): String = tempKelvin?.let {
    val temp = tempCelsius(it)
    "${tempSign(temp)}${temp.absoluteValue} ℃"
} ?: "null"