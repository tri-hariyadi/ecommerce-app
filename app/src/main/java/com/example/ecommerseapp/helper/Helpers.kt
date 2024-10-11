package com.example.ecommerseapp.helper

class Helpers {
    companion object {
        fun formatCurrency(idr: Float): String {
            var rupiah = ""
            val nominal = idr.toInt().toString().split("").reversed().joinToString("")
            for (index in nominal.indices) {
                if (index % 3 == 0) {
                    val a = if (index + 3 > nominal.length) nominal.length else index + 3
                    rupiah += nominal.substring(index, a) + "."
                }
            }
            return "Rp ${
                rupiah.split("")
                    .slice(rupiah.indices)
                    .joinToString("")
                    .reversed()}"
        }

        /**
         * Helper function that return String with format currency in rupiah
         * this function can also calculate the price with offer percentage
         */
        fun getProductPrice(price: Int, offerPercentage: Float? = null): String {
            if (offerPercentage == null) return formatCurrency(price.toFloat())

            val remainingPricePercentage = offerPercentage/100
            val priceAfterOffer = remainingPricePercentage * price
            return formatCurrency(price.toFloat() - priceAfterOffer)
        }
    }
}