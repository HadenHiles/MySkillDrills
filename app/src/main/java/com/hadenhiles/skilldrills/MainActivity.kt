package com.hadenhiles.skilldrills

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_start.*

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
                    closeSessionBottomSheetButton.alpha = 1F
                } else {
                    closeSessionBottomSheetButton.alpha = 0f
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Do something for slide offset
            }
        }

        // Assign the bottomsheet callback to the behaviour callback handler
        sessionBottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)

        sessionBottomSheet.setOnClickListener {
            if (sessionBottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                sessionBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        // Close the bottom sheet when the user clicks the close button
        closeSessionBottomSheetButton.setOnClickListener {
            sessionBottomSheetBehavior.peekHeight = 200
            navigationContainer.setPadding(0, 0, 0, 200)
            sessionBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
}