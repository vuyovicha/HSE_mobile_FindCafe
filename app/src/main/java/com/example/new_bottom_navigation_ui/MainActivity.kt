package com.example.new_bottom_navigation_ui

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.IdRes
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*

class RestaurantCriterions(val label : String, val value : String)

class MainActivity : AppCompatActivity() {

    companion object {
        var cousineOptions : ArrayList<RestaurantCriterions> = ArrayList()
        var dietaryRestrictionsOptions : ArrayList<RestaurantCriterions> = ArrayList()
        var establishmentTypeOptions : ArrayList<RestaurantCriterions> = ArrayList()
        var pricesOptions : ArrayList<RestaurantCriterions> = ArrayList()
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        val navView: BottomNavigationView = findViewById(R.id.nav_view)
//
//        val navController = findNavController(R.id.nav_host_fragment)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//
//        cousineOptions = getOptions("cousine_field.txt")
//        dietaryRestrictionsOptions = getOptions("dietary_restrictions_field.txt")
//        establishmentTypeOptions = getOptions("establishment_type_field.txt")
//        pricesOptions = getOptions("prices_field.txt")
//    }

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

    /////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nav_view.setOnNavigationItemSelectedListener {
            setFragment(it.itemId)
            true
        }

        if (savedInstanceState == null) {
            setFragment(R.id.navigation_map)
            nav_view.menu.getItem(1).isChecked = true
        }

        cousineOptions = getOptions("cousine_field.txt")
        dietaryRestrictionsOptions = getOptions("dietary_restrictions_field.txt")
        establishmentTypeOptions = getOptions("establishment_type_field.txt")
        pricesOptions = getOptions("prices_field.txt")
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

}