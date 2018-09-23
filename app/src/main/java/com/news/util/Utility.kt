package com.news.util

import com.google.gson.Gson
import com.news.gson.NewsList

/**
 * Created by len_titude on 2017/5/4.
 */

object Utility {
    fun parseJsonWithGson(requestText: String): NewsList {
        val gson = Gson()
        return gson.fromJson(requestText, NewsList::class.java)
    }

}
