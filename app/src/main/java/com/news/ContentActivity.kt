package com.news

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.news.NewsAdapter.Companion.PIC
import com.news.NewsAdapter.Companion.TITLE
import com.news.NewsAdapter.Companion.URL
import kotlinx.android.synthetic.main.activity_content.*

class ContentActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
        actionBar.setDisplayShowTitleEnabled(true)
        webView!!.settings.javaScriptEnabled = true
        webView!!.webViewClient = WebViewClient()
        actionBar.title = intent.getStringExtra(TITLE)
        webView!!.loadUrl(intent.getStringExtra(URL))
        Glide.with(this).load(intent.getStringExtra(PIC)).into(bigPic)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { //点击返回键做了处理
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}
