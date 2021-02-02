package com.kekadoc.tools.android.log

import android.util.Log

typealias LogData<Data> = (it: Data) -> String

fun <T> T.log(): Logger<T> = Logger(this)
fun <T> T.log(logData: ((it: T) -> String)): Logger<T> = Logger(this, logData)

class Logger <T> constructor(private val data: T, val logData: ((it: T) -> String)? = null) {

    companion object {
        private const val TAG: String = "Logger-TAG"
    }

    fun v(tag: String? = TAG, msg: String? = null, tr: Throwable? = null): Int {
        return if (tr == null) Log.v(tag, create(msg, data))
        else Log.v(tag, create(msg, data), tr)
    }
    fun d(tag: String? = TAG, msg: String? = null, tr: Throwable?): Int {
        return if (tr == null) Log.d(tag, create(msg, data))
        else Log.d(tag, create(msg, data), tr)
    }
    fun i(tag: String? = TAG, msg: String? = null, tr: Throwable?): Int {
        return if (tr == null) Log.i(tag, create(msg, data))
        else Log.i(tag, create(msg, data), tr)
    }
    fun w(tag: String? = TAG, msg: String? = null, tr: Throwable? = null): Int {
        return if (tr == null) Log.w(tag, create(msg, data))
        else Log.w(tag, create(msg, data), tr)
    }
    fun e(tag: String? = TAG, msg: String? = null, tr: Throwable? = null): Int {
        return if (tr == null) Log.e(tag, create(msg, data))
        else Log.e(tag, create(msg, data), tr)
    }

    private fun create(msg: String?, data: T): String {
        val str = logData?.invoke(data) ?: data.toString()
        return if (msg == null) str
        else msg + str
    }

}