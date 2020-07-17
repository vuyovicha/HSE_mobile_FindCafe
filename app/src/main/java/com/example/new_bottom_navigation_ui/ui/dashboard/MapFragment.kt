package com.example.new_bottom_navigation_ui.ui.dashboard

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.new_bottom_navigation_ui.MainActivity
import com.example.new_bottom_navigation_ui.R
import com.example.new_bottom_navigation_ui.ui.home.SharedViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.map_fragment.*

class DashboardFragment : Fragment(), OnMapReadyCallback {

    private val model: SharedViewModel by activityViewModels()

    private lateinit var mainMap: GoogleMap

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.map_fragment, container, false)
        model.text.observe(viewLifecycleOwner, Observer {
            //use it here
        })


        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        find_button.setOnClickListener{

        }

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