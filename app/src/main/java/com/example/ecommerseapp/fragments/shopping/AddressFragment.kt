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
import com.example.ecommerseapp.data.Address
import com.example.ecommerseapp.databinding.FragmentAddressBinding
import com.example.ecommerseapp.util.Resource
import com.example.ecommerseapp.viewmodel.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressFragment : Fragment() {
    private lateinit var binding: FragmentAddressBinding
    val viewModel by viewModels<AddressViewModel>()
    private val args by navArgs<AddressFragmentArgs>()

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.addNewAddress.collectLatest {
//                    when(it) {
//                        is Resource.Loading<*> -> {
//                            binding.progressbarAddress.visibility = View.VISIBLE
//                        }
//                        is Resource.Success<*> -> {
//                            binding.progressbarAddress.visibility = View.INVISIBLE
//                            findNavController().navigateUp()
//                        }
//                        is Resource.Error<*> -> {
//                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
//                        }
//                        else -> Unit
//                    }
//                }
//            }
//        }
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.error.collectLatest {
//                    Toast.makeText(requireContext(), it , Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addressArg = args.address
        if (addressArg == null) {
            binding.buttonDelelte.visibility = View.GONE
        } else {
            binding.apply {
                edAddressTitle.setText(addressArg.addressTitle)
                edFullName.setText(addressArg.fullName)
                edState.setText(addressArg.street)
                edPhone.setText(addressArg.phone)
                edCity.setText(addressArg.city)
                edState.setText(addressArg.state)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addNewAddress.collectLatest {
                    when(it) {
                        is Resource.Loading<*> -> {
                            binding.progressbarAddress.visibility = View.VISIBLE
                        }
                        is Resource.Success<*> -> {
                            binding.progressbarAddress.visibility = View.INVISIBLE
                            findNavController().navigateUp()
                        }
                        is Resource.Error<*> -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> Unit
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.collectLatest {
                    Toast.makeText(requireContext(), it , Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.apply {
            buttonSave.setOnClickListener {
                val addressTitle = edAddressTitle.text.toString()
                val fullName = edFullName.text.toString()
                val street = edStreet.text.toString()
                val phone = edPhone.text.toString()
                val city = edCity.text.toString()
                val state = edState.text.toString()
                val address = Address(addressTitle, fullName, street, phone, city, state)

                viewModel.addAddress(address)
            }
        }
    }
}