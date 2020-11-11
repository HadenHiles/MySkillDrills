package com.hadenhiles.skilldrills

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hadenhiles.skilldrills.models.Activity
import com.hadenhiles.skilldrills.models.Drill
import com.hadenhiles.skilldrills.models.DrillType
import com.hadenhiles.skilldrills.models.activites.Baseball
import com.hadenhiles.skilldrills.models.activites.Golf
import com.hadenhiles.skilldrills.models.activites.Hockey
import com.hadenhiles.skilldrills.models.drilltypes.Duration
import com.hadenhiles.skilldrills.models.drilltypes.DurationCountdown
import com.hadenhiles.skilldrills.models.drilltypes.Score
import kotlinx.android.synthetic.main.activity_add_drill.*
import kotlinx.android.synthetic.main.fragment_drills.*


class AddDrillActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_drill)

        addDrillToolbar.setNavigationOnClickListener {
            finish()
        }

        // Populate activities dropdown
        val activities: List<Activity> = listOf(Hockey(), Golf(), Baseball())
        val activityAdapter: ArrayAdapter<Activity> = ArrayAdapter<Activity>(applicationContext, android.R.layout.simple_spinner_dropdown_item, activities)
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        activitySpinner.adapter = activityAdapter

        // Populate activities dropdown
        val drillTypes: List<DrillType> = listOf(Duration(), DurationCountdown(), Score())
        val drillTypeAdapter: ArrayAdapter<DrillType> = ArrayAdapter<DrillType>(applicationContext, android.R.layout.simple_spinner_dropdown_item, drillTypes)
        drillTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        drillTypeSpinner.adapter = drillTypeAdapter

        // When the user selects/changes the drill activity type
        activitySpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                // Populate categories dropdown based on selected activity
                var categories: List<String> = listOf()
                val selectedActivity = activitySpinner.selectedItem.toString()

                if (selectedActivity == "Hockey") {
                    categories = Hockey().categories?: listOf("Other")
                } else if (selectedActivity == "Golf") {
                    categories = Golf().categories?: listOf("Other")
                } else if (selectedActivity == "Baseball") {
                    categories = Baseball().categories?: listOf("Other")
                }

                categorySpinner.adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, categories)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        saveDrillButton.setOnClickListener {
            // gather fields
            val name: String? = drillName.text.toString().trim()
            val description: String? = drillDescription.text.toString().trim()
            val selectedActivity = activitySpinner.selectedItem.toString()
            val selectedCategory = categorySpinner.selectedItem.toString()
            val selectedDrillType = drillTypeSpinner.selectedItem.toString()

            // Validate that a name has been entered
            if (!TextUtils.isEmpty(name)) {
                // Need to make sure we have a user uid to associate with the drill
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    // capture inputs into an instance of the Drill class
                    val drill = Drill()
                    drill.name = name
                    drill.description = description
                    drill.activity = Activity().fromName(selectedActivity)
                    drill.category = selectedCategory
                    drill.drillType = DrillType().fromDescriptor(selectedDrillType)

                    // connect & save to Firebase
                    val db = FirebaseFirestore.getInstance().collection("drills").document(user.uid).collection("drills")
                    drill.id = db.document().id
                    db.document(drill.id!!).set(drill)

                    // Let the user know it worked
                    Toast.makeText(this, "Drill added", Toast.LENGTH_SHORT).show()
                    // Send the user back to the drills list view
                    finish()
                } else {
                    // Let the user know it didn't work
                    Toast.makeText(this, "Failed to add drill", Toast.LENGTH_SHORT).show()

                    // Send the user back to the drills list view
                    finish()
                }
            } else {
                // Tell the user a name is required
                drillName.error = "Drill name cannot be empty"
                Toast.makeText(this, "Please enter a drill name", Toast.LENGTH_SHORT).show()
            }
        }
    }
}