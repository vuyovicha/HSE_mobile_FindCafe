package com.example.new_bottom_navigation_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.find_fragment.*
import kotlinx.android.synthetic.main.result_fragment.*
import kotlinx.android.synthetic.main.result_fragment.find_fragment_recycler


class ResultFragment : Fragment() {

    companion object {
        const val TAG = "ResultFragment"
    }

    private val model: SharedViewModel by activityViewModels()
    private lateinit var adapter: ResultFragmentAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View?  = inflater.inflate(R.layout.result_fragment, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ResultFragmentAdapter(requireActivity().supportFragmentManager)
        adapter.replaceItems(MainActivity.foundRestaurants)
        find_fragment_recycler.adapter = adapter
        find_fragment_recycler.layoutManager = LinearLayoutManager(context)

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser) requireActivity().nav_view.menu.getItem(2).isChecked = true
    }

}