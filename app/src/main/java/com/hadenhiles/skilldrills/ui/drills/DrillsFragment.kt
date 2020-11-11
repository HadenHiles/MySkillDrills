package com.hadenhiles.skilldrills.ui.drills

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hadenhiles.skilldrills.AddDrillActivity
import com.hadenhiles.skilldrills.R
import kotlinx.android.synthetic.main.fragment_drills.*

class DrillsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_drills, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addDrillButton.setOnClickListener {
            val intent = Intent(context?.applicationContext, AddDrillActivity::class.java)
            startActivity(intent)
        }
    }

}