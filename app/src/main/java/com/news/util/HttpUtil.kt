package com.news.util

import okhttp3.OkHttpClient
import okhttp3.Request

object HttpUtil {
    fun sendOkHttpRequest(address: String, callback: okhttp3.Callback) {
        OkHttpClient().newCall(Request.Builder().url(address).build()).enqueue(callback)
    }
}
