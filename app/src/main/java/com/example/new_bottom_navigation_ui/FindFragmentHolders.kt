package com.example.new_bottom_navigation_ui

import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.find_fragment_header.view.*
import kotlinx.android.synthetic.main.find_fragment_open_now.view.*
import kotlinx.android.synthetic.main.find_fragment_preferences_view.view.*
import kotlinx.android.synthetic.main.find_fragment_preferences_view.view.change_preferences_button
import kotlinx.android.synthetic.main.find_fragment_preferences_view.view.chosen_preferences
import kotlinx.android.synthetic.main.find_fragment_preferences_view.view.preferences_header
import kotlinx.android.synthetic.main.find_fragment_preferences_view_2.view.*
import kotlinx.android.synthetic.main.find_fragment_prices.view.*
import kotlinx.android.synthetic.main.find_fragment_rating.view.*
import kotlinx.android.synthetic.main.find_fragment_route_view.view.*

class HeaderViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
    private val header = root.header_text

    fun onBindHeader(row: FindFragmentAdapter.Header) {
        header.text = row.label

    }
}

class RouteViewHolder(private val root: View, private val manager : FragmentManager) : RecyclerView.ViewHolder(root) {
    private val button = root.set_point_button

    fun onBindRoute(row: FindFragmentAdapter.Route, position : Int) {
        button.text = row.label

        button.setOnClickListener{
            val supportFragmentManager = manager
            val fragmentTag = SetRouteFragment::class.java.simpleName
            MainActivity.routePosition = position

            supportFragmentManager.commit {
                supportFragmentManager.fragments.forEach { hide(it) }
                val fragment = supportFragmentManager.findFragmentByTag(fragmentTag)
                if (fragment != null) {
                    show(fragment)
                } else {
                    val nextFragment = SetRouteFragment()
                    add(R.id.nav_host_fragment, nextFragment, nextFragment::class.java.simpleName)
                }

            }

            val thisFragment: Fragment? = manager.findFragmentByTag(FindFragment.TAG)
            if (thisFragment != null) manager.beginTransaction().remove(thisFragment).commit()
        }

    }
}

class PreferenceListViewHolder(private val root: View, private val manager : FragmentManager) : RecyclerView.ViewHolder(root) {
    private val header = root.preferences_header
    private val changeButton = root.settings_button
    private val chosenPreferences = root.chosen_preferences

    companion object {
        private const val ESTABLISHMENT_TYPE = "Establishment type"
        private const val COUSIN = "Cousin"
        private const val DIETARY_RESTRICTIONS = "Dietary restrictions"
    }

    private fun getChosenPreferences(options : ArrayList<RestaurantCriterionsIndexed>, states : BooleanArray) : String {
        var chosenPreferencesString = ""
        for (i in options.indices) {
            if (states[i]) {
                if (chosenPreferencesString.isNotEmpty()) {
                    chosenPreferencesString = chosenPreferencesString + ", " + options[i].label
                } else {
                    chosenPreferencesString = options[i].label
                }
            }
        }

        if (chosenPreferencesString.isNotEmpty()) {
            chosenPreferencesString.removeRange(chosenPreferencesString.length - 2, chosenPreferencesString.length - 1)
        } else {
            chosenPreferencesString = "None"
        }

        return chosenPreferencesString
    }

