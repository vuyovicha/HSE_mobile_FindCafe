package com.example.new_bottom_navigation_ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.set_preferences_row.view.*
import java.util.*
import kotlin.collections.ArrayList

class SetPreferencesSearchFragmentAdapter (private var setPreferencesFragmentRows: List<RestaurantCriterionsIndexed>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    Filterable {

    private var setPreferencesFragmentFilterRows: List<RestaurantCriterionsIndexed> = listOf()

    init {
        setPreferencesFragmentFilterRows = setPreferencesFragmentRows
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SetSearchPreferencesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.set_preferences_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SetSearchPreferencesViewHolder).onBindSetSearchPreferencesViewHolder(setPreferencesFragmentFilterRows[position])
    }

    override fun getItemCount() = setPreferencesFragmentFilterRows.count()

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    setPreferencesFragmentFilterRows = setPreferencesFragmentRows
                } else {
                    val resultList = ArrayList<RestaurantCriterionsIndexed>()
                    for (row in setPreferencesFragmentRows) {
                        if (row.label.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            println("heyyyya")
                            resultList.add(row)
                        }
                    }
                    setPreferencesFragmentFilterRows = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = setPreferencesFragmentFilterRows
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                setPreferencesFragmentFilterRows = results?.values as ArrayList<RestaurantCriterionsIndexed>
                notifyDataSetChanged()
            }

        }
    }

    inner class SetSearchPreferencesViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
        private val checkBox: CheckBox = root.checkBox

        fun onBindSetSearchPreferencesViewHolder(option : RestaurantCriterionsIndexed) {
            checkBox.text = option.label
            checkBox.isChecked =  MainActivity.cousineStates[option.index]
            checkBox.setOnClickListener {
                MainActivity.cousineStates[option.index] = !MainActivity.cousineStates[option.index]
            }

        }
    }
}