package com.example.new_bottom_navigation_ui

import android.graphics.Rect
import android.util.TypedValue
import android.view.View

import androidx.recyclerview.widget.RecyclerView


class SpaceItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.apply {
            bottom = 8.toDp(view)
        }
    }
}

fun Number.toDp(view: View) =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        view.resources.displayMetrics
    ).toInt()