package com.hadenhiles.skilldrills.ui.drills

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hadenhiles.skilldrills.R.*
import com.hadenhiles.skilldrills.models.Drill
import kotlinx.android.synthetic.main.fragment_drills.*
import kotlinx.android.synthetic.main.item_drill.*
import kotlinx.android.synthetic.main.item_drill.view.*

class DrillsFragment : Fragment() {

    // connect to Firestore
    val db = FirebaseFirestore.getInstance()
    private var adapter: DrillAdapter? = null
    private var user = FirebaseAuth.getInstance().currentUser
    private var userUid = user?.uid ?: ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layout.fragment_drills, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set our recyclerview to use LinearLayout
        drillsRecyclerView.layoutManager = LinearLayoutManager(context?.applicationContext)

        // query the db for all restaurants
        val query = db.collection("drills").document(userUid).collection("drills").orderBy(
            "name",
            Query.Direction.ASCENDING
        )

        // pass query results to the Recycler adapter
        val options =
            FirestoreRecyclerOptions.Builder<Drill>().setQuery(query, Drill::class.java).build()
        adapter = DrillAdapter(options)
        drillsRecyclerView.adapter = adapter

        addDrillButton.setOnClickListener {
            val intent = Intent(context?.applicationContext, AddDrillActivity::class.java)
            startActivity(intent)
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

    private inner class DrillViewHolder internal constructor(private val view: View) :
        RecyclerView.ViewHolder(view) {}

    private inner class DrillAdapter internal constructor(options: FirestoreRecyclerOptions<Drill>) :
        FirestoreRecyclerAdapter<Drill, DrillViewHolder>(
            options
        ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrillViewHolder {
            // Inflate the item_restaurant.xml layout template to populate the recyclerview
            val view = LayoutInflater.from(parent.context).inflate(layout.item_drill, parent, false)

            return DrillViewHolder(view)
        }

        override fun onBindViewHolder(holder: DrillViewHolder, position: Int, model: Drill) {
            // populate drill data into drill item layout for each drill
            holder.itemView.nameTextView.text = model.name
            holder.itemView.activityTextView.text = model.activity?.name
            holder.itemView.categoryTextView.text = model.category

            if (position % 2 != 0) {
                holder.itemView.setBackgroundColor(Color.parseColor("#f5f5f5"))
            }

            holder.itemView.setOnClickListener {
                // Edit the drill
                val intent = Intent(context, EditDrillActivity::class.java)
                intent.putExtra("drillId", model.id)
                startActivity(intent)
            }

            holder.itemView.removeDrillButton.setOnClickListener {
                if (!model.id.isNullOrEmpty()) {
                    db.collection("drills").document(userUid).collection("drills").document(model.id!!)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(context?.applicationContext, "Drill deleted", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e -> println("Error deleting document $e") }
                }
            }
        }
    }
}