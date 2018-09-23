package com.news

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {
    private val titleList = ArrayList<Title>()
    lateinit var adapter: TitleAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu)
        actionBar.setDisplayShowTitleEnabled(true)
        actionBar.title = ITEM_SOCIETY
        adapter = TitleAdapter(this, R.layout.news_item, titleList)
        listView!!.adapter = adapter
        listView!!.onItemClickListener = object : AdapterView.OnItemClickListener {
            var intent = Intent(this@MainActivity, ContentActivity::class.java)
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val title = titleList[position]
                intent.putExtra("title", actionBar.title)
                intent.putExtra("uri", title.uri)
                startActivity(intent)
            }
        }
        navigationView.setCheckedItem(R.id.nav_society)
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_society -> handleCurrentPage(ITEM_SOCIETY, ITEM_SOCIETY)
                R.id.nav_county -> handleCurrentPage(ITEM_COUNTY, ITEM_COUNTY)
                R.id.nav_internation -> handleCurrentPage(ITEM_INTERNATION, ITEM_INTERNATION)
                R.id.nav_fun -> handleCurrentPage(ITEM_FUN, ITEM_FUN)
                R.id.nav_sport -> handleCurrentPage(ITEM_SPORT, ITEM_SPORT)
                R.id.nav_looker -> handleCurrentPage(ITEM_LOOKER, ITEM_LOOKER)
            }
            drawerLayout.closeDrawers()
            true
        }

        refreshLayout.setOnRefreshListener {
            refreshLayout.isRefreshing = true
            val itemName = parseString((actionBar.title as String?)!!)
            requestData(itemName)
        }

        requestData(ITEM_SOCIETY)
    }

    private fun handleCurrentPage(text: String, item: String) { //判断是否是当前页面,如果不是则 请求处理数据
        val actionBar = supportActionBar
        if (text != actionBar!!.title!!.toString()) {
            actionBar.title = text
            requestData(item)
            refreshLayout!!.isRefreshing = true
        }
    }

    private fun requestData(itemName: String) { //请求处理数据 根据返回到的 URL 链接进行申请和返回数据
        OkHttpClient().newCall(Request.Builder().url(response(itemName)).build())
                .enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Snackbar.make(listView!!, e.toString(), 2000).show()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val responseText = response.body()!!.string()
                        val newsList = Gson().fromJson(responseText, NewsList::class.java)
                        titleList.clear()
                        for (news in newsList.newsList!!) {
                            val title = Title(news.title!!, news.description!!, news.picUrl!!, news.url!!)
                            titleList.add(title)
                        }
                        runOnUiThread {
                            adapter.notifyDataSetChanged()
                            listView!!.setSelection(0)
                            refreshLayout!!.isRefreshing = false
                        }
                    }
                })
    }

    private fun response(itemName: String): String { // 输入不同的类型选项，返回对应的 URL 链接
        val prefix = "https://api.tianapi.com/"
        val suffix = "/?key=d4f074e5a8866ff604bd1ee6b981eb96&num=20"
        var address = ""
        when (itemName) {
            ITEM_SOCIETY -> address = "${prefix}social$suffix"
            ITEM_COUNTY -> address = "${prefix}guonei$suffix"
            ITEM_INTERNATION -> address = "${prefix}world$suffix"
            ITEM_FUN -> address = "${prefix}huabian$suffix"
            ITEM_SPORT -> address = "${prefix}tiyu$suffix"
            ITEM_LOOKER -> address = "${prefix}meinv$suffix"
        }
        return address
    }

    private fun parseString(text: String): String { //通过 actionbar.getTitle() 的参数，返回对应的 ItemName
        when (text) {
            ITEM_SOCIETY -> ITEM_SOCIETY
            ITEM_COUNTY -> ITEM_COUNTY
            ITEM_INTERNATION -> ITEM_INTERNATION
            ITEM_FUN -> ITEM_FUN
            ITEM_SPORT -> ITEM_SPORT
            ITEM_LOOKER -> ITEM_LOOKER
        }
        return text
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { //对侧边栏按钮进行处理，打开侧边栏
        when (item.itemId) {
            android.R.id.home -> drawerLayout!!.openDrawer(GravityCompat.START)
        }
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) { //对返回键进行处理，如果侧边栏打开则关闭侧边栏，否则关闭 activity
            drawerLayout!!.closeDrawers()
        } else {
            finish()
        }
    }

    companion object {
        const val ITEM_SOCIETY = "社会新闻"
        const val ITEM_COUNTY = "国内新闻"
        const val ITEM_INTERNATION = "国际新闻"
        const val ITEM_FUN = "娱乐新闻"
        const val ITEM_SPORT = "体育新闻"
        const val ITEM_LOOKER = "美女图片"
    }
}
