package com.example.new_bottom_navigation_ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.maps.model.LatLng
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
    private var mapsApiKey: String? = null
    private var width = 0
    private var places: MutableList<com.google.maps.model.LatLng> = ArrayList()

    private lateinit var map: GoogleMap


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.map_fragment, container, false)
        val button = root.find_button as Button

        if (MainActivity.placesToShow.isNotEmpty()) {
            if (MainActivity.getRoute) {
                places.add(LatLng(MainActivity.restaurantGetInfo.latitude.toDouble(), MainActivity.restaurantGetInfo.longitude.toDouble()))
            } else {
                places = MainActivity.placesToShow
            }
            places.add(0, LatLng(MainActivity.fromAddress.coordinates.latitude.toDouble(), MainActivity.fromAddress.coordinates.longitude.toDouble()))
            places.add(LatLng(MainActivity.toAddress.coordinates.latitude.toDouble(), MainActivity.toAddress.coordinates.longitude.toDouble()))
        }


        button.setOnClickListener{
            val supportFragmentManager = requireActivity().supportFragmentManager
            val fragmentTag = FindFragment::class.java.simpleName

            val thisFragment: Fragment? = supportFragmentManager.findFragmentByTag(fragmentTag)
            if (thisFragment != null) supportFragmentManager.beginTransaction().remove(thisFragment).commit()

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

        root.my_zoom_in.setOnClickListener{
            if (map != null) map.moveCamera(CameraUpdateFactory.zoomIn())
        }

        root.my_zoom_out.setOnClickListener{
            if (map != null) map.moveCamera(CameraUpdateFactory.zoomOut())
        }

        root.my_location.setOnClickListener {
            if (map != null) {
                //map.moveCamera(CameraUpdateFactory.zoomIn())
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
        width = resources.displayMetrics.widthPixels
        requireActivity().nav_view.menu.getItem(1).isChecked = true

        map_view.onCreate(savedInstanceState)
        map_view.getMapAsync(this)
        map_view.onResume()
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        MapsInitializer.initialize(context)
        map = googleMap

        googleMap.setOnMarkerClickListener(
            fun(marker: Marker?): Boolean {
                return false
            }

        )

        // При запуске карты ставим метки и прокладываем через них маршрут
        // GPS HERE
//        val location = MainActivity.mgr?.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
//        if (location != null) {
//            googleMap.addMarker(MarkerOptions()
//                .position(
//                    com.google.android.gms.maps.model.LatLng(location.latitude, location.longitude))
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
//                .title = "I am here"
//        }

        val markers = arrayOfNulls<MarkerOptions>(places.size)
        val bounds = LatLngBounds.builder()
        for (i in places.indices) {
            var locationMarkerIcon = getBitmapFromVector(requireContext(), R.drawable.ic_baseline_restaurant_menu_24,
                ContextCompat.getColor(requireContext(), R.color.colorRedMarker))
            if (i == 0) {
                locationMarkerIcon = getBitmapFromVector(requireContext(), R.drawable.ic_baseline_my_location_24,
                    ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark))
            } else if (i == places.count() - 1) {
                locationMarkerIcon = getBitmapFromVector(requireContext(), R.drawable.ic_baseline_location_on_24,
                    ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark))
            }
            markers[i] = MarkerOptions()
                .position(
                    com.google.android.gms.maps.model.LatLng(
                        places[i].lat,
                        places[i].lng
                    )
                )
                .icon(locationMarkerIcon)
                .anchor(0.5f, 1f)
            googleMap.addMarker(markers[i])
            bounds.include(com.google.android.gms.maps.model.LatLng(places[i].lat, places[i].lng))
        }
        if (places.isNotEmpty()) googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), width, width, 25))


        //getting route here
        if (MainActivity.getRoute) {
            val apiService = TomTomApiService.create()
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

    //from drawable to bitmap (setting marker icon)
    private fun getBitmapFromVector(
        context: Context,
        @DrawableRes vectorResourceId: Int,
        @ColorInt tintColor: Int
    ): BitmapDescriptor? {
        val vectorDrawable = ResourcesCompat.getDrawable(
            context.resources, vectorResourceId, null
        )
        if (vectorDrawable == null) {
            Log.e(TAG, "Requested vector resource was not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, tintColor)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}