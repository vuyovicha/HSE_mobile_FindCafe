package com.example.new_bottom_navigation_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.find_fragment.*
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import kotlin.concurrent.thread

data class Restaurant(
    val name : String = "",
    val latitude : String = "",
    val longitude : String = "",
    val locationString : String = "",
    val smallPhotoUrl : String = "",
    val largePhotoUrl : String = "",
    val rating : String = "",
    val priceLevel : String = "",
    val description : String = "",
    val address : String = "",
    val cousin : ArrayList<String> = ArrayList(),
    val dietaryRestrictions : ArrayList<String> = ArrayList(),
    val establishmentType : ArrayList<String> = ArrayList(),
    val addedMinutes : String = "",
    val website : String = "",
    val call : String = "",
    val isClosed : Boolean = false
)

data class PointString(var latitude: String = "", var longitude: String = "")

class FindFragment : Fragment() {

    private val client = OkHttpClient()

    companion object {
        const val TAG = "FindFragment"
    }

    private val model: SharedViewModel by activityViewModels()
    private lateinit var adapter: FindFragmentAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.find_fragment, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val findFragmentRows = mutableListOf<FindFragmentAdapter.FindFragmentRow>()
        //findFragmentRows.add(FindFragmentAdapter.Header("Route"))
        findFragmentRows.add(FindFragmentAdapter.Route(MainActivity.fromAddress.address))
        findFragmentRows.add(FindFragmentAdapter.Route(MainActivity.toAddress.address))
        //findFragmentRows.add(FindFragmentAdapter.Header("Cafe preferences"))
        findFragmentRows.add(FindFragmentAdapter.PreferenceList("Establishment type"))
        findFragmentRows.add(FindFragmentAdapter.PreferenceList("Cousin"))
        findFragmentRows.add(FindFragmentAdapter.PreferenceList("Dietary restrictions"))
        findFragmentRows.add(FindFragmentAdapter.PreferencePrice(MainActivity.pricesState))
        findFragmentRows.add(FindFragmentAdapter.PreferenceRating(MainActivity.ratingState))
        findFragmentRows.add(FindFragmentAdapter.PreferenceOpenNow(MainActivity.openState))

        adapter = FindFragmentAdapter(requireActivity().supportFragmentManager)
        adapter.replaceItems(findFragmentRows)
        find_fragment_recycler.adapter = adapter
        find_fragment_recycler.layoutManager = LinearLayoutManager(context)

        model.data.observe(viewLifecycleOwner, Observer {
            adapter.notifyDataSetChanged()
        })

