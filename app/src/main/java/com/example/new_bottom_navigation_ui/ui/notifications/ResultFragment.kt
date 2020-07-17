package com.example.new_bottom_navigation_ui.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.new_bottom_navigation_ui.R
import com.example.new_bottom_navigation_ui.ui.home.SharedViewModel

class NotificationsFragment : Fragment() {

    private val model: SharedViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        model.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}