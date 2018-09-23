package com.news.gson

import com.google.gson.annotations.SerializedName

class NewsList {

    var code: Int = 0

    var msg: String? = null

    @SerializedName("newslist")//json数组名需要序列化
    var newsList: List<News>? = null

}
