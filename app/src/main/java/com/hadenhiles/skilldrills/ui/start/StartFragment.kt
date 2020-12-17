package com.hadenhiles.skilldrills.ui.start

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.se.omapi.Session
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hadenhiles.skilldrills.MainActivity
import com.hadenhiles.skilldrills.R
import com.hadenhiles.skilldrills.models.Routine
import com.hadenhiles.skilldrills.ui.routines.RoutineActivity
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_routines.*
import kotlinx.android.synthetic.main.fragment_start.*
import kotlinx.android.synthetic.main.fragment_start.routinesRecyclerView
import kotlinx.android.synthetic.main.item_routine.view.*


class StartFragment : Fragment() {

    // connect to Firestore
    val db = FirebaseFirestore.getInstance()
    private var adapter: StartFragment.StartRoutineAdapter? = null
    private var user = FirebaseAuth.getInstance().currentUser
    private var userUid = user?.uid?: ""
    private var sessionTimer: Chronometer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set our recyclerview to use LinearLayout
        routinesRecyclerView.layoutManager = LinearLayoutManager(context?.applicationContext)

        // Create a reference to the session timer
        sessionTimer = requireActivity().findViewById<Chronometer>(R.id.sessionTimer)

        // query the db for all restaurants
        val query = db.collection("routines").document(userUid).collection("routines").orderBy(
            "name",
            Query.Direction.ASCENDING
        )

        // pass query results to the Recycler adapter
        val options = FirestoreRecyclerOptions.Builder<Routine>().setQuery(
            query,
            Routine::class.java
        ).build()
        adapter = StartRoutineAdapter(options)
        routinesRecyclerView.adapter = adapter

        // Start an empty session
        startSessionButton.setOnClickListener {
            (activity as MainActivity?)?.startSession()
        }
    }



    // tell adapter to start watching data for changes
    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        if (adapter != null) {
            adapter!!.stopListening()
        }
    }

    override fun onResume() {
        super.onResume()
        adapter!!.notifyDataSetChanged()
    }

    private inner class StartRoutineViewHolder internal constructor(private val view: View): RecyclerView.ViewHolder(
        view
    ){}
    private inner class StartRoutineAdapter internal constructor(options: FirestoreRecyclerOptions<Routine>): FirestoreRecyclerAdapter<Routine, StartRoutineViewHolder>(
        options
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StartRoutineViewHolder {
            // Inflate the item_restaurant.xml layout template to populate the recyclerview
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_routine,
                parent,
                false
            )

            return StartRoutineViewHolder(view)
        }

        override fun onBindViewHolder(holder: StartRoutineViewHolder, position: Int, model: Routine) {
            // populate routine data into routine item layout for each routine
            holder.itemView.nameTextView.text = model.name

            if(position % 2 == 0) {
                holder.itemView.setBackgroundColor(Color.parseColor("#f5f5f5"))
            }

            holder.itemView.setOnClickListener {
                // Start the session with the selected routine
                (activity as MainActivity?)?.startSession(model.id)
            }

            holder.itemView.removeRoutineButton.isVisible = false
        }

    }
}