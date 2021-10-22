package gg.op.agro.decoration

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class SpacesGridHorizontalItemDecoration(private val rowNum: Int,private val space: Int,private val edgeSpace : Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.right = space
        val position = parent.getChildLayoutPosition(view)
        val count  = parent.adapter?.itemCount




        if(position<rowNum){
            outRect.left= edgeSpace
        }
        if(position>=count!!-rowNum){
            outRect.right = edgeSpace
        }

        if (  position % rowNum  != 0) {
            outRect.top = space/2


        }

    }

}