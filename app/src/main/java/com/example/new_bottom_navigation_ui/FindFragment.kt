package com.example.new_bottom_navigation_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.new_bottom_navigation_ui.R
import com.example.new_bottom_navigation_ui.ui.home.SharedViewModel
import kotlinx.android.synthetic.main.find_fragment.*

class FindFragment : Fragment() {

    companion object {
        const val TAG = "FindFragment"
    }

    private val model: SharedViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.find_fragment, container, false)
//        model.text.observe(viewLifecycleOwner, Observer {
//            //use it here
//        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}