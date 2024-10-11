package com.example.ecommerseapp.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerseapp.R
import com.example.ecommerseapp.adapters.ColorsAdapter
import com.example.ecommerseapp.adapters.SizesAdapter
import com.example.ecommerseapp.adapters.ViewPager2Images
import com.example.ecommerseapp.data.CartProduct
import com.example.ecommerseapp.databinding.FragmentProductDetailsBinding
import com.example.ecommerseapp.helper.Helpers
import com.example.ecommerseapp.util.Resource
import com.example.ecommerseapp.util.hideBottomNavigationView
import com.example.ecommerseapp.viewmodel.DetailsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding: FragmentProductDetailsBinding
    private val viewPagerAdapter by lazy { ViewPager2Images() }
    private val sizesAdapter by lazy { SizesAdapter() }
    private val colorsAdapter by lazy { ColorsAdapter() }
    private var selectedColor: Int? = null
    private var selectedSize: String? = null
    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBottomNavigationView()
        binding = FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        setupSizesRv()
        setupColorsRv()
        setupViewpager()

        sizesAdapter.onItemClick = {
            selectedSize = it
        }

        colorsAdapter.onItemClick = {
            selectedColor = it
        }

        binding.apply {
            tvProductName.text = product.name
            tvProductPrice.text = Helpers.formatCurrency(product.price.toFloat())
            tvProductDescription.text = product.description

            imageClose.setOnClickListener {
                findNavController().navigateUp()
            }

            if (product.colors.isNullOrEmpty()) {
                tvProductColors.visibility = View.INVISIBLE
            }
            if (product.sizes.isNullOrEmpty()) {
                tvProductSize.visibility = View.INVISIBLE
            }

            buttonAddToCart.setOnClickListener {
                viewModel.addUpdateProductInCart(
                    CartProduct(product, 1, selectedColor, selectedSize)
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addToCart.collectLatest {
                    when(it) {
                        is Resource.Loading<*> -> {
                            binding.buttonAddToCart.startAnimation()
                        }
                        is Resource.Success<*> -> {
                            binding.buttonAddToCart.revertAnimation()
                            Toast.makeText(requireContext(), "Product was added to cart", Toast.LENGTH_SHORT)
                                .show()
                        }
                        is Resource.Error<*> -> {
                            binding.buttonAddToCart.revertAnimation()
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> Unit
                    }
                }
            }
        }

        viewPagerAdapter.differ.submitList(product.images)
        if (product.images.size < 2) {
            binding.tabLayout.visibility = View.GONE
        }
        product.colors?.let { colorsAdapter.differ.submitList(it) }
        product.sizes?.let { sizesAdapter.differ.submitList(it) }
    }

    private fun setupSizesRv() {
        binding.rvSize.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = sizesAdapter
        }
    }

    private fun setupColorsRv() {
        binding.rvColors.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = colorsAdapter
        }
    }

    private fun setupViewpager() {
        binding.apply {
            viewPagerProductImages.adapter = viewPagerAdapter
            TabLayoutMediator(tabLayout, viewPagerProductImages) { tab, position ->
                // Tidak perlu mengatur apa pun di sini, hanya menghubungkan TabLayout dengan ViewPager2
            }.attach()
        }
    }
}