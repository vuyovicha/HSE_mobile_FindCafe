package com.example.new_bottom_navigation_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.set_preferences_fragment.back_button
import kotlinx.android.synthetic.main.set_preferences_fragment.set_preferences_fragment_recycler
import kotlinx.android.synthetic.main.set_preferences_fragment_search.*

class SetPreferencesCousinFragment : Fragment() {

    companion object {
        const val TAG = "SetPreferencesCousinFragment"
    }

    private val model: SharedViewModel by activityViewModels()
    private lateinit var adapter: SetPreferencesSearchFragmentAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.set_preferences_fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SetPreferencesSearchFragmentAdapter(MainActivity.cousineOptions)
        set_preferences_fragment_recycler.adapter = adapter
        set_preferences_fragment_recycler.layoutManager = LinearLayoutManager(context)

        back_button.setOnClickListener {
            model.changeData()

            val supportFragmentManager = requireActivity().supportFragmentManager
            val fragmentTag = FindFragment::class.java.simpleName

            supportFragmentManager.commit {
                supportFragmentManager.fragments.forEach { hide(it) }
                val fragment = supportFragmentManager.findFragmentByTag(fragmentTag)
                if (fragment != null) {
                    show(fragment)
                } else {
                    val nextFragment = FindFragment()
                    add(R.id.nav_host_fragment, nextFragment, nextFragment::class.java.simpleName)
                }
            }
        }

        search_preferences.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

        })

        model.data.observe(viewLifecycleOwner, Observer {
            adapter.notifyDataSetChanged()
            search_preferences.setQuery("", false)
            search_preferences.clearFocus()
        })

    }


}