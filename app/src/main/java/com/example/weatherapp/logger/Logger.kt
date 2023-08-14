package com.example.weatherapp.logger

import android.util.Log
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class Logger(thisRef: Any, tag: String?) {

    enum class LoggerLevel(val priority: Int) {
        VERBOSE(2), DEBUG(3), INFO(4), WARN(5), ERROR(6)
    }

    private val tag = tag ?: thisRef.toTag()

    fun log(message: String, priority: Int = 2, t: Throwable? = null) {
        when (priority) {
            LoggerLevel.VERBOSE.priority -> Log.v(tag, message, t)
            LoggerLevel.DEBUG.priority -> Log.d(tag, message, t)
            LoggerLevel.INFO.priority -> Log.i(tag, message, t)
            LoggerLevel.WARN.priority -> Log.w(tag, message, t)
            LoggerLevel.ERROR.priority -> Log.e(tag, message, t)
            else -> Log.v(tag, message, t)
        }
    }

    private fun Any.toTag(): String = this::class.java.simpleName
}

inline fun <reified T : Any> T.logger(tag: String? = null) = LoggerProperty<T>(tag)

class LoggerProperty<T : Any>(private val tag: String? = null) : ReadOnlyProperty<T, Logger> {

    @Volatile
    var logger: Logger? = null

    override fun getValue(thisRef: T, property: KProperty<*>): Logger {
        logger?.let { return it }
        logger = Logger(thisRef, tag)
        return logger as Logger
    }
}