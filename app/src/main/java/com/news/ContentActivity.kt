package com.news

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
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
        webView!!.settings.javaScriptEnabled = true
        webView!!.webViewClient = WebViewClient()
        val uri = intent.getStringExtra("uri")
        val title = intent.getStringExtra("titleTxT")
        actionBar.setDisplayShowTitleEnabled(true)
        actionBar.title = title
        webView!!.loadUrl(uri)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { //点击返回键做了处理
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}
