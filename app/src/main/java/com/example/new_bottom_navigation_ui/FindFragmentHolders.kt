package com.example.new_bottom_navigation_ui

import android.view.View
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

class PreferenceListViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
    private val header = root.preferences_header

    fun onBindPreferenceList(row: FindFragmentAdapter.PreferenceList) {
        header.text = row.label

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

    }
}

class PreferenceRatingViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
    private val rating = root.reviewRating

    fun onBindPreferenceRating(row: FindFragmentAdapter.PreferenceRating) {
        rating.rating = row.rating

    }
}

class PreferenceOpenNowViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
    private val checkbox = root.open_now_checkbox

    fun onBindPreferenceOpenNow(row: FindFragmentAdapter.PreferenceOpenNow) {
        checkbox.isChecked = row.isChecked

    }
}