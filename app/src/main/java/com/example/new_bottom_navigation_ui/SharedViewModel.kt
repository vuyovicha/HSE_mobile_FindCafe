package com.example.new_bottom_navigation_ui.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.new_bottom_navigation_ui.MainActivity
import kotlinx.coroutines.MainScope


class SharedViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private fun readJSON () {


    }


}