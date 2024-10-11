package com.example.ecommerseapp.adapters

import android.graphics.Paint
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
import com.example.ecommerseapp.databinding.ProductRvItemBinding
import com.example.ecommerseapp.helper.Helpers

class BestProductAdapter : RecyclerView.Adapter<BestProductAdapter.BestProductViewHolder>() {
    private var maxHeight = 0

    inner class BestProductViewHolder(private val binding: ProductRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                val multiTransformation = MultiTransformation(CenterCrop(), GranularRoundedCorners(20f, 20f, 0f, 0f))
                Glide.with(itemView)
                    .load(product.images[0])
                    .apply(RequestOptions()
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

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductViewHolder {
        return BestProductViewHolder(
            ProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: BestProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }

        holder.itemView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val itemHeight = holder.itemView.measuredHeight
                if (itemHeight > maxHeight) {
                    maxHeight = itemHeight
                    notifyItemRangeChanged(0, itemCount) // Update semua item
                }

                // Setel tinggi item menjadi maxHeight jika sudah ditemukan
                if (maxHeight > 0) {
                    val params = holder.itemView.layoutParams
                    params.height = maxHeight
                    holder.itemView.layoutParams = params
                }

                // Hapus listener setelah diterapkan
                holder.itemView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }

        })
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((Product) -> Unit)? = null
}