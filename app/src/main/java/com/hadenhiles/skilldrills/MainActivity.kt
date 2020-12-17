package com.hadenhiles.skilldrills

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hadenhiles.skilldrills.models.Drill
import com.hadenhiles.skilldrills.models.Routine
import com.hadenhiles.skilldrills.models.Session
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_routine.*
import java.time.Duration
import java.time.temporal.TemporalUnit
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    // connect to Firestore
    val db = FirebaseFirestore.getInstance()
    private var adapter: DrillsAdapter? = null
    private var user = FirebaseAuth.getInstance().currentUser
    private var userUid = user?.uid?: ""
    private var sessionTimerRunning: Boolean = false

    // Initialize our adapter for the routine drills recycler
    private lateinit var drillsAdapter: DrillsAdapter
    private var sessionDrills: MutableList<Drill> = mutableListOf<Drill>()

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

        // Initialize the empty session drills recycler
        sessionDrillsRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        drillsAdapter = DrillsAdapter(sessionDrills)
        sessionDrillsRecyclerView.adapter = drillsAdapter

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

        // Add a click handler for the session add drill button
        addSessionDrillButton.setOnClickListener {
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
                        sessionDrills.add(drills[item])
                        drillsAdapter.notifyDataSetChanged()
                    }

                    //Create alert dialog object via builder
                    val alertDialogObject = dialogBuilder.create()

                    //Show the dialog
                    alertDialogObject.show()
                }
        }

        // Allow the user to finish a session - give them the option to save or discard (for history and potentially give option to update original routine values as well)
        finishButton.setOnClickListener {
            // setup reference to the bottom sheet
            val sessionBottomSheetBehaviour = BottomSheetBehavior.from(mainContainer.sessionBottomSheet)
            sessionBottomSheetBehaviour.peekHeight = 200
            mainContainer.navigationContainer.setPadding(0, 0, 0, 200)

            val builder = AlertDialog.Builder(this, R.style.AlertDialog)
            builder.setMessage("Would you like to save this session?")
                .setCancelable(true)
                .setPositiveButton("Save") { dialog, id ->
                    // Save the session to firestore
                    if (user != null) {
                        // capture inputs into an instance of the Drill class
                        val session = Session()
                        // Update the routine properties with the new data
                        session.name = sessionTitleTextView.text.toString().trim()
                        session.note = sessionNoteEditText.text.toString().trim()
                        session.drills = sessionDrills
                        session.duration = Duration.ofMillis(SystemClock.elapsedRealtime() - sessionTimer.base)

                        // connect & save to Firebase
                        val db = FirebaseFirestore.getInstance().collection("sessions").document(userUid)
                                .collection("sessions")
                        session.id = db.document().id
                        db.document(session.id!!).set(session)

                        // Reset the session bottom sheet content
                        sessionTitleTextView.text = getString(R.string.default_empty_session_title)
                        sessionNoteEditText.setText("")
                        sessionDrills.removeAll(sessionDrills)
                        drillsAdapter.notifyDataSetChanged()

                        // Close the session
                        sessionTimer.base = SystemClock.elapsedRealtime()
                        sessionTimer.stop()
                        sessionTimerRunning = false

                        // Hide the bottom sheet
                        sessionBottomSheetBehaviour.peekHeight = 0
                        mainContainer.navigationContainer.setPadding(0, 0, 0, 0)
                        sessionBottomSheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED

                        // Let the user know it worked
                        Toast.makeText(this, "Session saved to history", Toast.LENGTH_SHORT).show()
                    } else {
                        // Let the user know it didn't work
                        Toast.makeText(this, "Failed to save session", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Discard") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()

                    // Reset the session bottom sheet content
                    sessionTitleTextView.text = getString(R.string.default_empty_session_title)
                    sessionNoteEditText.setText("")
                    sessionDrills.removeAll(sessionDrills)
                    drillsAdapter.notifyDataSetChanged()

                    // Close the session
                    sessionTimer.base = SystemClock.elapsedRealtime()
                    sessionTimer.stop()
                    sessionTimerRunning = false

                    // Hide the bottom sheet
                    sessionBottomSheetBehaviour.peekHeight = 0
                    mainContainer.navigationContainer.setPadding(0, 0, 0, 0)
                    sessionBottomSheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            val alert = builder.create()
            alert.show()
        }
    }

    fun startSession(routineId: String? = null) {
        // setup reference to the bottom sheet
        val sessionBottomSheetBehaviour = BottomSheetBehavior.from(mainContainer.sessionBottomSheet)
        sessionBottomSheetBehaviour.peekHeight = 200
        mainContainer.navigationContainer.setPadding(0, 0, 0, 200)

        // Check if there is an existing session or not
        if (sessionTimerRunning) {
            // Ask the user if they would like to override the existing session with a new one
            val builder = AlertDialog.Builder(this, R.style.AlertDialog)
            builder.setMessage("Override your current session?")
                .setCancelable(true)
                .setPositiveButton("Yes") { dialog, id ->
                    // Reset the session title and note
                    sessionTitleTextView.text = getString(R.string.default_empty_session_title)
                    sessionNoteEditText.setText("")

                    // Empty the current drills
                    sessionDrills.removeAll(sessionDrills)

                    // Setup the session with the selected routine data if routine id is provided
                    if (!routineId.isNullOrEmpty()) {
                        loadRoutineDrills(routineId)
                    }

                    // Restart the session
                    sessionTimer.base = SystemClock.elapsedRealtime()
                    sessionTimer.stop()
                    sessionTimer.start()

                    // Reveal the bottom sheet
                    sessionBottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
                }
                .setNegativeButton("Cancel") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        } else {
            if (!routineId.isNullOrEmpty()) {
                loadRoutineDrills(routineId)
            }

            // Start the session
            sessionTimer.base = SystemClock.elapsedRealtime()
            sessionTimer.stop()
            sessionTimer.start()
            sessionTimerRunning = true

            // Reveal the bottom sheet
            sessionBottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun loadRoutineDrills(routineId: String) {
        // Populate our session with the routine's drills from Firestore
        db.collection("routines").document(userUid).collection("routines")
            .whereEqualTo("id", routineId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Unpack the routine into our Routine model class
                    val routine = document.toObject(Routine::class.java)

                    // Set the session title/note to the routine name/note
                    sessionTitleTextView.text = routine.name
                    sessionNoteEditText.setText(routine.note)

                    // Update the list of session drills and tell our adapter that we've updated it's data
                    sessionDrills.addAll(routine.drills)
                    drillsAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }

    // For populating the session drills recycler view
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
                sessionDrills.remove(dataSet[position])
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, sessionDrills.count())
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size

    }
}