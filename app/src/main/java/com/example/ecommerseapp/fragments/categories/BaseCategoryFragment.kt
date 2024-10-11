package com.example.ecommerseapp.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerseapp.R
import com.example.ecommerseapp.adapters.BestProductAdapter
import com.example.ecommerseapp.databinding.FragmentBaseCategoryBinding
import com.example.ecommerseapp.util.MaxWidthLayoutManager
import com.example.ecommerseapp.util.PaddingItemDecoration
import com.example.ecommerseapp.util.showBottomNavigationView

open class BaseCategoryFragment : Fragment(R.layout.fragment_base_category) {
    private lateinit var binding: FragmentBaseCategoryBinding
//    private lateinit var offerAdapter: BestProductAdapter
//    private lateinit var bestProductAdapter: BestProductAdapterprivate
    protected val offerAdapter: BestProductAdapter by lazy { BestProductAdapter() }
    protected val bestProductAdapter: BestProductAdapter by lazy { BestProductAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOfferRv()
        setupBestProductRv()

        offerAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment, b)
        }

        bestProductAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment, b)
        }

        binding.rvOffer.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(1) && dx != 0) {
                    onOfferPagingRequest()
                }
            }
        })

        binding.nestedScrollBaseCategory.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (v.getChildAt(0).bottom <= v.height + scrollY) {
                    onBestPagingRequest()
                }
            }
        )

    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }

    fun showOfferLoading() {
        binding.offerProductsProgressbar.visibility = View.VISIBLE
    }

    fun hideOfferLoading() {
        binding.offerProductsProgressbar.visibility = View.GONE
    }

    fun showBestProductLoading() {
        binding.bestProductsProgressbar.visibility = View.VISIBLE
    }

    fun hideBestProductLoading() {
        binding.bestProductsProgressbar.visibility = View.GONE
    }

    open fun onOfferPagingRequest() {

    }

    open fun onBestPagingRequest() {

    }

    private fun setupOfferRv() {
        binding.rvOffer.apply {
            val maxWidthInPx = resources.displayMetrics.density * 200
//            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            layoutManager = MaxWidthLayoutManager(requireContext(), maxWidthInPx.toInt(), LinearLayoutManager.HORIZONTAL)

            val paddingStart = (16 * resources.displayMetrics.density).toInt()
            val paddingEnd = (16 * resources.displayMetrics.density).toInt()
            val spaceItem = (16 * resources.displayMetrics.density).toInt()
            addItemDecoration(PaddingItemDecoration(paddingStart, paddingEnd, spaceItem))
            adapter = offerAdapter
        }
    }

    private fun setupBestProductRv() {
        binding.rvBaseBestProduct.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductAdapter
        }
    }
}