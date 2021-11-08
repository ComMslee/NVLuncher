package com.nv.customview.widget

import android.appwidget.AppWidgetHostView
import android.content.Context
import android.view.MotionEvent
import android.view.ViewConfiguration

class WidgetView : AppWidgetHostView {
    var longClick: OnLongClickListener? = null
    private var mHasPerformedLongPress = false
    private var mPendingCheckForLongPress: CheckForLongPress? = null


    inner class CheckForLongPress : Runnable {
        private var mOriginalWindowAttachCount = 0
        override fun run() {

            mHasPerformedLongPress = true
            longClick?.onLongClick(null)
        }

        fun rememberWindowAttachCount() {
            mOriginalWindowAttachCount = windowAttachCount
        }
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, animationIn: Int, animationOut: Int) : super(
        context,
        animationIn,
        animationOut
    )


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // Consume any touch events for ourselves after longpress is triggered
//        if (mHasPerformedLongPress) {
//
//            mHasPerformedLongPress = false
//            return true
//        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                postCheckForLongClick()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mHasPerformedLongPress = false
                if (mPendingCheckForLongPress != null) {
                    removeCallbacks(mPendingCheckForLongPress)
                }
            }
        }

        // Otherwise continue letting touch events fall through to children
        return false
    }


    private fun postCheckForLongClick() {
        mHasPerformedLongPress = false
        if (mPendingCheckForLongPress == null) {
            mPendingCheckForLongPress = CheckForLongPress()
        }
        mPendingCheckForLongPress!!.rememberWindowAttachCount()
        postDelayed(mPendingCheckForLongPress, ViewConfiguration.getLongPressTimeout().toLong())
    }

    override fun cancelLongPress() {
        super.cancelLongPress()
        mHasPerformedLongPress = false
        if (mPendingCheckForLongPress != null) {
            removeCallbacks(mPendingCheckForLongPress)
        }
    }

    override fun getDescendantFocusability(): Int {
        return FOCUS_BLOCK_DESCENDANTS
    }
}