package com.example.new_bottom_navigation_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.new_bottom_navigation_ui.R
import com.example.new_bottom_navigation_ui.ui.home.SharedViewModel
import kotlinx.android.synthetic.main.find_fragment.*

class FindFragment : Fragment() {

    companion object {
        const val TAG = "FindFragment"
    }

    private val model: SharedViewModel by activityViewModels()
    private lateinit var adapter: FindFragmentAdapter

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
        val findFragmentRows = mutableListOf<FindFragmentAdapter.FindFragmentRow>()
        findFragmentRows.add(FindFragmentAdapter.Header("Route"))
        findFragmentRows.add(FindFragmentAdapter.Route("From"))
        findFragmentRows.add(FindFragmentAdapter.Route("To"))
        findFragmentRows.add(FindFragmentAdapter.Header("Cafe preferences"))
        findFragmentRows.add(FindFragmentAdapter.PreferenceList("Establishment type"))
        findFragmentRows.add(FindFragmentAdapter.PreferenceList("Cousin"))
        findFragmentRows.add(FindFragmentAdapter.PreferenceList("Dietary restrictions"))
        findFragmentRows.add(FindFragmentAdapter.PreferencePrice(0))
        findFragmentRows.add(FindFragmentAdapter.PreferenceRating(3.toFloat()))
        findFragmentRows.add(FindFragmentAdapter.PreferenceOpenNow(true))

        adapter = FindFragmentAdapter(requireActivity().supportFragmentManager)
        adapter.replaceItems(findFragmentRows)
        find_fragment_recycler.adapter = adapter
        find_fragment_recycler.layoutManager = LinearLayoutManager(context)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }
}