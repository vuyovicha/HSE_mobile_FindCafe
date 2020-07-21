package com.example.new_bottom_navigation_ui

import android.annotation.SuppressLint
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.maps.model.LatLng
import com.hse.findcafe.PlaceHolder
import com.hse.findcafe.ui.home.RouteHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.map_fragment.*
import kotlinx.android.synthetic.main.map_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapFragment : Fragment(), OnMapReadyCallback {

    companion object {
        const val TAG = "MapFragment"
    }

    private val model: SharedViewModel by activityViewModels()
    private val places: MutableList<com.google.maps.model.LatLng> =
        ArrayList()
    private var mapsApiKey: String? = null
    private var width = 0

    private lateinit var map: GoogleMap

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

        root.my_location.setOnClickListener {
            if (map != null) {
                val bounds = LatLngBounds.builder()
                for (i in places.indices) {
                    bounds.include(
                        com.google.android.gms.maps.model.LatLng(
                            places[i].lat,
                            places[i].lng
                        )
                    )
                }
                map.moveCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        bounds.build(),
                        width,
                        width,
                        25
                    )
                )
            }
        }


        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        places.add(LatLng(55.754724, 37.621380))
        places.add(LatLng(55.760133, 37.618697))
        places.add(LatLng(55.764753, 37.591313))
        places.add(LatLng(55.728466, 37.604155))
        mapsApiKey = "AIzaSyDAIoGVJga6e4IzDwV-ICbZcVwkrRV57OQ"
        width = resources.displayMetrics.widthPixels

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        MapsInitializer.initialize(context)
        map = googleMap
        googleMap.setOnMarkerClickListener(
            fun(marker: Marker?): Boolean {
                Toast.makeText(context, marker?.title ?: "Null", Toast.LENGTH_LONG).show()
                return false
            }
        )
        // При запуске карты ставим метки и прокладываем через них маршрут
        // TODO: передавать массив меток через intent
        // GPS HERE
        val location = MainActivity.mgr?.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
        if (location != null) {
            googleMap.addMarker(MarkerOptions()
                .position(
                    com.google.android.gms.maps.model.LatLng(location.latitude, location.longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                .title = "I am here"

            //places.add(LatLng(location.latitude, location.longitude))
            //Toast.makeText(this, location.longitude.toString(), Toast.LENGTH_LONG).show()
        }

        val markers = arrayOfNulls<MarkerOptions>(places.size)
        val bounds = LatLngBounds.builder()
        for (i in places.indices) {
            markers[i] = MarkerOptions()
                .position(
                    com.google.android.gms.maps.model.LatLng(
                        places[i].lat,
                        places[i].lng
                    )
                )
            googleMap.addMarker(markers[i])
            bounds.include(com.google.android.gms.maps.model.LatLng(places[i].lat, places[i].lng))
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), width, width, 25))
        val apiService = TomTomApiService.create()

        // Autocomplete
        /*apiService.find("Москва макдоналдс").enqueue(object : Callback<PlaceHolder> {
            override fun onResponse(
                call: Call<PlaceHolder>,
                response: Response<PlaceHolder>
            ) {
                if (response.body()?.results?.get(0) != null) {
                    Toast.makeText(context, response.body()!!.results[0].address.streetName + " " + response.body()!!.results[0].address.localName, Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<PlaceHolder>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })*/

        for (i in 0 until places.size - 1) {
            apiService.route(places[i].lat, places[i].lng, places[i + 1].lat, places[i + 1].lng)
                .enqueue(object : Callback<RouteHolder> {

                    override fun onResponse(
                        call: Call<RouteHolder>,
                        response: Response<RouteHolder>
                    ) {
                        val line = PolylineOptions()
                        line.width(16f).color(R.color.colorPrimary)
                        googleMap.addPolyline(line)
                        if (response.body() != null) {
                            for (i in response.body()!!.routes[0].legs[0].points) {
                                val pt = com.google.android.gms.maps.model.LatLng(
                                    i.latitude,
                                    i.longitude
                                )
                                line.add(pt)

                            }
                        }
                        map.addPolyline(line)
                    }

                    override fun onFailure(call: Call<RouteHolder>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }
}