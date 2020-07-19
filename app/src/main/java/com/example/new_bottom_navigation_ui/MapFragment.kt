package com.example.new_bottom_navigation_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import com.example.new_bottom_navigation_ui.R
import com.example.new_bottom_navigation_ui.ui.home.SharedViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.map_fragment.*
import kotlinx.android.synthetic.main.map_fragment.view.*

class MapFragment : Fragment(), OnMapReadyCallback {

    companion object {
        const val TAG = "MapFragment"
    }

    private val model: SharedViewModel by activityViewModels()

    private lateinit var mainMap: GoogleMap

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.map_fragment, container, false)
//        model.text.observe(viewLifecycleOwner, Observer {
////            //use it here
////        })

        val button = root.find_button as Button

        button.setOnClickListener{
            val supportFragmentManager = requireActivity().supportFragmentManager
//            fm.beginTransaction().show(R.id.nav_host_fragment, FindFragment()).addToBackStack(null).commit()
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
            requireActivity().nav_view.menu.getItem(0).isChecked = true

        }


        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        map.onCreate(null)
        map.onResume()
        map.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        MapsInitializer.initialize(context)
        mainMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mainMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mainMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}