package com.hadenhiles.skilldrills.ui.routines

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hadenhiles.skilldrills.R
import com.hadenhiles.skilldrills.models.Drill
import com.hadenhiles.skilldrills.models.Routine
import kotlinx.android.synthetic.main.activity_add_routine.*


class RoutineActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    private var user = FirebaseAuth.getInstance().currentUser
    private var userUid = user?.uid ?: ""
    var routineId: String? = null

    // Initialize our adapter for the routine drills recycler
    private lateinit var drillsAdapter: DrillsAdapter
    private var selectedDrills: MutableList<Drill> = mutableListOf<Drill>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_routine)

        // Get the routine id if passed so we can populate the routine data
        routineId = intent.getStringExtra("id")

        drillsRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        drillsAdapter = DrillsAdapter(selectedDrills)
        drillsRecyclerView.adapter = drillsAdapter

        if (!routineId.isNullOrEmpty()) {
            // Populate the routine's data and drills from Firestore
            db.collection("routines").document(userUid).collection("routines")
                .whereEqualTo("id", routineId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        // Unpack the routine into our Routine model class
                        val routine = document.toObject(Routine::class.java)
                        // Set the name, notes, etc.
                        routineToolbar.title = routine.name
                        routineName.setText(routine.name)
                        routineNote.setText(routine.note)

                        // Update the list of drills and tell our adapter that we've updated it's data
                        selectedDrills.addAll(routine.drills)
                        drillsAdapter.notifyDataSetChanged()
                    }
                }
                .addOnFailureListener { exception ->
                    println("Error getting documents: $exception")
                }
        } else {
            // Hide the delete button
            routineToolbar.menu.findItem(R.id.delete).isVisible = false
        }

        routineToolbar.setNavigationOnClickListener {
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
                    dialogBuilder.setItems(
                        drillsList
                    ) { dialogInterface, item ->
                        selectedDrills.add(drills[item])
                        drillsAdapter.notifyDataSetChanged()
                    }

                    //Create alert dialog object via builder
                    val alertDialogObject = dialogBuilder.create()

                    //Show the dialog
                    alertDialogObject.show()
                }
        }

        saveRoutineButton.setOnClickListener {
            // Validate that a name has been entered
            val name: String = routineName.text.toString().trim()
            if (!TextUtils.isEmpty(name)) {
                // Need to make sure we have a user uid to associate with the drill
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    // capture inputs into an instance of the Drill class
                    val routine = Routine()
                    // Update the routine properties with the new data
                    routine.name = name
                    routine.note = routineNote.text.toString().trim()
                    routine.drills = selectedDrills

                    // connect & save to Firebase
                    val db =
                        FirebaseFirestore.getInstance().collection("routines").document(user.uid)
                            .collection("routines")
                    routine.id = routineId?: db.document().id
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

        if (!routineId.isNullOrEmpty()) {
            routineToolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete -> {
                        // Handle delete icon press
                        val routineReference: DocumentReference =
                            db.collection("routines").document(userUid)
                                .collection("routines").document(routineId!!)
                        routineReference.delete().addOnSuccessListener { aVoid: Void? ->
                            Toast.makeText(this, "Routine deleted", Toast.LENGTH_SHORT).show()
                        }
                        finish()
                        true
                    }
                    else -> false
                }
            }
        }
    }


    private inner class DrillsAdapter(private val dataSet: List<Drill>) :
        RecyclerView.Adapter<DrillsAdapter.ViewHolder>() {

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val nameTextView: TextView = view.findViewById(R.id.nameTextView)
            val activityTextView: TextView = view.findViewById(R.id.activityTextView)
            val categoryTextView: TextView = view.findViewById(R.id.categoryTextView)
            val removeDrillButton: ImageButton = view.findViewById(R.id.removeDrillButton)
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view, which defines the UI of the list item
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_drill, viewGroup, false)

            return ViewHolder(view)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            viewHolder.nameTextView.text = dataSet[position].name
            viewHolder.activityTextView.text = dataSet[position].activity?.name
            viewHolder.categoryTextView.text = dataSet[position].category

            viewHolder.removeDrillButton.setOnClickListener {
                selectedDrills.remove(dataSet[position])
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, selectedDrills.count())
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size

    }

}