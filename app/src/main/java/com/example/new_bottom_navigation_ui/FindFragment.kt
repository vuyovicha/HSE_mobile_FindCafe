package com.example.new_bottom_navigation_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.new_bottom_navigation_ui.R
import com.example.new_bottom_navigation_ui.SharedViewModel
import kotlinx.android.synthetic.main.find_fragment.*
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

data class Restaurant(
    private val name : String,
    private val latitude : String,
    private val longitude : String,
    private val locationString : String,
    private val smallPhotoUrl : String,
    private val largePhotoUrl : String,
    private val rating : String,
    private val priceLevel : String,
    private val description : String,
    private val address : String,
    private val cousin : ArrayList<String>,
    private val dietaryRestrictions : ArrayList<String>,
    private val establishmentType : ArrayList<String>
)

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
        findFragmentRows.add(FindFragmentAdapter.Route("From"))
        findFragmentRows.add(FindFragmentAdapter.Route("To"))
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
            sendRequest()
        }

    }

    private fun sendRequest() {
        val prices = "&prices_restaurants=10953"
        val cousinType = "&combined_food=10660"
        val distance = "&distance=5"
        val establishmentType = "&restaurant_tagcategory=10591"
        val openNow = "open_now=true"
        val dietaryRestrictions = "&dietary_restrictions=10665"
        val rating = "&min_rating=3"
        val latitude = "&latitude=12.91285"
        val longitude = "&longitude=100.87808"

        val request = Request.Builder()
            .url("https://tripadvisor1.p.rapidapi.com/restaurants/list-by-latlng?limit=30&currency=EUR$prices$cousinType$distance$establishmentType$openNow$dietaryRestrictions&lunit=km&lang=en_US$rating$latitude$longitude")
            .get()
            .addHeader("x-rapidapi-host", "tripadvisor1.p.rapidapi.com")
            .addHeader("x-rapidapi-key", "9a35fdaa8bmsh64d201965518884p14fa46jsne83376c9e265")
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) { }
            override fun onResponse(call: okhttp3.Call, response: Response) {
                val requestedString = response.body()?.string().toString()
                val jsonObject = JSONObject(requestedString)
                jsonObject.remove("open_hours_options")
                jsonObject.remove("paging")
                MainActivity.foundRestaurants = ArrayList()
                val items = jsonObject.getJSONArray("data")
                for (i in 1 until items.length()) {
                    val item = JSONObject(items[i].toString())
                    if (item.length() > 40) {
                        val photo = JSONObject(JSONObject(item.getString("photo")).getString("images"))
                        val cousinesList = getPreferenceList("cuisine", item)
                        val dietaryRestrictionsList = getPreferenceList("dietary_restrictions", item)
                        val establishmentTypeList = getPreferenceList("establishment_types", item)
                        MainActivity.foundRestaurants.add(
                            Restaurant(
                                item.getString("name"),
                                item.getString("latitude"),
                                item.getString("longitude"),
                                item.getString("location_string"),
                                JSONObject(photo.getString("small")).getString("url"),
                                JSONObject(photo.getString("large")).getString("url"),
                                item.getString("rating"),
                                item.getString("price_level"),
                                item.getString("description"),
                                item.getString("address"),
                                cousinesList,
                                dietaryRestrictionsList,
                                establishmentTypeList
                                )
                        )
                    }
                }
            }
        })

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