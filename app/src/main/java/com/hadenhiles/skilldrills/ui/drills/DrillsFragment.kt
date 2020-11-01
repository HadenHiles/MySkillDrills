package com.hadenhiles.skilldrills.ui.drills

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.hadenhiles.skilldrills.R

class DrillsFragment : Fragment() {

    private lateinit var drillsViewModel: DrillsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        drillsViewModel = ViewModelProviders.of(this).get(DrillsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_drills, container, false)
        val textView: TextView = root.findViewById(R.id.text_drills)

        drillsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        return root
    }

}