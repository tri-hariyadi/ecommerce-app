package com.example.ecommerseapp.firebase

import com.example.ecommerseapp.data.CartProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class FirebaseCommon(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val cartCollection = firestore.collection("user")
        .document(auth.uid!!)
        .collection("cart")

    fun addProductToCart(
        cartProduct: CartProduct,
        onResult: (CartProduct?, Exception?) -> Unit
    ) {
        cartCollection.document().set(cartProduct)
            .addOnSuccessListener {
                onResult(cartProduct, null)
            }
            .addOnFailureListener {
                onResult(null, it)
            }
    }

    fun increaseQuantity(
        documentId: String,
        onResult: (String?, Exception?) -> Unit
    ) {
        firestore.runTransaction { transaction ->
            val documentRef = cartCollection.document(documentId)
            val document = transaction.get(documentRef)
            val productObject = document.toObject(CartProduct::class.java)
            productObject?.let {cartProduct ->
                val newQuantity = cartProduct.quantity + 1
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transaction.set(documentRef, newProductObject)
            }
        }
            .addOnSuccessListener {
                onResult(documentId, null)
            }
            .addOnFailureListener {
                onResult(null, it)
            }
    }

    fun decreaseQuantity(
        documentId: String,
        onResult: (String?, Exception?) -> Unit
    ) {
        firestore.runTransaction { transaction ->
            val documentRef = cartCollection.document(documentId)
            val document = transaction.get(documentRef)
            val productObject = document.toObject(CartProduct::class.java)
            productObject?.let {cartProduct ->
                val newQuantity = cartProduct.quantity - 1
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transaction.set(documentRef, newProductObject)
            }
        }
            .addOnSuccessListener {
                onResult(documentId, null)
            }
            .addOnFailureListener {
                onResult(null, it)
            }
    }

    enum class QuantityChanging {
        INCREASE, DECREASE
    }
}