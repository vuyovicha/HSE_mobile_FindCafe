package com.example.new_bottom_navigation_ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.IllegalArgumentException

class FindFragmentAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var findFragmentRows: List<FindFragmentRow> = listOf<FindFragmentRow>()

    companion object {
        private const val HEADER = 0
        private const val ROUTE = 1
        private const val PREFERENCE_LIST = 2
        private const val PREFERENCE_PRICE = 3
        private const val PREFERENCE_RATING = 4
        private const val PREFERENCE_OPEN_NOW = 5
    }

    interface FindFragmentRow
    class Header(val label: String) : FindFragmentRow
    class Route(val label: String) : FindFragmentRow
    class PreferenceList(val label: String) : FindFragmentRow
    class PreferencePrice(val checkedButtonIndex: Int) : FindFragmentRow
    class PreferenceRating(val rating: Float) : FindFragmentRow
    class PreferenceOpenNow(val isChecked: Boolean) : FindFragmentRow


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER -> HeaderViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.find_fragment_header, parent, false))
            ROUTE -> RouteViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.find_fragment_route_view, parent, false))
            PREFERENCE_LIST -> PreferenceListViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.find_fragment_preferences_view, parent, false))
            PREFERENCE_PRICE -> PreferencePriceViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.find_fragment_prices, parent, false))
            PREFERENCE_RATING -> PreferenceRatingViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.find_fragment_rating, parent, false))
            PREFERENCE_OPEN_NOW -> PreferenceOpenNowViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.find_fragment_open_now, parent, false))
            else -> throw IllegalArgumentException("Not supported view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (findFragmentRows[position]) {
            is Header -> HEADER
            is Route -> ROUTE
            is PreferenceList -> PREFERENCE_LIST
            is PreferencePrice -> PREFERENCE_PRICE
            is PreferenceRating -> PREFERENCE_RATING
            is PreferenceOpenNow -> PREFERENCE_OPEN_NOW
            else -> throw IllegalArgumentException("Not supported view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            HEADER -> (holder as HeaderViewHolder).onBindHeader(findFragmentRows[position] as Header)
            ROUTE -> (holder as RouteViewHolder).onBindRoute(findFragmentRows[position] as Route)
            PREFERENCE_LIST -> (holder as PreferenceListViewHolder).onBindPreferenceList(findFragmentRows[position] as PreferenceList)
            PREFERENCE_PRICE -> (holder as PreferencePriceViewHolder).onBindPreferencePrice(findFragmentRows[position] as PreferencePrice)
            PREFERENCE_RATING -> (holder as PreferenceRatingViewHolder).onBindPreferenceRating(findFragmentRows[position] as PreferenceRating)
            PREFERENCE_OPEN_NOW -> (holder as PreferenceOpenNowViewHolder).onBindPreferenceOpenNow(findFragmentRows[position] as PreferenceOpenNow)
            else -> throw IllegalArgumentException("Not supported view type")
        }
    }

    override fun getItemCount() = findFragmentRows.count()

    fun replaceItems(items: List<FindFragmentRow>) {
        this.findFragmentRows = items
        notifyDataSetChanged()
    }
}