    fun onBindPreferenceList(row: FindFragmentAdapter.PreferenceList) {
        header.text = row.label
        var options : ArrayList<RestaurantCriterionsIndexed> = ArrayList()
        var states : BooleanArray = BooleanArray(0)
        when (header.text) {
            ESTABLISHMENT_TYPE ->  {
                options = MainActivity.establishmentTypeOptions
                states = MainActivity.establishmentTypeStates
            }
            COUSIN -> {
                options = MainActivity.cousineOptions
                states = MainActivity.cousineStates
            }
            DIETARY_RESTRICTIONS -> {
                options = MainActivity.dietaryRestrictionsOptions
                states = MainActivity.dietaryRestrictionsStates
            }
        }
        chosenPreferences.text = getChosenPreferences(options, states)

        changeButton.setOnClickListener{
            val supportFragmentManager = manager
            val fragmentTag = when (header.text) {
                ESTABLISHMENT_TYPE ->  SetPreferencesEstablishmentTypeFragment::class.java.simpleName
                COUSIN -> SetPreferencesCousinFragment::class.java.simpleName
                DIETARY_RESTRICTIONS -> SetPreferencesDietaryRestrictionsFragment::class.java.simpleName
                else -> "Not supported fragment tag"
            }

            MainActivity.setPreferencesFragmentTag = when(header.text) {
                ESTABLISHMENT_TYPE ->  ESTABLISHMENT_TYPE
                COUSIN -> COUSIN
                DIETARY_RESTRICTIONS -> DIETARY_RESTRICTIONS
                else -> "Not supported fragment tag"
            }

            supportFragmentManager.commit {
                supportFragmentManager.fragments.forEach { hide(it) }
                val fragment = supportFragmentManager.findFragmentByTag(fragmentTag)
                if (fragment != null) {
                    show(fragment)
                } else {
                    val nextFragment = when (header.text) {
                        ESTABLISHMENT_TYPE ->  SetPreferencesEstablishmentTypeFragment()
                        COUSIN -> SetPreferencesCousinFragment()
                        DIETARY_RESTRICTIONS -> SetPreferencesDietaryRestrictionsFragment()
                        else -> SetPreferencesCousinFragment() //setting random fragment for else
                    }
                    add(R.id.nav_host_fragment, nextFragment, nextFragment::class.java.simpleName)
                }

            }
        }

    }
}

class PreferencePriceViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
    private val anyPriceButton = root.any_price
    private val minPriceButton = root.min_price
    private val mediumPriceButton = root.medium_price
    private val maxPriceButton = root.max_price

    fun onBindPreferencePrice(row: FindFragmentAdapter.PreferencePrice) { //do not need a row here
        when (MainActivity.pricesState) {
            0 -> anyPriceButton.isChecked = true
            1 -> minPriceButton.isChecked = true
            2 -> mediumPriceButton.isChecked = true
            3 -> maxPriceButton.isChecked = true
        }

        anyPriceButton.setOnClickListener {
            MainActivity.pricesState = 0
        }

        minPriceButton.setOnClickListener {
            MainActivity.pricesState = 1
        }

        mediumPriceButton.setOnClickListener {
            MainActivity.pricesState = 2
        }

        maxPriceButton.setOnClickListener {
            MainActivity.pricesState = 3
        }
    }
}

class PreferenceRatingViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
    private val anyRating = root.any_rating
    private val threeRating = root.three_rating
    private val fourRating = root.four_rating
    private val fiveRating = root.five_rating


    fun onBindPreferenceRating(row: FindFragmentAdapter.PreferenceRating) { //todo do not need a row here
        when (MainActivity.pricesState) {
            0 -> anyRating.isChecked = true
            1 -> threeRating.isChecked = true
            2 -> fourRating.isChecked = true
            3 -> fiveRating.isChecked = true
        }

        anyRating.setOnClickListener {
            MainActivity.pricesState = 0
        }

        threeRating.setOnClickListener {
            MainActivity.pricesState = 1
        }

        fourRating.setOnClickListener {
            MainActivity.pricesState = 2
        }

        fiveRating.setOnClickListener {
            MainActivity.pricesState = 3
        }

    }

}

class PreferenceOpenNowViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
    private val checkbox = root.open_now_checkbox

    fun onBindPreferenceOpenNow(row: FindFragmentAdapter.PreferenceOpenNow) { //todo we do not need a "row" here
        checkbox.isChecked = MainActivity.openState

        checkbox.setOnClickListener{
            MainActivity.openState = checkbox.isChecked
        }

    }
}