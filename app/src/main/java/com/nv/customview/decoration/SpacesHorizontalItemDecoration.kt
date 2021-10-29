package gg.op.agro.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class SpacesHorizontalItemDecoration(private val space: Int, private val edgeSpace: Int) :
    ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {

        val position = parent.getChildLayoutPosition(view)
        val count = parent.adapter?.itemCount
        if (position == 0) {
            outRect.left = edgeSpace
        } else {
            outRect.left = space
        }

        if (position + 1 == count) {
            outRect.right = edgeSpace
        }
    }

}