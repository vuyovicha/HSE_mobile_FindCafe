package com.hse.findcafe

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hse.findcafe.ui.home.HomeFragment
import com.hse.findcafe.ui.saved_routes.SavedRoutesFragment
import com.hse.findcafe.ui.user.UserFragment


class MainActivity : AppCompatActivity() {

    /*val fragment1: Fragment = HomeFragment()
    val fragment2: Fragment = SavedRoutesFragment()
    val fragment3: Fragment = UserFragment()
    val fm: FragmentManager = supportFragmentManager
    var active: Fragment = fragment1*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_home, R.id.navigation_saved_routes, R.id.navigation_notifications))
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        /*navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    fm.beginTransaction().hide(active).show(fragment1).commit()
                    active = fragment1
                    //Toast.makeText(applicationContext, "home", Toast.LENGTH_SHORT).show()
                }
                R.id.navigation_saved_routes -> {
                    fm.beginTransaction().hide(active).show(fragment2).commit()
                    active = fragment2
                    //Toast.makeText(applicationContext, "2", Toast.LENGTH_SHORT).show()
                }
                R.id.navigation_user -> {
                    fm.beginTransaction().hide(active).show(fragment3).commit()
                    active = fragment3
                    //Toast.makeText(applicationContext, "3", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }

        fm.beginTransaction().add(R.id.nav_host_fragment, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment,fragment1, "1").commit();
*/

    }
}
