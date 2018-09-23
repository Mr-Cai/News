package com.news

import com.google.gson.annotations.SerializedName

class NewsList {
    var code: Int = 0
    @SerializedName("newslist")//json数组名需要序列化
    var newsList: List<News>? = null
}
