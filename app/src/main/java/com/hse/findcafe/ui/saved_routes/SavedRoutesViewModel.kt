package com.hse.findcafe.ui.saved_routes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SavedRoutesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is saved routes Fragment"
    }
    val text: LiveData<String> = _text
}