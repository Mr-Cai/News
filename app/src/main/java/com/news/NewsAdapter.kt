package com.news

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.news.NewsBean.News
import kotlinx.android.synthetic.main.news_item.view.*

class NewsAdapter(var news: ArrayList<News>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false))

    override fun getItemCount(): Int = news.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleTxT.text = news[position].title
        holder.classifyTxT.text = news[position].description
        holder.timeTxT.text = news[position].ctime
        val context = holder.itemView.context
        Glide.with(context).load(news[position].picUrl).into(holder.thumbnailPic)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ContentActivity::class.java)
            intent.putExtra(URL, news[position].url)
            intent.putExtra(TITLE, news[position].title)
            intent.putExtra(PIC, news[position].picUrl)
            context.startActivity(intent)
        }
        holder.itemView.startAnimation(AnimationUtils
                .loadAnimation(context, android.R.anim.slide_in_left))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTxT: TextView = itemView.titleTxT
        var classifyTxT: TextView = itemView.classifyTxT
        var timeTxT: TextView = itemView.timeTxT
        var thumbnailPic: ImageView = itemView.thumbnailPic
    }

    companion object {
        const val URL = "URL"
        const val TITLE = "TITLE"
        const val PIC = "PIC"
    }
}