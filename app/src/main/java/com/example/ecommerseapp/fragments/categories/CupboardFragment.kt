package com.example.ecommerseapp.fragments.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ecommerseapp.data.Category
import com.example.ecommerseapp.util.Resource
import com.example.ecommerseapp.viewmodel.CategoryViewModel
import com.example.ecommerseapp.viewmodel.factory.BaseCategoryViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CupboardFragment : BaseCategoryFragment() {
    @Inject
    lateinit var firestore: FirebaseFirestore

    val viewModel by viewModels<CategoryViewModel> {
        BaseCategoryViewModelFactory(firestore, Category.Cupboard)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.offerProducts.collectLatest {
                    when(it) {
                        is Resource.Loading<*> -> {
                            showOfferLoading()
                        }
                        is Resource.Success<*> -> {
                            hideOfferLoading()
                            offerAdapter.differ.submitList(it.data)
                        }
                        is Resource.Error<*> -> {
                            hideOfferLoading()
                            Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG).show()
                        }
                        else -> Unit
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bestProducts.collectLatest {
                    when(it) {
                        is Resource.Loading<*> -> {
                            showBestProductLoading()
                        }
                        is Resource.Success<*> -> {
                            hideBestProductLoading()
                            bestProductAdapter.differ.submitList(it.data)
                        }
                        is Resource.Error<*> -> {
                            hideBestProductLoading()
                            Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG).show()
                        }
                        else -> Unit
                    }
                }
            }
        }

    }
}