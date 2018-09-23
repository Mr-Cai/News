package com.news.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsList {

    public int code;

    public String msg;

    @SerializedName("newslist")//json数组名需要序列化
    public List<News> newsList;

}
