package com.example.ecommerseapp.fragments.shopping

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerseapp.R
import com.example.ecommerseapp.adapters.SearchAdapter
import com.example.ecommerseapp.databinding.FragmentSearchBinding
import com.example.ecommerseapp.util.GridSpacingItemDecoration
import com.example.ecommerseapp.util.Resource
import com.example.ecommerseapp.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModels<SearchViewModel>()
    private val searchProductAdapter: SearchAdapter by lazy { SearchAdapter() }

    // CoroutineScope to handle debounce
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupItemsProduct()

        binding.edSearch.requestFocus()

        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.edSearch, InputMethodManager.SHOW_IMPLICIT)

        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()
                // Cancel the previous job if it's still running
                searchJob?.cancel()

                // Start a new coroutine with debounce
                searchJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(300) // Debounce delay, 300ms
                    if (searchText.isNotEmpty()) {
                        viewModel.searchItem(searchText)
                    } else {
                        searchProductAdapter.differ.submitList(emptyList())
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.search.collectLatest {
                    when(it) {
                        is Resource.Loading<*> -> {

                        }
                        is Resource.Success<*> -> {
                            searchProductAdapter.differ.submitList(it.data)
                        }
                        is Resource.Error<*> -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun setupItemsProduct() {
        binding.rcItemsSearch.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            (layoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == 0) 2 else 1
                }

            }

            adapter = searchProductAdapter
            val itemDecoration = GridSpacingItemDecoration(2, resources.getDimensionPixelOffset(R.dimen.grid_spacing), hasHeader = true)
            addItemDecoration(itemDecoration)
        }
    }
}