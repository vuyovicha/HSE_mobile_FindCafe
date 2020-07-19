package com.example.new_bottom_navigation_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.set_preferences_fragment.*

class SetPreferencesEstablishmentTypeFragment : Fragment() {

    companion object {
        const val TAG = "SetPreferencesEstablishmentTypeFragment"
    }

    private val model: SharedViewModel by activityViewModels()
    private lateinit var adapter: SetPreferencesFragmentAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.set_preferences_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SetPreferencesFragmentAdapter()
        adapter.replaceItems(MainActivity.establishmentTypeOptions)
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

        model.data.observe(viewLifecycleOwner, Observer {
            adapter.notifyDataSetChanged()
        })
    }

}