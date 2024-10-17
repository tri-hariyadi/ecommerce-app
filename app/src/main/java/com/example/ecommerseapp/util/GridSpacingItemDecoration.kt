package com.example.ecommerseapp.util

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val spanCount: Int,   // Jumlah kolom pada grid
    private val spacing: Int,     // Jarak antar item (spasi horizontal/vertical)
    private val includeEdge: Boolean = false, // Apakah spasi akan ditambahkan di tepi grid atau tidak
    private val hasHeader: Boolean = false
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // posisi item dalam adapter
        val layoutManager = parent.layoutManager as GridLayoutManager
        val spanSizeLookup = layoutManager.spanSizeLookup

        val spanSize = spanSizeLookup.getSpanSize(position)
        val column = if (hasHeader) {
            (position - 1) % spanCount // kolom item (index dimulai dari 0)
        } else {
            position % spanCount // kolom item (index dimulai dari 0)
        }

        if (spanSize == spanCount) {
            // Jika header, tidak perlu spacing
            outRect.set(0, 0, 0, 0)
            return
        }

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount // spasi kiri
            outRect.right = (column + 1) * spacing / spanCount    // spasi kanan

            if (position < spanCount) { // item di baris pertama
                outRect.top = spacing // spasi atas (untuk baris pertama)
            }
            outRect.bottom = spacing // spasi bawah
        } else {
            outRect.left = column * spacing / spanCount // spasi kiri (tanpa tepi)
            outRect.right = spacing - (column + 1) * spacing / spanCount // spasi kanan (tanpa tepi)
            if (hasHeader) {
                if (position >= spanCount + 1) {
                    outRect.top = spacing
                }
            } else if (position >= spanCount) {
                outRect.top = spacing // spasi atas (bukan di baris pertama)
            }
        }
    }
}