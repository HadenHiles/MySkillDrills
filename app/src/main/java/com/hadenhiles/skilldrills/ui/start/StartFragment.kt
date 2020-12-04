package com.hadenhiles.skilldrills.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.hadenhiles.skilldrills.R
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_start.*


class StartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startSessionButton.setOnClickListener {
            val mainContainer = requireActivity().findViewById<View>(R.id.mainContainer)
            val sessionBottomSheetBehaviour = BottomSheetBehavior.from(mainContainer.sessionBottomSheet)
            sessionBottomSheetBehaviour.peekHeight = 200
            mainContainer.navigationContainer.setPadding(0, 0, 0, 200)
            sessionBottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
}