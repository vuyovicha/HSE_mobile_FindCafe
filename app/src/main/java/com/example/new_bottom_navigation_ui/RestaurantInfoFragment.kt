package com.example.new_bottom_navigation_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.restaurant_info_fragment.*

class RestaurantInfoFragment : Fragment() {

    companion object {
        const val TAG = "RestaurantInfoFragment"

        private const val DESCRIPTION_ROW = 0
        private const val ADDRESS_ROW = 1
        private const val DIETARY_RESTRICTION_ROW = 2
        private const val PRICE_ROW = 3
        private const val CUISINE_ROW = 4
        private const val OPEN_ROW = 5
    }

    private val model: SharedViewModel by activityViewModels()
    private lateinit var adapter: RestaurantInfoFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.restaurant_info_fragment, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = RestaurantInfoFragmentAdapter(requireActivity().supportFragmentManager)

        var rows = ArrayList<RestaurantInfoFragmentAdapter.ResultFragmentRow>()
        rows.add(RestaurantInfoFragmentAdapter.Image(MainActivity.restaurantGetInfo.largePhotoUrl))
        var estTypeString = getStringListValues(MainActivity.restaurantGetInfo.establishmentType)
        rows.add(RestaurantInfoFragmentAdapter.Preview(
            MainActivity.restaurantGetInfo.name,
            estTypeString,
            MainActivity.restaurantGetInfo.rating,
            MainActivity.restaurantGetInfo.addedMinutes
        ))
        rows.add(RestaurantInfoFragmentAdapter.Actions(MainActivity.restaurantGetInfo.call, MainActivity.restaurantGetInfo.website))

        if (MainActivity.restaurantGetInfo.description.isNotEmpty()) rows.add(RestaurantInfoFragmentAdapter.IconRow(DESCRIPTION_ROW, MainActivity.restaurantGetInfo.description))
        rows.add(RestaurantInfoFragmentAdapter.IconRow(ADDRESS_ROW, MainActivity.restaurantGetInfo.address))
        var dietaryRestrictions = getStringListValues(MainActivity.restaurantGetInfo.dietaryRestrictions)
        if (dietaryRestrictions.isNotEmpty()) rows.add(RestaurantInfoFragmentAdapter.IconRow(DIETARY_RESTRICTION_ROW, dietaryRestrictions))
        if (MainActivity.restaurantGetInfo.priceLevel.isNotEmpty()) rows.add(RestaurantInfoFragmentAdapter.IconRow(PRICE_ROW, MainActivity.restaurantGetInfo.priceLevel))
        var cousin = getStringListValues(MainActivity.restaurantGetInfo.cousin)
        if (cousin.isNotEmpty()) rows.add(RestaurantInfoFragmentAdapter.IconRow(CUISINE_ROW, cousin))
        rows.add(RestaurantInfoFragmentAdapter.IconRow(OPEN_ROW, MainActivity.restaurantGetInfo.isClosed.toString()))

        adapter.replaceItems(rows)
        restaurant_info_recycler.adapter = adapter
        restaurant_info_recycler.layoutManager = LinearLayoutManager(context)

        back_button.setOnClickListener{

            val supportFragmentManager = requireActivity().supportFragmentManager
            val thisFragment: Fragment? = supportFragmentManager.findFragmentByTag(RestaurantInfoFragment.TAG)
            if (thisFragment != null) supportFragmentManager.beginTransaction().remove(thisFragment).commit()

            var fragmentTag = ResultFragment::class.java.simpleName
            if (requireActivity().nav_view.menu.getItem(1).isChecked) fragmentTag = MapFragment::class.java.simpleName

            supportFragmentManager.commit {
                supportFragmentManager.fragments.forEach { hide(it) }
                val fragment = supportFragmentManager.findFragmentByTag(fragmentTag)
                if (fragment != null) {
                    show(fragment)
                } else {
                    var nextFragment : Fragment = ResultFragment()
                    if (requireActivity().nav_view.menu.getItem(1).isChecked) nextFragment = MapFragment()
                    add(R.id.nav_host_fragment, nextFragment, nextFragment::class.java.simpleName)

                }
            }
        }

    }

    private fun getStringListValues(inputList : ArrayList<String>) : String {
        var list = ""
        for (i in inputList.indices) {
            if (list.isNotEmpty()) list += ", "
            list += inputList[i]
        }
        return list
    }



}