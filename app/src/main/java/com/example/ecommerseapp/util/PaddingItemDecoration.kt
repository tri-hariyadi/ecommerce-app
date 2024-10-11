package com.example.ecommerseapp.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class PaddingItemDecoration(
    private val paddingStart: Int,
    private val paddingEnd: Int,
    private val space: Int = 10
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        // Tambahkan padding pada item pertama
        if (position == 0) {
            outRect.left = paddingStart
        }

        // Tambahkan padding pada item terakhir
        if (position == itemCount - 1) {
            outRect.right = paddingEnd
        }

        if (position < itemCount - 1) {
            outRect.right = space  // Menambahkan jarak antar item pada bagian bawah
        }
    }
}
