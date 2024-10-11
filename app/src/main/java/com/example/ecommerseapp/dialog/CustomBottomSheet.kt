package com.example.ecommerseapp.dialog

//import android.annotation.SuppressLint
//import android.app.Activity
//import android.content.Context
//import android.view.GestureDetector
//import android.view.LayoutInflater
//import android.view.MotionEvent
//import android.view.View
//import android.view.ViewGroup
//import android.view.animation.Animation
//import android.view.animation.AnimationUtils
//import android.widget.Button
//import android.widget.LinearLayout
//import androidx.customview.widget.ViewDragHelper
//import com.example.ecommerseapp.R

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.customview.widget.ViewDragHelper
import com.example.ecommerseapp.R

class CustomBottomSheet @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var bottomSheetView: View
    private var dragHelper: ViewDragHelper

    init {
        // Inflate layout
        bottomSheetView = LayoutInflater.from(context).inflate(R.layout.custom_bottom_sheet, this, false)
        addView(bottomSheetView)
        bottomSheetView.visibility = View.GONE

        // Initialize ViewDragHelper for drag gestures
        dragHelper = ViewDragHelper.create(this, 1.0f, DragHelperCallback())
    }

    // Custom method to show the bottom sheet with slide-up animation
    fun show() {
        bottomSheetView.visibility = View.VISIBLE
        val slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up)
        bottomSheetView.startAnimation(slideUp)
    }

    // Custom method to hide the bottom sheet with slide-down animation
    fun hide() {
        val slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down)
        bottomSheetView.startAnimation(slideDown)
        bottomSheetView.visibility = View.GONE
    }

    // Detect touch events for drag gestures
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return dragHelper.shouldInterceptTouchEvent(ev!!)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        dragHelper.processTouchEvent(event!!)
        return true
    }

    // Drag Helper Callback for handling drag gestures
    private inner class DragHelperCallback : ViewDragHelper.Callback() {

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            // Only allow bottom sheet to be dragged
            return child == bottomSheetView
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            // Restrict vertical dragging
            return Math.max(0, top)
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            if (releasedChild.top > height / 2) {
                hide()
            } else {
                show()
            }
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return bottomSheetView.height
        }
    }
}

//class CustomBottomSheet(
//    private val activity: Activity
//) {
//
//    private var bottomSheetView: View? = null
//    private var overlay: View? = null
//    private var bottomSheetContainer: LinearLayout? = null
//    private var dragHelper: ViewDragHelper? = null
//
//    init {
//        // Inflate layout bottom sheet
//        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        bottomSheetView = inflater.inflate(R.layout.custom_bottom_sheet, null)
//
//        // Tambahkan ke root layout activity
//        val rootLayout = activity.findViewById<ViewGroup>(android.R.id.content)
//        rootLayout.addView(bottomSheetView)
//
//        // Inisialisasi views
//        overlay = bottomSheetView?.findViewById(R.id.overlay)
//        bottomSheetContainer = bottomSheetView?.findViewById(R.id.bottom_sheet_container)
//
//        // Atur overlay agar bottom sheet ter-dismiss saat di-klik
//        overlay?.setOnClickListener {
//            dismissBottomSheet()
//        }
//
//        // Atur tombol close untuk menutup bottom sheet
//        bottomSheetView?.findViewById<Button>(R.id.button_close)?.setOnClickListener {
//            dismissBottomSheet()
//        }
//
//        // Inisialisasi behavior untuk drag
//        initDraggableBehavior()
//    }
//
//    // Fungsi untuk menampilkan bottom sheet
//    fun showBottomSheet() {
////        overlay?.visibility = View.VISIBLE
////        val anim = AnimationUtils.loadAnimation(activity, R.anim.slide_up)
////        bottomSheetContainer?.startAnimation(anim)
////        bottomSheetContainer?.visibility = View.VISIBLE
//        overlay?.visibility = View.VISIBLE
//        bottomSheetContainer?.visibility = View.VISIBLE
//        bottomSheetContainer?.let { dragHelper?.smoothSlideViewTo(it, 0, 0) }
//    }
//
//    // Fungsi untuk menutup bottom sheet
//    fun dismissBottomSheet() {
////        val anim = AnimationUtils.loadAnimation(activity, R.anim.slide_down)
////        bottomSheetContainer?.startAnimation(anim)
////        anim.setAnimationListener(object : Animation.AnimationListener {
////            override fun onAnimationEnd(animation: Animation?) {
////                bottomSheetContainer?.visibility = View.GONE
////                overlay?.visibility = View.GONE
////            }
////
////            override fun onAnimationStart(animation: Animation?) {}
////            override fun onAnimationRepeat(animation: Animation?) {}
////        })
//        bottomSheetContainer?.let { dragHelper?.smoothSlideViewTo(it, 0, bottomSheetView?.height!!) }
//        bottomSheetView?.postInvalidateOnAnimation()
//    }
//
//    private fun initDraggableBehavior() {
//        dragHelper = ViewDragHelper.create(bottomSheetView as ViewGroup, 1.0f, object : ViewDragHelper.Callback() {
//
//            // Tentukan view yang bisa di-drag (container bottom sheet)
//            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
//                return child == bottomSheetContainer
//            }
//
//            // Tentukan pergerakan vertikal dari view yang di-drag
//            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
//                return Math.max(0, top)
//            }
//
//            // Pergerakan selesai, tentukan apakah view perlu dikembalikan ke atas atau ditutup
//            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
//                val containerHeight = bottomSheetContainer?.height ?: 0
//                if (releasedChild.top > containerHeight / 2) {
//                    dismissBottomSheet()
//                } else {
//                    dragHelper?.settleCapturedViewAt(0, 0)
//                    bottomSheetView?.postInvalidateOnAnimation()
//                }
//            }
//
//            // Periksa status view
//            override fun onViewPositionChanged(
//                changedView: View, left: Int, top: Int, dx: Int, dy: Int
//            ) {
//                overlay?.alpha = 1 - (top.toFloat() / bottomSheetContainer?.height!!)
//            }
//
//            override fun getViewVerticalDragRange(child: View): Int {
//                return bottomSheetContainer?.height ?: 0
//            }
//        })
//    }
//
//    // Override touch event untuk mengaktifkan ViewDragHelper
//    fun onTouchEvent(event: MotionEvent): Boolean {
//        dragHelper?.processTouchEvent(event)
//        return true
//    }
//
//    fun onInterceptTouchEvent(event: MotionEvent): Boolean {
//        return dragHelper?.shouldInterceptTouchEvent(event) ?: false
//    }
//
//    // Inisialisasi behavior untuk drag
////    private fun initDraggableBehavior() {
////        val gestureDetector = GestureDetector(activity, object : GestureDetector.SimpleOnGestureListener() {
////            override fun onScroll(
////                e1: MotionEvent?,
////                e2: MotionEvent,
////                distanceX: Float,
////                distanceY: Float
////            ): Boolean {
////                // Geser bottom sheet sesuai dengan jarak yang di-scroll
////                val translationY = bottomSheetContainer?.translationY ?: 0f
////                bottomSheetContainer?.translationY = translationY - distanceY
////
////                // Jika bottom sheet telah ditarik ke bawah lebih dari 50%, maka dismiss
////                if (translationY - distanceY > bottomSheetContainer?.height!! / 2) {
////                    dismissBottomSheet()
////                }
////                return true
////            }
////        })
////
////        // Handle touch event pada kontainer bottom sheet
////        bottomSheetContainer?.setOnTouchListener { v, event ->
////            gestureDetector.onTouchEvent(event)
////            true
////        }
////    }
//}