package com.example.new_bottom_navigation_ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.set_route_row.view.*


class SetRouteFragmentAdapter (private val man : FragmentManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var SetRouteFragmentRows: List<Result> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SetRouteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.set_route_row, parent, false), man)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SetRouteViewHolder).onBindSetPreferencesViewHolder(SetRouteFragmentRows[position])
    }

    override fun getItemCount() = SetRouteFragmentRows.count()

    fun replaceItems(items: List<Result>) {
        this.SetRouteFragmentRows = items
        notifyDataSetChanged()
    }


    inner class SetRouteViewHolder(private val root: View, private val manager : FragmentManager) : RecyclerView.ViewHolder(root) {
        private val text: TextView = root.location

        fun onBindSetPreferencesViewHolder(location : Result) {
            text.text = location.address.freeformAddress

            text.setOnClickListener {
                val point  = DisplayAddress(
                    location.address.freeformAddress,
                    PointString(location.position.lat.toString(), location.position.lon.toString()))
                if (MainActivity.routePosition == 0) MainActivity.fromAddress = point
                else MainActivity.toAddress = point

                val supportFragmentManager = manager
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



                    val thisFragment: Fragment? =
                        manager.findFragmentByTag(SetRouteFragment.TAG)
                    if (thisFragment != null) manager.beginTransaction().remove(thisFragment)
                        .commit()
                }

            }

        }
    }
}