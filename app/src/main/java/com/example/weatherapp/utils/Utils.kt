package com.example.weatherapp.utils

fun Any.toTag(): String = this::class.java.simpleName

inline fun <reified T : Any> T.logger(tag: String? = null) = LoggerProperty<T>(tag)