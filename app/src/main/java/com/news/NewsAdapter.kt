package com.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.news_item.view.*

class NewsAdapter(var news: ArrayList<Title>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false))

    override fun getItemCount(): Int = news.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleTxT.text = news[position].title
        holder.classifyTxT.text = news[position].descr
        holder.timeTxT.text = news[position].time
        Glide.with(holder.itemView.context).load(news[position].imageUrl).into(holder.thumbnailPic)
        holder.itemView.setOnClickListener {

        }
        holder.itemView.startAnimation(AnimationUtils
                .loadAnimation(holder.itemView.context, android.R.anim.slide_in_left))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTxT: TextView = itemView.titleTxT
        var classifyTxT: TextView = itemView.classifyTxT
        var timeTxT: TextView = itemView.timeTxT
        var thumbnailPic: ImageView = itemView.thumbnailPic
    }
}