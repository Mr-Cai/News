package com.news

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast

import com.google.android.material.navigation.NavigationView
import com.news.gson.News
import com.news.gson.NewsList
import com.news.util.HttpUtil
import com.news.util.Utility

import java.io.IOException
import java.util.ArrayList

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response

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
        refreshLayout!!.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        listView = findViewById(R.id.list_view)
        adapter = TitleAdapter(this, R.layout.list_view_item, titleList)
        listView!!.adapter = adapter
        listView!!.onItemClickListener = object : AdapterView.OnItemClickListener {
            internal var intent = Intent(this@MainActivity, ContentActivity::class.java)

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
                R.id.nav_nba -> handleCurrentPage("NBA新闻", ITEM_NBA)
                R.id.nav_football -> handleCurrentPage("足球新闻", ITEM_FOOTBALL)
                R.id.nav_technology -> handleCurrentPage("科技新闻", ITEM_TECHNOLOGY)
                R.id.nav_work -> handleCurrentPage("创业新闻", ITEM_WORK)
                R.id.nav_apple -> handleCurrentPage("苹果新闻", ITEM_APPLE)
                R.id.nav_war -> handleCurrentPage("军事新闻", ITEM_WAR)
                R.id.nav_internet -> handleCurrentPage("移动互联", ITEM_INTERNET)
                R.id.nav_travel -> handleCurrentPage("旅游资讯", ITEM_TREVAL)
                R.id.nav_health -> handleCurrentPage("健康知识", ITEM_HEALTH)
                R.id.nav_strange -> handleCurrentPage("奇闻异事", ITEM_STRANGE)
                R.id.nav_looker -> handleCurrentPage("美女图片", ITEM_LOOKER)
                R.id.nav_vr -> handleCurrentPage("VR科技", ITEM_VR)
                R.id.nav_it -> handleCurrentPage("IT资讯", ITEM_IT)
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
    private fun handleCurrentPage(text: String, item: Int) {
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
    fun requestNew(itemName: Int) {

        // 根据返回到的 URL 链接进行申请和返回数据
        val address = response(itemName)    // key
        HttpUtil.sendOkHttpRequest(address, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { Toast.makeText(this@MainActivity, "新闻加载失败", Toast.LENGTH_SHORT).show() }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val responseText = response.body()!!.string()
                val newlist = Utility.parseJsonWithGson(responseText)
                val code = newlist.code
                val msg = newlist.msg
                if (code == 200) {
                    titleList.clear()
                    for (news in newlist.newsList!!) {
                        val title = Title(news.title, news.description, news.picUrl, news.url)
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
    private fun response(itemName: Int): String {
        var address = "https://api.tianapi.com/social/?key=d4f074e5a8866ff604bd1ee6b981eb96&num=20"
        when (itemName) {
            ITEM_SOCIETY -> {
            }
            ITEM_COUNTY -> address = address.replace("social".toRegex(), "guonei")
            ITEM_INTERNATION -> address = address.replace("social".toRegex(), "world")
            ITEM_FUN -> address = address.replace("social".toRegex(), "huabian")
            ITEM_SPORT -> address = address.replace("social".toRegex(), "tiyu")
            ITEM_NBA -> address = address.replace("social".toRegex(), "nba")
            ITEM_FOOTBALL -> address = address.replace("social".toRegex(), "football")
            ITEM_TECHNOLOGY -> address = address.replace("social".toRegex(), "keji")
            ITEM_WORK -> address = address.replace("social".toRegex(), "startup")
            ITEM_APPLE -> address = address.replace("social".toRegex(), "apple")
            ITEM_WAR -> address = address.replace("social".toRegex(), "military")
            ITEM_INTERNET -> address = address.replace("social".toRegex(), "mobile")
            ITEM_TREVAL -> address = address.replace("social".toRegex(), "travel")
            ITEM_HEALTH -> address = address.replace("social".toRegex(), "health")
            ITEM_STRANGE -> address = address.replace("social".toRegex(), "qiwen")
            ITEM_LOOKER -> address = address.replace("social".toRegex(), "meinv")
            ITEM_VR -> address = address.replace("social".toRegex(), "vr")
            ITEM_IT -> address = address.replace("social".toRegex(), "it")
        }
        return address
    }

    private fun parseString(text: String): Int { //通过 actionbar.getTitle() 的参数，返回对应的 ItemName
        if (text == "社会新闻") {
            return ITEM_SOCIETY
        }
        if (text == "国内新闻") {
            return ITEM_COUNTY
        }
        if (text == "国际新闻") {
            return ITEM_INTERNATION
        }
        if (text == "娱乐新闻") {
            return ITEM_FUN
        }
        if (text == "体育新闻") {
            return ITEM_SPORT
        }
        if (text == "NBA新闻") {
            return ITEM_NBA
        }
        if (text == "足球新闻") {
            return ITEM_FOOTBALL
        }
        if (text == "科技新闻") {
            return ITEM_TECHNOLOGY
        }
        if (text == "创业新闻") {
            return ITEM_WORK
        }
        if (text == "苹果新闻") {
            return ITEM_APPLE
        }
        if (text == "军事新闻") {
            return ITEM_WAR
        }
        if (text == "移动互联") {
            return ITEM_INTERNET
        }
        if (text == "旅游资讯") {
            return ITEM_TREVAL
        }
        if (text == "健康知识") {
            return ITEM_HEALTH
        }
        if (text == "奇闻异事") {
            return ITEM_STRANGE
        }
        if (text == "美女图片") {
            return ITEM_LOOKER
        }
        if (text == "VR科技") {
            return ITEM_VR
        }
        return if (text == "IT资讯") {
            ITEM_IT
        } else ITEM_SOCIETY
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
        private val ITEM_SOCIETY = 1
        private val ITEM_COUNTY = 2
        private val ITEM_INTERNATION = 3
        private val ITEM_FUN = 4
        private val ITEM_SPORT = 5
        private val ITEM_NBA = 6
        private val ITEM_FOOTBALL = 7
        private val ITEM_TECHNOLOGY = 8
        private val ITEM_WORK = 9
        private val ITEM_APPLE = 10
        private val ITEM_WAR = 11
        private val ITEM_INTERNET = 12
        private val ITEM_TREVAL = 13
        private val ITEM_HEALTH = 14
        private val ITEM_STRANGE = 15
        private val ITEM_LOOKER = 16
        private val ITEM_VR = 17
        private val ITEM_IT = 18
    }
}
