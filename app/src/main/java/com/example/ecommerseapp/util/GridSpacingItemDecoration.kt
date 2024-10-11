package com.example.ecommerseapp.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val spanCount: Int,   // Jumlah kolom pada grid
    private val spacing: Int,     // Jarak antar item (spasi horizontal/vertical)
    private val includeEdge: Boolean = true // Apakah spasi akan ditambahkan di tepi grid atau tidak
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // posisi item dalam adapter
        val column = position % spanCount // kolom item (index dimulai dari 0)

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
            if (position >= spanCount) {
                outRect.top = spacing // spasi atas (bukan di baris pertama)
            }
        }
    }
}