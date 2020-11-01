package com.hadenhiles.skilldrills.ui.routines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RoutinesViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is the Routines Fragment"
    }
    val text: LiveData<String> = _text
}