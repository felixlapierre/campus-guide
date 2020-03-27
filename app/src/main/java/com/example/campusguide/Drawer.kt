package com.example.campusguide

import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.campusguide.calendar.ChooseCalendar
import com.example.campusguide.calendar.Login
import com.google.android.material.navigation.NavigationView

class Drawer constructor(private val activity: MapsActivity, private val login: Login) :
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    fun setupDrawer() {
        toolbar = activity.findViewById(R.id.toolbar)
        activity.setSupportActionBar(toolbar)

        drawerLayout = activity.findViewById(R.id.drawer_layout)
        navView = activity.findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            activity, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val userEmail = login.getUserEmail()
        when (item.itemId) {
            R.id.calendar -> {
//                val calendar = Calendar(this) , calendar.getCalendars()
                val chooseCalendar = ChooseCalendar(activity)
                chooseCalendar.show(activity.supportFragmentManager, "calendarList")
            }
//            R.id.nav_profile -> {
//                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
//            }
            R.id.login_button -> {
                if (item.title == "Login to an Account") {
                    login.signIn()
                }
                else if (item.title == "Log Out of $userEmail") {
                    Toast.makeText(activity, "Logged Out", Toast.LENGTH_LONG).show()
                    login.signOut()
                }
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}