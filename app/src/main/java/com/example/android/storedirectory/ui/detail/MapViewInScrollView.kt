package com.example.android.storedirectory.ui.detail

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.google.android.gms.maps.MapView

class MapViewInScrollView(context: Context, attributeSet: AttributeSet) :
    MapView(context, attributeSet) {

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            when (it.action) {
                MotionEvent.ACTION_UP -> {
                    this.parent.requestDisallowInterceptTouchEvent(false)
                }
                MotionEvent.ACTION_DOWN -> {
                    this.parent.requestDisallowInterceptTouchEvent(true)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}