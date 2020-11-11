package com.hadenhiles.skilldrills

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.hadenhiles.skilldrills.models.Activity
import com.hadenhiles.skilldrills.models.DrillType
import com.hadenhiles.skilldrills.models.activites.Baseball
import com.hadenhiles.skilldrills.models.activites.Golf
import com.hadenhiles.skilldrills.models.activites.Hockey
import com.hadenhiles.skilldrills.models.drilltypes.Duration
import com.hadenhiles.skilldrills.models.drilltypes.DurationCountdown
import com.hadenhiles.skilldrills.models.drilltypes.Score
import kotlinx.android.synthetic.main.activity_add_drill.*


class AddDrillActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_drill)

        addDrillToolbar.setNavigationOnClickListener {
            finish()
        }

        // Populate activities dropdown
        val activities: ArrayList<Activity> = arrayListOf(Hockey(), Golf(), Baseball())
        val activityAdapter: ArrayAdapter<Activity> = ArrayAdapter<Activity>(applicationContext, android.R.layout.simple_spinner_dropdown_item, activities)
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        activitySpinner.adapter = activityAdapter

        // Populate activities dropdown
        val drillTypes: ArrayList<DrillType> = arrayListOf(Duration(), DurationCountdown(), Score())
        val drillTypeAdapter: ArrayAdapter<DrillType> = ArrayAdapter<DrillType>(applicationContext, android.R.layout.simple_spinner_dropdown_item, drillTypes)
        drillTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        drillTypeSpinner.adapter = drillTypeAdapter

        // When the user selects/changes the drill activity type
        activitySpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                // Populate categories dropdown based on selected activity
                var categories: Array<String> = arrayOf()
                val selectedActivity = activitySpinner.selectedItem.toString()

                if (selectedActivity == "Hockey") {
                    categories = Hockey().categories?: arrayOf("Other")
                } else if (selectedActivity == "Golf") {
                    categories = Golf().categories?: arrayOf("Other")
                } else if (selectedActivity == "Baseball") {
                    categories = Baseball().categories?: arrayOf("Other")
                }

                categorySpinner.adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, categories)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }
}