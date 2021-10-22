package com.nv.customview.applist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nv.nvluncher.R
import gg.op.agro.p.PListAdapter
import kotlinx.android.synthetic.main.item_app.view.*

class AppListAdapter() : PListAdapter<AppListAdapter.ViewHolder, AppData>(){


    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(
        R.layout.item_app,parent,false)) {


        val thumbnail: ImageView = itemView.item_image_view_app
        val name: TextView = itemView.item_text_view_app

        init {
            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(adapterPosition,arrayList.get(adapterPosition))
            }
        }
    }

    override fun mapping(holder: AppListAdapter.ViewHolder, model: AppData, position: Int) {
        holder.thumbnail.setImageDrawable(model.thumb)
        holder.name.text = model.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppListAdapter.ViewHolder {
        return ViewHolder(parent)
    }
}