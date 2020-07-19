package com.example.new_bottom_navigation_ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.new_bottom_navigation_ui.MainActivity
import kotlinx.coroutines.MainScope



class SharedViewModel : ViewModel() {

    private val initData = MutableLiveData<Int>().apply {
        value = 1
    }

    val data : MutableLiveData<Int> = initData

    fun changeData() {
        data.value = data.value?.plus(1)
    }


}