package com.example.ecommerseapp.data.order

enum class OrderStats(val desc: String) {
    ORDERED("Ordered"),
    CANCELED("Canceled"),
    CONFIRMED("Confirmed"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    RETURNED("Returned")
}

sealed class OrderStatus(val status: String) {
    data object Ordered : OrderStatus(OrderStats.ORDERED.desc)
    data object Canceled : OrderStatus(OrderStats.CANCELED.desc)
    data object Confirmed : OrderStatus(OrderStats.CONFIRMED.desc)
    data object Shipped : OrderStatus(OrderStats.SHIPPED.desc)
    data object Delivered : OrderStatus(OrderStats.DELIVERED.desc)
    data object Returned : OrderStatus(OrderStats.RETURNED.desc)
}

fun getOrderStatus(status: String): OrderStatus {
    return when (status) {
        OrderStats.ORDERED.desc -> {
            OrderStatus.Ordered
        }
        OrderStats.CANCELED.desc -> {
            OrderStatus.Canceled
        }
        OrderStats.CONFIRMED.desc -> {
            OrderStatus.Confirmed
        }
        OrderStats.SHIPPED.desc -> {
            OrderStatus.Shipped
        }
        OrderStats.DELIVERED.desc -> {
            OrderStatus.Delivered
        }
        else -> OrderStatus.Returned
    }
}
