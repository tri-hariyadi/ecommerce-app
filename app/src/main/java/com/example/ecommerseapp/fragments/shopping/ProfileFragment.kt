package com.example.ecommerseapp.fragments.shopping

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.bumptech.glide.Glide
import com.example.ecommerseapp.R
import com.example.ecommerseapp.activities.LoginRegisterActivity
import com.example.ecommerseapp.databinding.FragmentProfileBinding
import com.example.ecommerseapp.util.Resource
import com.example.ecommerseapp.util.showBottomNavigationView
import com.example.ecommerseapp.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private lateinit var binding: FragmentProfileBinding
    val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            constraintProfile.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_userAccountFragment)
            }

            linearAllOrders.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_ordersFragment)
            }

            linearBilling.setOnClickListener {
                val action = ProfileFragmentDirections
                    .actionProfileFragmentToBillingFragment(0, emptyArray(), false)
                findNavController().navigate(action)
            }

            linearLogOut.setOnClickListener {
                viewModel.logout()
                val intent = Intent(requireActivity(), LoginRegisterActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }

        val versionName = try {
            val packageInfo = requireContext().packageManager
                .getPackageInfo(requireContext().packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            "Unknown"
        }

        "Version $versionName".also { binding.tvVersion.text = it }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collectLatest {
                    when(it) {
                        is Resource.Loading<*> -> {
                            binding.progressbarSettings.visibility = View.VISIBLE
                        }
                        is Resource.Success<*> -> {
                            binding.progressbarSettings.visibility = View.GONE
                            Glide.with(requireView())
                                .load(it.data!!.imagePath)
                                .error(ColorDrawable(Color.BLACK))
                                .into(binding.imageUser)
                            "${it.data.firstName} ${it.data.lastName}".also { text ->  binding.tvUserName.text = text }
                        }
                        is Resource.Error<*> -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                            binding.progressbarSettings.visibility = View.GONE
                        }
                        else -> Unit
                    }
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }

}