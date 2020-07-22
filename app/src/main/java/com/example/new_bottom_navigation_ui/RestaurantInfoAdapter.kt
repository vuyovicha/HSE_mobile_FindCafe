package com.example.new_bottom_navigation_ui

import android.graphics.Path
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.IllegalArgumentException

class RestaurantInfoAdapter(val rows: List<RestaurantInfoRow>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var restaurantInfoRows: List<RestaurantInfoRow> = rows//listOf<RestaurantInfoRow>()

    companion object {
        private const val ACTIONS = 0
        private const val SIMPLE_INFO = 1
        private const val LIST = 2
        private const val OPEN = 3
        private const val LINE = 4
    }

    interface RestaurantInfoRow
    class Actions(val website_url: String, val reviews_url: String) : RestaurantInfoRow
    class SimpleInfo(val icon: Int, val text: String) : RestaurantInfoRow
    class HeaderAndList(val icon: Int, val label: String, val list: Array<String>) : RestaurantInfoRow
    class Open(val is_open: String, val open_time: String) : RestaurantInfoRow
    class Line() : RestaurantInfoRow


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ACTIONS -> ActionsHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.restaurant_info_actions, parent, false))
            LINE -> LineHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.restaurant_info_line, parent, false))
            SIMPLE_INFO -> SimpleInfoHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.restaurant_info_simple_info, parent, false))
            LIST -> ListHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.restaurant_info_list, parent, false))
            OPEN -> OpenInfoHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.restaurant_info_open, parent, false))
            else -> throw IllegalArgumentException("Not supported view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (restaurantInfoRows[position]) {
            is Actions -> ACTIONS
            is SimpleInfo -> SIMPLE_INFO
            is HeaderAndList -> LIST
            is Open -> OPEN
            is Line -> LINE
            else -> throw IllegalArgumentException("Not supported view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ACTIONS -> (holder as ActionsHolder).onBind(restaurantInfoRows[position] as Actions)
            SIMPLE_INFO -> (holder as SimpleInfoHolder).onBind(restaurantInfoRows[position] as SimpleInfo)
            LIST -> (holder as ListHolder).onBind(restaurantInfoRows[position] as HeaderAndList)
            OPEN -> (holder as OpenInfoHolder).onBind(restaurantInfoRows[position] as Open)
            LINE -> (holder as LineHolder).onBind(restaurantInfoRows[position] as Line)
            else -> throw IllegalArgumentException("Not supported view type")
        }
    }

    override fun getItemCount() = restaurantInfoRows.count()

    fun replaceItems(items: List<RestaurantInfoRow>) {
        this.restaurantInfoRows = items
        notifyDataSetChanged()
    }
}