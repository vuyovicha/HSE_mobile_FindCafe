package com.example.new_bottom_navigation_ui

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RestaurantCriterions(val label : String, val value : String)

class MainActivity : AppCompatActivity() {

    companion object {
        var cousineOptions : ArrayList<RestaurantCriterions> = ArrayList()
        var dietaryRestrictionsOptions : ArrayList<RestaurantCriterions> = ArrayList()
        var establishmentTypeOptions : ArrayList<RestaurantCriterions> = ArrayList()
        var pricesOptions : ArrayList<RestaurantCriterions> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        cousineOptions = getOptions("cousine_field.txt")
        dietaryRestrictionsOptions = getOptions("dietary_restrictions_field.txt")
        establishmentTypeOptions = getOptions("establishment_type_field.txt")
        pricesOptions = getOptions("prices_field.txt.txt")
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

}