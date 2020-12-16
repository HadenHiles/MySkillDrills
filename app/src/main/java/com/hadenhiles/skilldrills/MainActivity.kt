package com.hadenhiles.skilldrills

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_profile, R.id.navigation_history, R.id.navigation_start, R.id.navigation_drills, R.id.navigation_routines))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Create a reference for managing bottom sheet behaviour
        val sessionBottomSheetBehavior = BottomSheetBehavior.from(sessionBottomSheet)

        // Setup a reference for listening to state changes of the bottom sheet
        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // Do something for new state
                if (sessionBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    toggleSessionBottomSheetButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_black_32)
                } else {
                    toggleSessionBottomSheetButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_black_32)
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Do something for slide offset
            }
        }

        // Assign the bottom sheet callback to the behaviour callback handler
        sessionBottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)

        sessionBottomSheet.setOnClickListener {
            if (sessionBottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                sessionBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        // Close the bottom sheet when the user clicks the close button
        toggleSessionBottomSheetButton.setOnClickListener {
            if (sessionBottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                sessionBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                sessionBottomSheetBehavior.peekHeight = 200
                navigationContainer.setPadding(0, 0, 0, 200)
                sessionBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }
}