        submit_preferences_button.setOnClickListener {
            val fragment: Fragment? =
                requireActivity().supportFragmentManager.findFragmentByTag(ResultFragment.TAG)
            if (fragment != null) requireActivity().supportFragmentManager.beginTransaction().remove(fragment)
                .commit()
            sendRequest()
        }

    }

    private fun getStringListValues(states : BooleanArray, options : ArrayList<RestaurantCriterionsIndexed>) : String {
        var list = ""
        for (i in options.indices) {
            if (states[i]) {
                if (list.isNotEmpty()) list += ","
                list += options[i].value
            }
        }
        return list
    }

    private fun getTime(point1 : CalculatingPoints, point2 : CalculatingPoints): Int? {
        val apiService = TomTomApiService.create()
        return apiService.route(point1.x, point1.y, point2.x, point2.y).execute().body()?.routes?.get(0)?.summary?.travelTimeInSeconds
    }

    private fun sendRequest() {
        val prices : String
        if (MainActivity.pricesState > 0) prices = "&prices_restaurants=" + MainActivity.pricesOptions[MainActivity.pricesState - 1].value
        else prices = ""

        val openNow = "&open_now=" + MainActivity.openState.toString()

        val rating : String
        if (MainActivity.ratingState > 0) rating = "&min_rating=" + MainActivity.ratingState.toString()
        else rating = ""

        val getCousinString = getStringListValues(MainActivity.cousineStates, MainActivity.cousineOptions)
        val cousinType : String
        if (getCousinString.isNotEmpty()) cousinType = "&combined_food=$getCousinString"
        else cousinType = ""

        val getDietaryRestrictionsString = getStringListValues(MainActivity.dietaryRestrictionsStates, MainActivity.dietaryRestrictionsOptions)
        val dietaryRestrictions : String
        if (getDietaryRestrictionsString.isNotEmpty()) dietaryRestrictions = "&dietary_restrictions=$getDietaryRestrictionsString"
        else dietaryRestrictions = ""

        val getEstablishmentTypeString = getStringListValues(MainActivity.dietaryRestrictionsStates, MainActivity.dietaryRestrictionsOptions)
        val establishmentType : String
        if (getEstablishmentTypeString.isNotEmpty()) establishmentType = "&restaurant_tagcategory=$getEstablishmentTypeString"
        else establishmentType = ""

        val distance = "&distance=10"  //todo change accordingly

            //todo uncomment this stuff
        val centerPoint : PointString = CalculatingPoints.getSegmentCenter(MainActivity.fromAddress.coordinates, MainActivity.toAddress.coordinates)
        val latitude = "&latitude=" + centerPoint.latitude
        val longitude = "&longitude=" + centerPoint.longitude

        val url = "https://tripadvisor1.p.rapidapi.com/restaurants/list-by-latlng?limit=30&currency=EUR$prices$cousinType$distance$establishmentType$openNow$dietaryRestrictions&lunit=km&lang=en_US$rating$latitude$longitude"

        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("x-rapidapi-host", "tripadvisor1.p.rapidapi.com")
            .addHeader("x-rapidapi-key", "9a35fdaa8bmsh64d201965518884p14fa46jsne83376c9e265")
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                //change state to error here
            }
            override fun onResponse(call: okhttp3.Call, response: Response) {
                val requestedString = response.body()?.string().toString()
                val jsonObject = JSONObject(requestedString)
                jsonObject.remove("open_hours_options")
                jsonObject.remove("paging")
                MainActivity.foundRestaurants = ArrayList()
                val items = jsonObject.getJSONArray("data")

                val fromPoint = CalculatingPoints(MainActivity.fromAddress.coordinates.latitude.toDouble(), MainActivity.fromAddress.coordinates.longitude.toDouble())
                val toPoint = CalculatingPoints(MainActivity.toAddress.coordinates.latitude.toDouble(), MainActivity.toAddress.coordinates.longitude.toDouble())

                thread(start = true) {
                    for (i in 1 until items.length()) {
                        val item = JSONObject(items[i].toString())
                        if (item.length() > 42) {
                            //no photo available urls
                            var smallPhoto = "https://lh3.googleusercontent.com/proxy/UCeMI56_IXsHAnNTahVtGH5AjI6y4kg6QA3LSXPvL8-3Qsj5-h_JrnPChuLReQSuQKRBA9c77ZlCrtcffgjSuY2il4_4KQ92Tpb9sG133prlqeg"
                            var largePhoto = "https://lh3.googleusercontent.com/proxy/UCeMI56_IXsHAnNTahVtGH5AjI6y4kg6QA3LSXPvL8-3Qsj5-h_JrnPChuLReQSuQKRBA9c77ZlCrtcffgjSuY2il4_4KQ92Tpb9sG133prlqeg"

                            if (item.has("photo")) {
                                val photoObject = JSONObject(JSONObject(item.getString("photo")).getString("images"))
                                smallPhoto = JSONObject(photoObject.getString("small")).getString("url")
                                largePhoto = JSONObject(photoObject.getString("large")).getString("url")
                            }

                            var rating = "-"
                            if (item.has("rating")) {
                                rating = item.getString("rating")
                            }

                            val cousinesList = getPreferenceList("cuisine", item)
                            val dietaryRestrictionsList = getPreferenceList("dietary_restrictions", item)
                            val establishmentTypeList = getPreferenceList("establishment_types", item)

                            val restaurant = CalculatingPoints(item.getString("latitude").toDouble(), item.getString("longitude").toDouble())

                            var minutes : String = "0"
                            val fromRestaurant = getTime(fromPoint, restaurant)
                            val restaurantTo = getTime(restaurant, toPoint)
                            val fromTo = getTime(fromPoint, toPoint)
                            if (fromRestaurant != null && restaurantTo != null && fromTo != null) {
                                minutes = ((fromRestaurant + restaurantTo - fromTo) / 60).toString()
                            }

                            var website = ""
                            var call = ""
                            if (item.has("web_url")) {
                                website = item.getString("web_url")
                            }
                            if (item.has("phone")) {
                                call = item.getString("phone")
                            }

                            MainActivity.foundRestaurants.add(
                                Restaurant(
                                    item.getString("name"),
                                    restaurant.x.toString(),
                                    restaurant.y.toString(),
                                    item.getString("location_string"),
                                    smallPhoto,
                                    largePhoto,
                                    rating,
                                    item.getString("price_level"),
                                    item.getString("description"),
                                    item.getString("address"),
                                    cousinesList,
                                    dietaryRestrictionsList,
                                    establishmentTypeList,
                                    minutes,
                                    website,
                                    call,
                                    item.getBoolean("is_closed")
                                )
                            )
                        }
                    }
                    goToResultFragment()
                }
            }
        })
    }


    private fun goToResultFragment() {
        val supportFragmentManager = requireActivity().supportFragmentManager
        val fragmentTag = ResultFragment::class.java.simpleName

        supportFragmentManager.commit {
            supportFragmentManager.fragments.forEach { hide(it) }
            val fragment = supportFragmentManager.findFragmentByTag(fragmentTag)
            if (fragment != null) {
                show(fragment)
            } else {
                val nextFragment = ResultFragment()
                add(R.id.nav_host_fragment, nextFragment, nextFragment::class.java.simpleName)

            }
        }
    }

    private fun getPreferenceList(preference : String, jsonItem : JSONObject) : ArrayList<String>{
        val items = jsonItem.getJSONArray(preference)
        val list = ArrayList<String>()
        for (j in 0 until items.length()) {
            val currentItem = JSONObject(items[j].toString())
            list.add(currentItem.getString("name"))
        }
        return list
    }

}