package com.hadenhiles.skilldrills

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
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
import kotlinx.android.synthetic.main.activity_add_drill.activitySpinner
import kotlinx.android.synthetic.main.activity_add_drill.categorySpinner
import kotlinx.android.synthetic.main.activity_add_drill.drillDescription
import kotlinx.android.synthetic.main.activity_add_drill.drillName
import kotlinx.android.synthetic.main.activity_add_drill.drillTypeSpinner
import kotlinx.android.synthetic.main.activity_edit_drill.*
import kotlinx.android.synthetic.main.activity_edit_drill.saveDrillButton
import java.lang.Exception

class EditDrillActivity : AppCompatActivity() {
    // Establish a connection to the user's drill collection in firebase
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val userDrills = FirebaseFirestore.getInstance().collection("drills").document(currentUser!!.uid).collection("drills")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_drill)

        // Get the drillId to be edited
        val drillId = intent.getStringExtra("drillId")

        // Close the edit view
        editDrillToolbar.setNavigationOnClickListener {
            finish()
        }

        val doc = userDrills.document(drillId.toString().trim()).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        // Map the firebase drill to our drill model
                        val fbDrill = (it.toObject(Drill::class.java)) ?: Drill()
                        val drill = Drill(fbDrill.id!!, fbDrill.name!!, fbDrill.description!!, fbDrill.activity!!, fbDrill.category!!, fbDrill.drillType!!)

                        drillName.setText(drill.name)
                        drillDescription.setText(drill.description)

                        // Populate activities dropdown
                        val activities: List<String> = listOf(Hockey().name!!, Golf().name!!, Baseball().name!!)
                        val activityAdapter: ArrayAdapter<String> = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, activities)
                        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        activitySpinner.adapter = activityAdapter
                        // Set selected
                        activitySpinner.setSelection(activityAdapter.getPosition(drill.activity?.name))

                        // Populate categories dropdown
                        val categoryAdapter: ArrayAdapter<String> = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, getCategories(drill.activity?.name!!))
                        categorySpinner.adapter = categoryAdapter

                        // Populate drill types dropdown
                        val drillTypes: List<String> = listOf(Duration().title!!, DurationCountdown().title!!, Score().title!!)
                        val drillTypeAdapter: ArrayAdapter<String> = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, drillTypes)
                        drillTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        drillTypeSpinner.adapter = drillTypeAdapter
                        // Set selected
                        drillTypeSpinner.setSelection(drillTypeAdapter.getPosition(drill.drillType?.title))

                        // When the user selects/changes the drill activity type
                        activitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                                categorySpinner.adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, getCategories(activitySpinner.selectedItem.toString()))

                                // Set the selected category according to the currently set or selected activity
                                val currentActivityPos = activityAdapter.getPosition(drill.activity?.name)
                                if (position != currentActivityPos) {
                                    // Reset the selected index
                                    categorySpinner.setSelection(0)
                                } else {
                                    categorySpinner.setSelection(categoryAdapter.getPosition(drill.category))
                                }
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }
                        }

                        // Disable the fields we don't want them to update
                        drillTypeSpinner.isEnabled = false
                    } else {
                        // If the drill isn't found then exit the edit drill activity
                        finish()
                    }
                }
                .addOnFailureListener { }

        // Update the drill
        saveDrillButton.setOnClickListener {
            // gather fields
            val name: String? = drillName.text.toString().trim()
            val description: String? = drillDescription.text.toString().trim()
            val selectedActivity = activitySpinner.selectedItem.toString()
            val selectedCategory = categorySpinner.selectedItem.toString()
            val selectedDrillType = drillTypeSpinner.selectedItem.toString()

            // Validate that a name has been entered
            if (!TextUtils.isEmpty(name)) {
                // Firestore rules will ensure only the drill owner can update
                try {
                    // capture inputs into an instance of the Drill class
                    val drill = Drill()
                    drill.name = name
                    drill.description = description
                    drill.activity = Activity().fromName(selectedActivity)
                    drill.category = selectedCategory
                    drill.drillType = DrillType().fromDescriptor(selectedDrillType)

                    // connect & update Firebase drill document
                    val curDrill = FirebaseFirestore.getInstance().collection("drills").document(currentUser!!.uid).collection("drills").document(drillId.toString().trim())
                    curDrill.update(mapOf<String, Any>("name" to drill.name!!, "description" to drill.description!!, "activity" to drill.activity!!, "category" to drill.category!!))

                    // Let the user know it worked
                    Toast.makeText(this, "Drill updated", Toast.LENGTH_SHORT).show()
                    // Send the user back to the drills list view
                    finish()
                } catch (e: Exception) {
                    // Let the user know it didn't work
                    Toast.makeText(this, "Failed to update drill", Toast.LENGTH_SHORT).show()

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

    fun getCategories(selectedActivity: String): List<String> {
        // Populate categories dropdown based on selected activity
        var categories: List<String> = listOf()

        if (selectedActivity == "Hockey") {
            categories = Hockey().categories ?: listOf("Other")
        } else if (selectedActivity == "Golf") {
            categories = Golf().categories ?: listOf("Other")
        } else if (selectedActivity == "Baseball") {
            categories = Baseball().categories ?: listOf("Other")
        }

        return categories
    }
}