package com.example.new_bottom_navigation_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.new_bottom_navigation_ui.ui.home.SharedViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.find_fragment.*
import kotlinx.android.synthetic.main.map_fragment.*
import kotlinx.android.synthetic.main.map_fragment.view.*
import kotlinx.android.synthetic.main.map_fragment_container.*


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
        val root = inflater.inflate(R.layout.map_fragment_container, container, false)
//        model.text.observe(viewLifecycleOwner, Observer {
////            //use it here
////        })

        val button = root.find_view

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


        val llBottomSheet = bottom_sheet
        val bottomSheetBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(llBottomSheet)

        val mloc = my_location

        //bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        //bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        //bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        //bottomSheetBehavior.peekHeight = 340

        //bottomSheetBehavior.isHideable = false

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}
            override fun onSlide(
                bottomSheet: View,
                slideOffset: Float
            ) {
                if (slideOffset in 0.0..0.5) mloc.animate().scaleX(1 - slideOffset*2).scaleY(1 - slideOffset*2).setDuration(0).start()
            }
        })

        val adapter = RestaurantInfoAdapter(
            listOf(
                RestaurantInfoAdapter.Actions("", ""),
                RestaurantInfoAdapter.Line(),
                RestaurantInfoAdapter.Open("Открыто", "Работает до 22:00"),
                RestaurantInfoAdapter.SimpleInfo(R.drawable.ic_phone_black_24dp, "+777777777"),
                RestaurantInfoAdapter.SimpleInfo(R.drawable.ic_phone_black_24dp, "Price level: $$$$"),
                RestaurantInfoAdapter.HeaderAndList(R.drawable.ic_category_black_24dp, "Establishment types", arrayOf("ресторан", "кафэ")),
                RestaurantInfoAdapter.HeaderAndList(R.drawable.ic_restaurant_menu_black_24dp, "Cuisine", arrayOf("супы", "коктели", "десерты")),
                RestaurantInfoAdapter.HeaderAndList(R.drawable.ic_restaurant_menu_black_24dp, "Dietary restrictions", arrayOf("вегетерианская пища"))
            )
        )
        rrv.adapter = adapter
        rrv.layoutManager = LinearLayoutManager(context)
        //find_fragment_recycler.addItemDecoration(SpaceItemDecoration())

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