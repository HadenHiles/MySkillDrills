package com.hadenhiles.skilldrills.ui.routines

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hadenhiles.skilldrills.LoginActivity
import com.hadenhiles.skilldrills.R
import com.hadenhiles.skilldrills.models.Routine
import kotlinx.android.synthetic.main.fragment_routines.*
import kotlinx.android.synthetic.main.item_routine.view.*

class RoutinesFragment : Fragment() {

    // connect to Firestore
    val db = FirebaseFirestore.getInstance()
    private var adapter: RoutineAdapter? = null
    private var user = FirebaseAuth.getInstance().currentUser
    private var userUid = user?.uid?: ""

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_routines, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set our recyclerview to use LinearLayout
        routinesRecyclerView.layoutManager = LinearLayoutManager(context?.applicationContext)

        // query the db for all restaurants
        val query = db.collection("routines").document(userUid).collection("routines").orderBy("name", Query.Direction.ASCENDING)

        // pass query results to the Recycler adapter
        val options = FirestoreRecyclerOptions.Builder<Routine>().setQuery(query, Routine::class.java).build()
        adapter = RoutineAdapter(options)
        routinesRecyclerView.adapter = adapter

        addRoutineButton.setOnClickListener {
            val intent = Intent(context?.applicationContext, RoutineActivity::class.java)
            startActivity(intent)
        }
    }

    // tell adapter to start watching data for changes
    override fun onStart() {
        super.onStart()
        adapter!!.startListening()

        // check if the user is signed in (optional but good to know)
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            val intent = Intent(context?.applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
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

    private inner class RoutineViewHolder internal constructor(private val view: View): RecyclerView.ViewHolder(view){}
    private inner class RoutineAdapter internal constructor(options: FirestoreRecyclerOptions<Routine>): FirestoreRecyclerAdapter<Routine, RoutineViewHolder>(options) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
            // Inflate the item_restaurant.xml layout template to populate the recyclerview
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_routine, parent, false)

            return RoutineViewHolder(view)
        }

        override fun onBindViewHolder(holder: RoutineViewHolder, position: Int, model: Routine) {
            // populate routine data into routine item layout for each routine
            holder.itemView.nameTextView.text = model.name

            if(position % 2 != 0){
                holder.itemView.setBackgroundColor(Color.parseColor("#f5f5f5"))
            }

            holder.itemView.setOnClickListener {
                // Edit the routine
                val intent = Intent(context?.applicationContext, RoutineActivity::class.java)
                intent.putExtra("id", model.id)
                startActivity(intent)
            }
        }

    }

}