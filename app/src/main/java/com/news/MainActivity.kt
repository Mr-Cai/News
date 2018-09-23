package com.news

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.news.gson.NewsList
import okhttp3.*
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {
    private val titleList = ArrayList<Title>()
    private var listView: ListView? = null
    private var adapter: TitleAdapter? = null
    private var navigationView: NavigationView? = null
    private var drawerLayout: DrawerLayout? = null
    private var refreshLayout: SwipeRefreshLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        assert(actionBar != null)
        actionBar!!.setDisplayShowTitleEnabled(true)
        actionBar.title = "社会新闻"
        refreshLayout = findViewById(R.id.swipe_layout)
        listView = findViewById(R.id.list_view)
        adapter = TitleAdapter(this, R.layout.list_view_item, titleList)
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

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        navigationView!!.setCheckedItem(R.id.nav_society)
        navigationView!!.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_society -> handleCurrentPage("社会新闻", ITEM_SOCIETY)
                R.id.nav_county -> handleCurrentPage("国内新闻", ITEM_COUNTY)
                R.id.nav_internation -> handleCurrentPage("国际新闻", ITEM_INTERNATION)
                R.id.nav_fun -> handleCurrentPage("娱乐新闻", ITEM_FUN)
                R.id.nav_sport -> handleCurrentPage("体育新闻", ITEM_SPORT)
                R.id.nav_looker -> handleCurrentPage("美女图片", ITEM_LOOKER)
                else -> {
                }
            }
            drawerLayout!!.closeDrawers()
            true
        }

        refreshLayout!!.setOnRefreshListener {
            refreshLayout!!.isRefreshing = true
            val itemName = parseString((actionBar.title as String?)!!)
            requestNew(itemName)
        }

        requestNew(ITEM_SOCIETY)

    }

    /**
     * 判断是否是当前页面,如果不是则 请求处理数据
     */
    private fun handleCurrentPage(text: String, item: String) {
        val actionBar = supportActionBar
        if (text != actionBar!!.title!!.toString()) {
            actionBar.title = text
            requestNew(item)
            refreshLayout!!.isRefreshing = true
        }
    }


    /**
     * 请求处理数据
     */
    fun requestNew(itemName: String) { // 根据返回到的 URL 链接进行申请和返回数据
        val address = response(itemName)    // key
        OkHttpClient().newCall(Request.Builder().url(address).build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Snackbar.make(listView!!, e.toString(), 2000).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseText = response.body()!!.string()
                val newsList = Gson().fromJson(responseText, NewsList::class.java)
                val code = newsList.code
                if (code == 200) {
                    titleList.clear()
                    for (news in newsList.newsList!!) {
                        val title = Title(news.title!!, news.description!!, news.picUrl!!, news.url!!)
                        titleList.add(title)
                    }

                    runOnUiThread {
                        adapter!!.notifyDataSetChanged()
                        listView!!.setSelection(0)
                        refreshLayout!!.isRefreshing = false
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "数据错误返回", Toast.LENGTH_SHORT).show()
                        refreshLayout!!.isRefreshing = false
                    }
                }
            }
        })
    }

    /**
     * 输入不同的类型选项，返回对应的 URL 链接
     */
    private fun response(itemName: String): String {
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
