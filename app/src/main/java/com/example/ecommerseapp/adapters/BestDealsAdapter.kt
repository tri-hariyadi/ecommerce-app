package com.example.ecommerseapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.ecommerseapp.data.Product
import com.example.ecommerseapp.databinding.BestDealsRvItemBinding
import com.example.ecommerseapp.helper.Helpers

class BestDealsAdapter : RecyclerView.Adapter<BestDealsAdapter.BestDealsViewHolder>() {
    inner class BestDealsViewHolder(private val binding: BestDealsRvItemBinding) : ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                val multiTransformation = MultiTransformation(CenterCrop(), RoundedCorners(20))
                Glide.with(itemView)
                    .load(product.images[0])
                    .apply(
                        RequestOptions()
                        .transform(multiTransformation)
                    )
                    .into(imgBestDeal)
                product.offerPercentage?.let {
                    val remainingPricePercentage = it/100
                    val priceAfterOffer = remainingPricePercentage * product.price
                    tvNewPrice.text = Helpers.formatCurrency(priceAfterOffer)
                }
                tvOldPrice.text =  Helpers.formatCurrency(product.price.toFloat())
                tvDealProductName.text = product.name
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestDealsViewHolder {
        return BestDealsViewHolder(
            BestDealsRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: BestDealsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((Product) -> Unit)? = null
}