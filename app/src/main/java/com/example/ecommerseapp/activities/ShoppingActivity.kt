package com.example.ecommerseapp.activities

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ecommerseapp.R
import com.example.ecommerseapp.databinding.ActivityShoppingBinding
import com.example.ecommerseapp.util.Resource
import com.example.ecommerseapp.viewmodel.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityShoppingBinding.inflate(layoutInflater)
    }
    val viewModel by viewModels<CartViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.shoppingHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartProducts.collectLatest {
                    when(it) {
                        is Resource.Success<*> -> {
                            val count = it.data?.size ?: 0
                            val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
                            if (count > 0) {
                                bottomNavigation.getOrCreateBadge(R.id.cartFragment).apply {
                                    number = count
                                    backgroundColor = resources.getColor(R.color.pink)
                                }
                            } else {
                                bottomNavigation.removeBadge(R.id.cartFragment)
                            }
                        }
                        else -> Unit
                    }
                }
            }
        }

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view is EditText) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                        view.clearFocus()
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                    }
            }
        }

        return super.dispatchTouchEvent(ev)
    }
}