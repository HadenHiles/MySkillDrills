package com.hadenhiles.skilldrills.ui.drills

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DrillsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is the Drills Fragment"
    }
    val text: LiveData<String> = _text
}