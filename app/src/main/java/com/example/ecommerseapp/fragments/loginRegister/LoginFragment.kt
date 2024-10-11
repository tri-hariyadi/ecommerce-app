package com.example.ecommerseapp.fragments.loginRegister

import android.content.Intent
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
import com.example.ecommerseapp.R
import com.example.ecommerseapp.activities.ShoppingActivity
import com.example.ecommerseapp.databinding.FragmentLoginBinding
import com.example.ecommerseapp.dialog.CustomBottomSheet
import com.example.ecommerseapp.dialog.setupBottomSheetDialog
import com.example.ecommerseapp.util.Resource
import com.example.ecommerseapp.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var bottomSheet: CustomBottomSheet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        bottomSheet = binding.customBottomSheet
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tvDontHaveAccount.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            tvForgotPasswordLogin.setOnClickListener {
                setupBottomSheetDialog { email ->
                    viewModel.resetPassword(email)
                }
//                bottomSheet.show()
            }

            buttonLoginLogin.setOnClickListener {
                val email = edEmailLogin.text.toString().trim()
                val password = edPasswordLogin.text.toString()
                viewModel.login(email, password)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resetPassword.collect {
                    when(it) {
                        is Resource.Loading<*> -> Unit
                        is Resource.Success<*> -> {
                            Snackbar.make(
                                requireView(),
                                "Reset link was sent to your email",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                        is Resource.Error<*> -> {
                            Snackbar.make(requireView(), "Error: ${it.message}", Snackbar.LENGTH_LONG).show()
                        }
                        else -> Unit
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.login.collect {
                    when(it) {
                        is Resource.Loading<*> -> {
                            binding.buttonLoginLogin.startAnimation()
                        }
                        is Resource.Success<*> -> {
                            binding.buttonLoginLogin.revertAnimation()
                            Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                                intent.addFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                )
                                startActivity(intent)
                            }
                        }
                        is Resource.Error<*> -> {
                            binding.buttonLoginLogin.revertAnimation()
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> Unit
                    }
                }
            }
        }

    }

}