package com.news

import com.google.gson.annotations.SerializedName

open class NewsBean {
    @SerializedName("newslist") // 字段冲突要注解修正
    lateinit var newsList: List<News>

    data class News(
            var title: String,
            var description: String,
            var ctime: String,
            var picUrl: String,
            var url: String
    )
}
