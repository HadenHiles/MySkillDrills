package com.hadenhiles.skilldrills.ui.history

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hadenhiles.skilldrills.R
import com.hadenhiles.skilldrills.models.Session
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.item_session.view.*
import java.sql.Timestamp

class HistoryFragment : Fragment() {

    // connect to Firestore
    val db = FirebaseFirestore.getInstance()
    private var adapter: SessionAdapter? = null
    private var user = FirebaseAuth.getInstance().currentUser
    private var userUid = user?.uid?: ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set our recyclerview to use LinearLayout
        sessionsRecyclerView.layoutManager = LinearLayoutManager(context?.applicationContext)

        // query the db for all restaurants
        val query = db.collection("sessions").document(userUid).collection("sessions").orderBy("timestamp", Query.Direction.DESCENDING)

        // pass query results to the Recycler adapter
        val options = FirestoreRecyclerOptions.Builder<Session>().setQuery(query, Session::class.java).build()
        adapter = SessionAdapter(options)
        sessionsRecyclerView.adapter = adapter
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

    private inner class SessionViewHolder internal constructor(private val view: View): RecyclerView.ViewHolder(view){}
    private inner class SessionAdapter internal constructor(options: FirestoreRecyclerOptions<Session>): FirestoreRecyclerAdapter<Session, SessionViewHolder>(options) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
            // Inflate the item_restaurant.xml layout template to populate the recyclerview
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_session, parent, false)

            return SessionViewHolder(view)
        }

        override fun onBindViewHolder(holder: SessionViewHolder, position: Int, model: Session) {
            // populate routine data into routine item layout for each routine
            holder.itemView.nameTextView.text = model.name

            val totalSecs = model.duration?: 0
            val hours = totalSecs / 3600
            val minutes = (totalSecs % 3600) / 60
            val seconds = totalSecs % 60
            holder.itemView.durationTextView.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            holder.itemView.timestampTextView.text = model.timestamp.toDate().toString()

            if(position % 2 != 0) {
                holder.itemView.setBackgroundColor(Color.parseColor("#f5f5f5"))
            }

            holder.itemView.setOnClickListener {
                // TODO: Start a new session with the same historical session data
                // TODO: Need to update startSession function in MainActivity to optionally accept a sessionId in addition to the routineId
            }
        }

    }
}