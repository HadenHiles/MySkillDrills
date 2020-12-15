package com.hadenhiles.skilldrills.ui.routines

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_routine)

        addRoutineToolbar.setNavigationOnClickListener {
            finish()
        }

        addDrillButton.setOnClickListener {
            val drillNames: MutableList<String> = ArrayList()
            val drills: MutableList<Drill> = mutableListOf<Drill>()

            // query the db for all drills
            db.collection("drills").document(userUid).collection("drills").orderBy(
                "name",
                Query.Direction.ASCENDING
            ).get()
                .addOnSuccessListener { documents ->
                    for (drill in documents) {
                        drills.add(drill.toObject(Drill::class.java))
                        drillNames.add(drill.toObject(Drill::class.java).name.toString())
                    }

                    //Create sequence of items
                    val drillsList: Array<String> = drillNames.toTypedArray<String>()
                    val dialogBuilder = AlertDialog.Builder(this)
                    dialogBuilder.setTitle("Select a Drill")
                    dialogBuilder.setItems(drillsList
                    ) { dialogInterface, arg ->

                    }

                    //Create alert dialog object via builder
                    val alertDialogObject = dialogBuilder.create()

                    //Show the dialog
                    alertDialogObject.show()
                }
        }

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
                    val routine = Routine()
                    routine.name = name
                    routine.note = note

                    // connect & save to Firebase
                    val db =
                        FirebaseFirestore.getInstance().collection("routines").document(user.uid)
                            .collection("routines")
                    routine.id = db.document().id
                    db.document(routine.id!!).set(routine)

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
}