package com.news

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class ContentActivity : AppCompatActivity() {
    private var webView: WebView? = null


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
        }

        webView = findViewById(R.id.web_view)
        webView!!.settings.javaScriptEnabled = true
        webView!!.webViewClient = WebViewClient()

        val uri = intent.getStringExtra("uri")
        val title = intent.getStringExtra("title")
        assert(actionBar != null)
        actionBar!!.setDisplayShowTitleEnabled(true)
        actionBar.title = title
        webView!!.loadUrl(uri)

    }

    /**
     * 点击返回键做了处理
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}
