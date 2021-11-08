package com.nv.customview.applist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nv.lutil.p.PListAdapter
import com.nv.nvluncher.R
import kotlinx.android.synthetic.main.item_app.view.*

class AppListAdapter() : PListAdapter<RecyclerView.ViewHolder, AppData>() {
    inner class Holder(parent: ViewGroup) : RecyclerView.ViewHolder(parent) {
        val thumbnail: ImageView = itemView.item_image_view_app
        val name: TextView = itemView.item_text_view_app

        init {
            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(adapterPosition, arrayList.get(adapterPosition))
            }
        }
    }

    override fun mapping(holder: RecyclerView.ViewHolder, model: AppData, position: Int) {
        if (holder is Holder) {
            holder.thumbnail.setImageDrawable(model.thumb)
            holder.name.text = model.name
        }
    }

    private var type: Int = 0

    constructor(type: Int) : this() {
        this.type = type
    }

    override fun getItemViewType(position: Int): Int {
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = when (viewType) {
            1 -> {
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_app_h, parent, false)
            }
            else -> {
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_app, parent, false)
            }
        }
        return Holder(view as ViewGroup)
    }
}