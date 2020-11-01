package com.hadenhiles.skilldrills.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hadenhiles.skilldrills.R

class StartFragment : Fragment() {

    private lateinit var startViewModel: StartViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        startViewModel =
                ViewModelProviders.of(this).get(StartViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_start, container, false)
        val textView: TextView = root.findViewById(R.id.text_start)
        startViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}