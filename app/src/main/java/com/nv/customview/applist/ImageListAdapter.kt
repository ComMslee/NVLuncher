package com.nv.customview.applist

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import gg.op.agro.p.PListAdapter

class ImageListAdapter : PListAdapter<ImageListAdapter.ViewHolder, Drawable>(){
    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        ImageView(parent.context)) {

        init {
            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(adapterPosition, arrayList[adapterPosition])
            }
        }
    }

    override fun mapping(holder: ImageListAdapter.ViewHolder, model: Drawable, position: Int) {
        val img  = holder.itemView as ImageView
        img.setImageDrawable(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageListAdapter.ViewHolder {
        return ViewHolder(parent)
    }
}