package com.example.ecommerseapp.fragments.settings

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerseapp.adapters.AllOrdersAdapter
import com.example.ecommerseapp.databinding.FragmentOrdersBinding
import com.example.ecommerseapp.util.Resource
import com.example.ecommerseapp.util.hideBottomNavigationView
import com.example.ecommerseapp.viewmodel.AllOrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllOrdersFragment : Fragment() {
    private lateinit var binding: FragmentOrdersBinding
    val viewModel by viewModels<AllOrdersViewModel>()
    private val ordersAdapter by lazy { AllOrdersAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideBottomNavigationView()
        setupOrdersRv()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allOrders.collectLatest {
                    when(it) {
                        is Resource.Loading<*> -> {
                            binding.progressbarAllOrders.visibility = View.VISIBLE
                        }
                        is Resource.Success<*> -> {
                            binding.progressbarAllOrders.visibility = View.GONE
                            ordersAdapter.differ.submitList(it.data)
                            if (it.data.isNullOrEmpty()) {
                                binding.tvEmptyOrders.visibility = View.VISIBLE
                            } else {
                                binding.tvEmptyOrders.visibility = View.GONE
                            }
                        }
                        is Resource.Error<*> -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                            binding.progressbarAllOrders.visibility = View.GONE
                        }
                        else -> Unit
                    }
                }
            }
        }

        ordersAdapter.onClick = {
            val action = AllOrdersFragmentDirections.actionOrdersFragmentToOrderDetailFragment(it)
            findNavController().navigate(action)
        }

        binding.imageCloseOrders.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun setupOrdersRv() {
        binding.rvAllOrders.apply {
            adapter = ordersAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }
}