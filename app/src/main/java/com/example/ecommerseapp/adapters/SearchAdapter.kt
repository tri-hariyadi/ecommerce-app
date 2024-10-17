package com.example.ecommerseapp.adapters

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.ecommerseapp.data.Product
import com.example.ecommerseapp.databinding.HeaderListSerchBinding
import com.example.ecommerseapp.databinding.ProductRvItemBinding
import com.example.ecommerseapp.helper.Helpers

class

SearchAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1
    private var maxHeight = 0

    inner class SearchAdapterViewHolder(private val binding: ProductRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                val multiTransformation = MultiTransformation(CenterCrop(), GranularRoundedCorners(20f, 20f, 0f, 0f))
                Glide.with(itemView)
                    .load(product.images[0])
                    .apply(
                        RequestOptions()
                        .transform(multiTransformation)
                    )
                    .into(imgProduct)
                product.offerPercentage?.let {
                    tvNewPrice.text = Helpers.getProductPrice(product.price, it)
                    tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                if (product.offerPercentage == null) {
                    tvNewPrice.visibility = View.GONE
                }
                tvPrice.text =  Helpers.getProductPrice(product.price)
                tvName.text = product.name
            }
        }
    }

    inner class HeaderViewHolder(private val binding: HeaderListSerchBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            HeaderViewHolder(
                HeaderListSerchBinding.inflate(
                    LayoutInflater.from(parent.context)
                )
            )
        } else {
            SearchAdapterViewHolder(
                ProductRvItemBinding.inflate(
                    LayoutInflater.from(parent.context)
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size + 1
    }

    // Tentukan jenis tampilan untuk setiap posisi
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SearchAdapterViewHolder) {
            val product = differ.currentList[position - 1]
            holder.bind(product)

            holder.itemView.setOnClickListener {
                onClick?.invoke(product)
            }

            holder.itemView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    holder.itemView.post {
                        val itemHeight = holder.itemView.height
                        if (itemHeight > maxHeight) {
                            maxHeight = itemHeight
                            notifyItemRangeChanged(0, itemCount) // Update semua item
                        }

                        // Setel tinggi item menjadi maxHeight jika sudah ditemukan
                        if (maxHeight > 0) {
                            holder.itemView.layoutParams.height = maxHeight
                            holder.itemView.requestLayout()
                        }
                    }

                    // Hapus listener setelah diterapkan
                    holder.itemView.viewTreeObserver.removeOnPreDrawListener(this)
                    return true
                }

            })
        }
    }

    var onClick: ((Product) -> Unit)? = null
}