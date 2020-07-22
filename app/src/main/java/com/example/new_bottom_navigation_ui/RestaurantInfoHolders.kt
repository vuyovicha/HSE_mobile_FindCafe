package com.example.new_bottom_navigation_ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.restaurant_info_actions.view.r_reviews
import kotlinx.android.synthetic.main.restaurant_info_actions.view.r_route
import kotlinx.android.synthetic.main.restaurant_info_actions.view.r_website
import kotlinx.android.synthetic.main.restaurant_info_list.view.*
import kotlinx.android.synthetic.main.restaurant_info_open.view.*
import kotlinx.android.synthetic.main.restaurant_info_simple_info.view.*

class ActionsHolder(private val root: View) : RecyclerView.ViewHolder(root) {
    private val route = root.r_route
    private val website = root.r_website
    private val reviews = root.r_reviews

    fun onBind(row: RestaurantInfoAdapter.Actions) {
        //header.text = row.label

    }
}

class LineHolder(private val root: View) : RecyclerView.ViewHolder(root) {

    fun onBind(row: RestaurantInfoAdapter.Line) {

    }
}

class SimpleInfoHolder(private val root: View) : RecyclerView.ViewHolder(root) {
    private val icon = root.r_info_icon
    private val text = root.r_text

    fun onBind(row: RestaurantInfoAdapter.SimpleInfo) {
        icon.setImageResource(row.icon)
        text.text = row.text
    }
}

class OpenInfoHolder(private val root: View) : RecyclerView.ViewHolder(root) {
    private val is_open = root.r_is_open
    private val open_time = root.r_is_open_time

    fun onBind(row: RestaurantInfoAdapter.Open) {
        is_open.text = row.is_open
        open_time.text = row.open_time
    }
}

class ListHolder(private val root: View) : RecyclerView.ViewHolder(root) {
    private val icon = root.r_list_icon
    private val header = root.r_list_header
    private val list = root.r_list

    fun onBind(row: RestaurantInfoAdapter.HeaderAndList) {
        icon.setImageResource(row.icon)
        header.text = row.label
        list.text = if (row.list.isNotEmpty()) "- " + row.list.reduce { res, str -> "${res}\n- ${str}" }.trimEnd('\n') else "- "
    }
}
