package com.example.new_bottom_navigation_ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.set_preferences_row.view.*

class SetPreferencesFragmentAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val ESTABLISHMENT_TYPE = "Establishment type"
        private const val DIETARY_RESTRICTIONS = "Dietary restrictions"
    }

    private var setPreferencesFragmentRows: List<RestaurantCriterionsIndexed> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SetPreferencesViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.set_preferences_row, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SetPreferencesViewHolder).onBindSetPreferencesViewHolder(setPreferencesFragmentRows[position].label, position)
    }

    override fun getItemCount() = setPreferencesFragmentRows.count()

    fun replaceItems(items: List<RestaurantCriterionsIndexed>) {
        this.setPreferencesFragmentRows = items
        notifyDataSetChanged()
    }

    inner class SetPreferencesViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
        private val checkBox: CheckBox = root.checkBox

        fun onBindSetPreferencesViewHolder(label: String, index: Int) {
            checkBox.text = label
            checkBox.isChecked = when(MainActivity.setPreferencesFragmentTag) {
                ESTABLISHMENT_TYPE ->   MainActivity.establishmentTypeStates[index]
                DIETARY_RESTRICTIONS -> MainActivity.dietaryRestrictionsStates[index]
                else -> false
            }
            checkBox.setOnClickListener {
                when(MainActivity.setPreferencesFragmentTag) {
                    ESTABLISHMENT_TYPE -> MainActivity.establishmentTypeStates[index] = !MainActivity.establishmentTypeStates[index]
                    DIETARY_RESTRICTIONS -> MainActivity.dietaryRestrictionsStates[index] = !MainActivity.dietaryRestrictionsStates[index]
                }
            }

        }
    }
}