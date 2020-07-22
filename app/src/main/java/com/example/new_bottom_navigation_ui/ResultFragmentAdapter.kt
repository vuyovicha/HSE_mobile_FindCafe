package com.example.new_bottom_navigation_ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
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

            val fromPoint = CalculatingPoints(MainActivity.fromAddress.coordinates.latitude.toDouble(), MainActivity.fromAddress.coordinates.longitude.toDouble())
            val toPoint = CalculatingPoints(MainActivity.toAddress.coordinates.latitude.toDouble(), MainActivity.toAddress.coordinates.longitude.toDouble())
            val restaurant = CalculatingPoints(restaurant.latitude.toDouble(), restaurant.longitude.toDouble())
            var minutes : String = ""
            thread(start = true) {
                val fromRestaurant = getDist(fromPoint, restaurant)
                val restaurantTo = getDist(restaurant, toPoint)
                val fromTo = getDist(fromPoint, toPoint)
                if (fromRestaurant != null && restaurantTo != null && fromTo != null) {
                    minutes = ((fromRestaurant + restaurantTo - fromTo) / 60).toString()
                }
            }

            addedTime.text = "+$minutes minutes added"
        }

        fun getDist(point1 : CalculatingPoints, point2 : CalculatingPoints): Int? {
            val apiService = TomTomApiService.create()
            return apiService.route(point1.x, point1.y, point2.x, point2.y).execute().body()?.routes?.get(0)?.summary?.travelTimeInSeconds
        }

        //todo this function doesn't return proper time
        fun getAddedTime(point1 : CalculatingPoints, point2 : CalculatingPoints) : Int {
            val apiService = TomTomApiService.create()
            var seconds : Int = 0
            apiService.route(point1.x, point1.y, point2.x, point2.y)
                .enqueue(object : Callback<RouteHolder> {
                    override fun onResponse(
                        call: Call<RouteHolder>,
                        response: Response<RouteHolder>
                    ) {
                        if (response.body() != null) {
                            seconds =  response.body()!!.routes[0].summary.travelTimeInSeconds
                        }
                    }

                    override fun onFailure(call: Call<RouteHolder>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
            return seconds
        }
    }
}