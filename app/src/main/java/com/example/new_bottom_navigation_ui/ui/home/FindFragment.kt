package com.example.new_bottom_navigation_ui.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.new_bottom_navigation_ui.R

class HomeFragment : Fragment() {

    private val model: SharedViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        model.text.observe(viewLifecycleOwner, Observer {
            //use it here
        })
        return root
    }
}