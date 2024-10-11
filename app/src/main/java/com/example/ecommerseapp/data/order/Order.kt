package com.example.ecommerseapp.data.order

import android.os.Parcelable
import com.example.ecommerseapp.data.Address
import com.example.ecommerseapp.data.CartProduct
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

@Parcelize
data class Order(
    val orderStatus: String = "",
    val totalPrice: Int = 0,
    val products: List<CartProduct> = emptyList(),
    val address: Address = Address(),
    val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date()),
    val orderId: Long = Random.nextLong(0, 100_000_000_000) * totalPrice.toLong()
) : Parcelable
