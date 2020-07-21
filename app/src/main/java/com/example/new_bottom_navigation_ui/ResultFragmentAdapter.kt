package com.example.new_bottom_navigation_ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.result_fragment_row.view.*

class ResultFragmentAdapter(private val man : FragmentManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var resultFragmentRows: List<Restaurant> = listOf<Restaurant>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ResultViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.result_fragment_row, parent, false))


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ResultViewHolder).onBindResult(resultFragmentRows[position] as Restaurant)
    }

    override fun getItemCount() = resultFragmentRows.count()

    fun replaceItems(items: List<Restaurant>) {
        this.resultFragmentRows = items
        notifyDataSetChanged()
    }

    inner class ResultViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
        private val image = root.cafeImageSmall
        private val name = root.cafeName
        private val location = root.cafeAddress
        private val addedTime = root.timeAddedToRoute

        fun onBindResult(restaurant : Restaurant) {
            Picasso.get().load(restaurant.smallPhotoUrl).into(image)
            name.text = restaurant.name
            location.text = restaurant.locationString
            val minutes = 15.toString() //todo calculate minutes here
            addedTime.text = "+$minutes minutes added"
        }
    }
}