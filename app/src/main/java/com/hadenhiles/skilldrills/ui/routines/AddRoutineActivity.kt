package com.hadenhiles.skilldrills.ui.routines

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hadenhiles.skilldrills.R
import com.hadenhiles.skilldrills.models.Drill
import com.hadenhiles.skilldrills.models.Routine
import kotlinx.android.synthetic.main.activity_add_routine.*


class AddRoutineActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    private var user = FirebaseAuth.getInstance().currentUser
    private var userUid = user?.uid ?: ""

    lateinit var drillsAdapter: ArrayAdapter<String>
    lateinit var listView: ListView
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var dialog: AlertDialog
    var drills: List<String> = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_routine)

        addRoutineToolbar.setNavigationOnClickListener {
            finish()
        }

        // query the db for all restaurants
//        val drillsSnapshot = db.collection("drills").document(userUid).collection("drills").orderBy(
//            "name",
//            Query.Direction.ASCENDING
//        ).get()
//
//
//        for (drill in drillsSnapshot.result!!) {
//            drills.toMutableList().add(drill.toObject(Drill::class.java).name.toString())
//        }
//
//        addDrillButton.setOnClickListener {
//            openDialog()
//        }

        saveRoutineButton.setOnClickListener {
            // gather fields
            val name: String? = routineName.text.toString().trim()
            val note: String? = routineNote.text.toString().trim()

            // Validate that a name has been entered
            if (!TextUtils.isEmpty(name)) {
                // Need to make sure we have a user uid to associate with the drill
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    // capture inputs into an instance of the Drill class
                    val drill = Routine()
                    drill.name = name
                    drill.note = note

                    // connect & save to Firebase
                    val db =
                        FirebaseFirestore.getInstance().collection("routines").document(user.uid)
                            .collection("routines")
                    drill.id = db.document().id
                    db.document(drill.id!!).set(drill)

                    // Let the user know it worked
                    Toast.makeText(this, "Routine created", Toast.LENGTH_SHORT).show()
                    // Send the user back to the drills list view
                    finish()
                } else {
                    // Let the user know it didn't work
                    Toast.makeText(this, "Failed to create routine", Toast.LENGTH_SHORT).show()

                    // Send the user back to the drills list view
                    finish()
                }
            } else {
                // Tell the user a name is required
                routineName.error = "Routine name cannot be empty"
                Toast.makeText(this, "Please enter a name for your routine", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    fun openDialog() {
        alertDialog = AlertDialog.Builder(this)
        val rowList: View = layoutInflater.inflate(R.layout.fragment_drills, null)
        listView = rowList.findViewById(R.id.drillItem)
        drillsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, drills)
        listView.adapter = drillsAdapter
        drillsAdapter.notifyDataSetChanged()
        alertDialog.setView(rowList)
        dialog = alertDialog.create()
        dialog.show()
    }
    fun closeDialog(view: View) {
        dialog.dismiss()
        Toast.makeText(baseContext, "Dialog Closed", Toast.LENGTH_SHORT).show()
    }
}