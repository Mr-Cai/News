package com.news

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide


class TitleAdapter(context: Context, private val resourceId: Int, objects: List<Title>) :
        ArrayAdapter<Title>(context, resourceId, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val title = getItem(position)
        val view: View
        val viewHolder: ViewHolder
        /**
         * 缓存布局和实例，优化 listView
         */
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, parent, false)
            viewHolder = ViewHolder()
            viewHolder.titleText = view.findViewById(R.id.titleTxT)
            viewHolder.titlePic = view.findViewById(R.id.thumbnailPic)
            viewHolder.titleDescr = view.findViewById(R.id.classifyTxT)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        assert(title != null)
        Glide.with(context).load(title!!.imageUrl).into(viewHolder.titlePic!!)
        viewHolder.titleText!!.text = title.title
        viewHolder.titleDescr!!.text = title.descr

        return view

    }

    inner class ViewHolder {
        internal var titleText: TextView? = null
        internal var titleDescr: TextView? = null
        internal var titlePic: ImageView? = null
    }
}
