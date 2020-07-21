package com.example.new_bottom_navigation_ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.SearchView
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.set_preferences_fragment_search.*

class RestaurantCriterions(val label : String, val value : String)

class  RestaurantCriterionsIndexed (val label : String, val value : String, val index : Int)

class DisplayAddress (var address : String = "", var coordinates : PointString = PointString())

class MainActivity : AppCompatActivity() {

    companion object {
        var cousineOptions : ArrayList<RestaurantCriterionsIndexed> = ArrayList()
        var dietaryRestrictionsOptions : ArrayList<RestaurantCriterionsIndexed> = ArrayList()
        var establishmentTypeOptions : ArrayList<RestaurantCriterionsIndexed> = ArrayList()
        var pricesOptions : ArrayList<RestaurantCriterionsIndexed> = ArrayList()
        var setPreferencesFragmentTag = "Cousin"
        var dietaryRestrictionsStates: BooleanArray = BooleanArray(0)
        var establishmentTypeStates: BooleanArray = BooleanArray(0)
        var cousineStates: BooleanArray = BooleanArray(0)
        var pricesState = 0
        var ratingState = 0
        var openState = true
        var fromPointFlag = false
        var routePosition = 0
        var foundRestaurants : ArrayList<Restaurant> = ArrayList()
        var mgr: LocationManager? = null
        const val GPS_ACCESS_CODE = 455
        var fromAddress = DisplayAddress()
        var toAddress = DisplayAddress()
    }


    fun getOptions(fileName: String) : ArrayList<RestaurantCriterions>
    {
        val stringJSON = readFileText(fileName)
        val GSON = Gson()
        val arrayRestaurantCriterionsType = object : TypeToken<ArrayList<RestaurantCriterions>>() {}.type
        return GSON.fromJson(stringJSON, arrayRestaurantCriterionsType)
    }

    fun readFileText(fileName: String): String {
        return assets.open(fileName).bufferedReader().use { it.readText() }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permissionStatus =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            mgr = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                GPS_ACCESS_CODE)
        }

        nav_view.setOnNavigationItemSelectedListener {
            setFragment(it.itemId)
            true
        }

        if (savedInstanceState == null) {
            setFragment(R.id.navigation_map)
            nav_view.menu.getItem(1).isChecked = true
        }

        val cousineOptionsNoIndex = getOptions("cousine_field.txt")
        cousineOptions = setIndexes(cousineOptionsNoIndex)

        val dietaryRestrictionsOptionsNoIndex = getOptions("dietary_restrictions_field.txt")
        dietaryRestrictionsOptions = setIndexes(dietaryRestrictionsOptionsNoIndex)
        dietaryRestrictionsOptions.removeAt(0)

        val establishmentTypeOptionsNoIndex = getOptions("establishment_type_field.txt")
        establishmentTypeOptions = setIndexes(establishmentTypeOptionsNoIndex)

        val pricesOptionsNoIndex = getOptions("prices_field.txt")
        pricesOptions = setIndexes(pricesOptionsNoIndex)

        dietaryRestrictionsStates = BooleanArray(dietaryRestrictionsOptions.count())
        establishmentTypeStates = BooleanArray(establishmentTypeOptions.count())
        cousineStates = BooleanArray(cousineOptions.count())

    }

    private fun setIndexes(optionsNoIndex : ArrayList<RestaurantCriterions>) : ArrayList<RestaurantCriterionsIndexed> {
        var cousineOptions : ArrayList<RestaurantCriterionsIndexed> = ArrayList()
        for (i in optionsNoIndex.indices) {
            cousineOptions.add(RestaurantCriterionsIndexed(optionsNoIndex[i].label, optionsNoIndex[i].value, i))
        }
        return cousineOptions
    }

    private fun getNextFragmentTag(@IdRes menuId: Int): String =
        when (menuId) {
            R.id.navigation_find ->
                FindFragment::class.java.simpleName

            R.id.navigation_map ->
                MapFragment::class.java.simpleName

            R.id.navigation_result ->
                ResultFragment::class.java.simpleName

            else ->
                throw IllegalStateException()
        }

    private fun setFragment(@IdRes id: Int) {
        val fragmentTag = getNextFragmentTag(id)

        supportFragmentManager.commit {
            supportFragmentManager.fragments.forEach { hide(it) }
            val fragment = supportFragmentManager.findFragmentByTag(fragmentTag)
            if (fragment != null) {
                show(fragment)
            } else {
                val nextFragment = getNextFragmentInstance(id)
                add(R.id.nav_host_fragment, nextFragment, nextFragment::class.java.simpleName)

            }
        }
    }

    private fun getNextFragmentInstance(@IdRes menuId: Int): Fragment =
        when (menuId) {
            R.id.navigation_find ->
                FindFragment()

            R.id.navigation_map ->
                MapFragment()

            R.id.navigation_result ->
                ResultFragment()

            else ->
                throw IllegalStateException()
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            GPS_ACCESS_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mgr = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    recreate()
                }
            }
        }
    }
}