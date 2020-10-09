package com.hadenhiles.myskilldrills.ui.routines

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.hadenhiles.myskilldrills.R

class RoutinesFragment : Fragment() {

    private lateinit var routinesViewModel: RoutinesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        routinesViewModel = ViewModelProviders.of(this).get(RoutinesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_routines, container, false)
        val textView: TextView = root.findViewById(R.id.text_routines)

        routinesViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        return root
    }

}