package com.hse.findcafe.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.*
//import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.LatLng
import com.google.maps.model.TravelMode
import com.hse.findcafe.FindDialog
import com.hse.findcafe.R
import kotlinx.android.synthetic.main.fragment_home.*


fun getAutoPredictions(req : String, ctx : Context): String? {
    // Get restourants here
    if (!Places.isInitialized()) {
        //val gApiKey: String = applicationContext.getString(R.string.api_key)
        val gApiKey = "API HERE"
        Places.initialize(ctx, gApiKey)
    }
    val placesClient = Places.createClient(ctx)

    val token = AutocompleteSessionToken.newInstance()
    val request =
        FindAutocompletePredictionsRequest.builder()
            .setTypeFilter(TypeFilter.REGIONS)
            .setSessionToken(token)
            .setQuery("Самара")
            .build()

    var ans: String? = null
    placesClient.findAutocompletePredictions(request)
        .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
            val sb = StringBuilder()
            for (prediction in response.autocompletePredictions) {
                sb.append(prediction.getPrimaryText(null).toString())
                sb.append("\n")
            }
            //Toast.makeText(applicationContext, sb.toString(), Toast.LENGTH_LONG).show()
            ans = sb.toString()
        }
        .addOnFailureListener { exception: Exception ->
            exception.printStackTrace(); Toast.makeText(
            ctx,
            exception.message,
            Toast.LENGTH_LONG
        ).show()
            exception.message?.let { Log.e("mytag", it) }
            for (i in exception.stackTrace) {
                Log.e("mytag", i.toString())
            }
        }
    return ans
}


class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var homeViewModel: HomeViewModel
private val places: MutableList<com.google.maps.model.LatLng> =
        ArrayList()
private var mapsApiKey: String? = null
    private var width = 0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        //val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it

        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       places.add(LatLng(55.754724, 37.621380))
        places.add(LatLng(55.760133, 37.618697))
        places.add(LatLng(55.764753, 37.591313))
        places.add(LatLng(55.728466, 37.604155))
        mapsApiKey = "9a35fdaa8bmsh64d201965518884p14fa46jsne83376c9e265"
        width = resources.displayMetrics.widthPixels

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        find_view.setOnClickListener {
            FindDialog.newInstance().show(requireActivity().supportFragmentManager, FindDialog.TAG)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        MapsInitializer.initialize(context)
         // При запуске карты ставим метки и прокладываем через них маршрут
      // TODO: передавать массив меток через intent
        val markers = arrayOfNulls<MarkerOptions>(places.size)
        for (i in places.indices) {
            markers[i] = MarkerOptions()
                .position(
                    com.google.android.gms.maps.model.LatLng(
                        places[i].lat,
                        places[i].lng
                    )
                )
            googleMap.addMarker(markers[i])
        }
        val geoApiContext = GeoApiContext.Builder()
            .apiKey(mapsApiKey)
            .build()
        var result: DirectionsResult? = null
        try {
            result = DirectionsApi.newRequest(geoApiContext)
                .mode(TravelMode.WALKING)
                .origin(places[0])
                .destination(places[places.size - 1])
                .waypoints(places[1], places[2]).await()
        } catch (e: Exception) {
            e.printStackTrace()
            //Toast.makeText(applicationContext, "Error!", Toast.LENGTH_LONG).show()
            return
        }
        val path =
            result!!.routes[0].overviewPolyline.decodePath()
        val line = PolylineOptions()
        val latLngBuilder = LatLngBounds.Builder()
        for (i in path.indices) {
            line.add(com.google.android.gms.maps.model.LatLng(path[i].lat, path[i].lng))
            latLngBuilder.include(
                com.google.android.gms.maps.model.LatLng(
                    path[i].lat,
                    path[i].lng
                )
            )
        }
        line.width(16f).color(R.color.colorPrimary)
        googleMap.addPolyline(line)
        val latLngBounds = latLngBuilder.build()
        val track =
            CameraUpdateFactory.newLatLngBounds(latLngBounds, width, width, 25)
        googleMap.moveCamera(track)
    }
}