package com.example.ecommerseapp.util

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MaxWidthLayoutManager(
    context: Context,
    private val maxWidth: Int,
    @RecyclerView.Orientation orientation: Int,
    reverseLayout: Boolean = false
) : LinearLayoutManager(context, orientation, reverseLayout) {

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)

        // Loop through each child view to set the max width
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child?.let {
                val params = it.layoutParams as RecyclerView.LayoutParams

                // Set the maximum width
//                if (it.measuredWidth > maxWidth) {
//                    params.width = maxWidth
//                    it.layoutParams = params
//                }
                params.width = maxWidth
                it.layoutParams = params
            }
        }
    }
}