package com.example.androidhw.recycler_view

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridItemDecorator(
    private val context: Context,
    private val spacingDp: Float
): RecyclerView.ItemDecoration() {

    private val spacingPx: Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        spacingDp,
        context.resources.displayMetrics
    ).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val spacingMiddle = (spacingDp * 0.75).toInt()

        val viewHolder = parent.getChildViewHolder(view)
        val currentPosition = parent.getChildAdapterPosition(view).takeIf {
            it != RecyclerView.NO_POSITION
        } ?: viewHolder.oldPosition

        when (currentPosition % 2) {
            0 -> {
                if (currentPosition == 0) {
                    outRect.top = spacingPx
                    outRect.left = spacingPx
                    outRect.right = spacingMiddle
                    outRect.bottom = spacingMiddle
                } else {
                    if (currentPosition == state.itemCount - 1 || currentPosition == state.itemCount - 2) {
                        outRect.top = spacingMiddle
                        outRect.left = spacingPx
                        outRect.right = spacingMiddle
                        outRect.bottom = spacingPx
                    } else {
                        outRect.top = spacingMiddle
                        outRect.left = spacingPx
                        outRect.right = spacingMiddle
                        outRect.bottom = spacingMiddle
                    }
                }
            }

            1 -> {
                if (currentPosition == 1) {
                    outRect.top = spacingPx
                    outRect.left = spacingMiddle
                    outRect.right = spacingPx
                    outRect.bottom = spacingMiddle
                } else {
                    if (currentPosition == state.itemCount - 1) {
                        outRect.top = spacingMiddle
                        outRect.left = spacingMiddle
                        outRect.right = spacingPx
                        outRect.bottom = spacingPx
                    } else {
                        outRect.top = spacingMiddle
                        outRect.left = spacingMiddle
                        outRect.right = spacingPx
                        outRect.bottom = spacingMiddle
                    }
                }
            }
        }
    }
}
