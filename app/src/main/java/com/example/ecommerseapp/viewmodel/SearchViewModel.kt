package com.example.ecommerseapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerseapp.data.Product
import com.example.ecommerseapp.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val db: FirebaseFirestore
) : ViewModel() {
    private val _search = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val search: Flow<Resource<List<Product>>> = _search

    fun searchItem(searchText: String) {
        db.collection("Products")
            .orderBy("name")
            .startAt(searchText)
            .endAt(searchText + "\uf8ff")
            .get()
            .addOnSuccessListener {documents ->
                val results = documents.toObjects(Product::class.java)
                viewModelScope.launch {
                    _search.emit(Resource.Success(results))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _search.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}