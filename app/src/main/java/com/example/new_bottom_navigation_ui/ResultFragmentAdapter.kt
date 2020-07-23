package com.example.new_bottom_navigation_ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.PolylineOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.result_fragment_row.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

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
            location.text = restaurant.address //todo or location string?
            var minutes = "minutes"
            if (restaurant.addedMinutes == "1") minutes = "minute"
            addedTime.text = "+${restaurant.addedMinutes} $minutes"

            root.setOnClickListener {
                MainActivity.restaurantGetInfo = restaurant
                val fragmentTag = RestaurantInfoFragment::class.java.simpleName

                man.commit {
                    man.fragments.forEach { hide(it) }
                    val fragment = man.findFragmentByTag(fragmentTag)
                    if (fragment != null) {
                        show(fragment)
                    } else {
                        val nextFragment = RestaurantInfoFragment()
                        add(R.id.nav_host_fragment, nextFragment, nextFragment::class.java.simpleName)

                    }
                }
            }
        }

    }
}