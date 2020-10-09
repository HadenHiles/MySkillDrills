package com.hadenhiles.myskilldrills.ui.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StartViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the Start Fragment"
    }
    val text: LiveData<String> = _text
}