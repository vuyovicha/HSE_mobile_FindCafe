package com.example.new_bottom_navigation_ui

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.find_fragment_header.view.*
import kotlinx.android.synthetic.main.find_fragment_open_now.view.*
import kotlinx.android.synthetic.main.find_fragment_preferences_view.view.*
import kotlinx.android.synthetic.main.find_fragment_prices.view.*
import kotlinx.android.synthetic.main.find_fragment_rating.view.*
import kotlinx.android.synthetic.main.find_fragment_route_view.view.*

class HeaderViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
    private val header = root.header_text

    fun onBindHeader(row: FindFragmentAdapter.Header) {
        header.text = row.label

    }
}

class RouteViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
    private val routePoint = root.route_point

    fun onBindRoute(row: FindFragmentAdapter.Route) {
        routePoint.text = row.label

    }
}

class PreferenceListViewHolder(private val root: View, private val manager : FragmentManager) : RecyclerView.ViewHolder(root) {
    private val header = root.preferences_header
    private val changeButton = root.change_preferences_button
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
    private val minPriceButton = root.min_price
    private val mediumPriceButton = root.medium_price
    private val maxPriceButton = root.max_price


    fun onBindPreferencePrice(row: FindFragmentAdapter.PreferencePrice) {
        when (row.checkedButtonIndex) {
            0 -> minPriceButton.isChecked = true
            1 -> mediumPriceButton.isChecked = true
            2 -> maxPriceButton.isChecked = true
        }
        MainActivity.pricesState = row.checkedButtonIndex
    }
}

class PreferenceRatingViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
    private val rating = root.reviewRating

    fun onBindPreferenceRating(row: FindFragmentAdapter.PreferenceRating) {
        rating.rating = row.rating
        MainActivity.ratingState = row.rating
    }
}

class PreferenceOpenNowViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
    private val checkbox = root.open_now_checkbox

    fun onBindPreferenceOpenNow(row: FindFragmentAdapter.PreferenceOpenNow) {
        checkbox.isChecked = row.isChecked
        MainActivity.openState = row.isChecked

    }
}