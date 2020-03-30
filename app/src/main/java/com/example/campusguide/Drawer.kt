package com.example.campusguide

import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.campusguide.calendar.Calendar
import com.example.campusguide.calendar.ChooseCalendarDialogFragment
import com.example.campusguide.calendar.Login
import com.google.android.material.navigation.NavigationView

class Drawer constructor (
    private val activity: MapsActivity,
    private val login: Login
) : NavigationView.OnNavigationItemSelectedListener {

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
                handleCalendarSelect(userEmail)
            }
            R.id.login_button -> {
                handleLoginSelect(item, userEmail)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun handleCalendarSelect(userEmail: String){
        val calendar = Calendar(activity, userEmail)
        val calendarsList = calendar.getCalendars()
        val chooseCalendar = ChooseCalendarDialogFragment(calendar, calendarsList)
        chooseCalendar.show(activity.supportFragmentManager, "calendarList")
    }

    private fun handleLoginSelect(item: MenuItem, userEmail: String){
        if (item.title == "Login to an Account") {
            login.signIn()
        }
        else if (item.title == "Log Out of $userEmail") {
            login.signOut()
            Calendar(activity, userEmail).unsetCalendar()
        }
    }
}