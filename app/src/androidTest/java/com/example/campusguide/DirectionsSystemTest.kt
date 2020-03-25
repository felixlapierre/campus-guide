package com.example.campusguide

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.filters.SdkSuppress
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import database.ObjectBox
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val TIMEOUT = 5000L

@RunWith(AndroidJUnit4ClassRunner::class)
@SdkSuppress(minSdkVersion = 18)
class DirectionsSystemTest {

    private lateinit var device: UiDevice
    private val concordiaUniversityAddress = "1455 De Maisonneuve"
    private val travelModes = arrayOf("driving", "walking", "transit")

    @Before
    fun startMainActivityFromHomeScreen() {

        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        device.pressHome()

        val launcherPackage: String = device.launcherPackageName
        assertThat(launcherPackage, notNullValue())
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
            TIMEOUT
        )

        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage("com.example.campusguide")
        context.startActivity(intent)

        device.wait(Until.hasObject(By.pkg("com.example.campusguide").depth(0)),
            TIMEOUT
        )
    }

    @Test
    fun getDirectionsToConcordiaFromCurrentLocation() {

        // Wait until the map of MapsActivity is loaded
        device.wait(Until.hasObject(By.desc("maps_activity_map ready")), TIMEOUT)

        // Click the Navigate button
        val navigateButton: UiObject = device.findObject(UiSelector().descriptionContains("navigateButton"))
        if(navigateButton.exists() && navigateButton.isEnabled) {
            navigateButton.click()
        }

        // Wait until the Choose Directions dialog appears
        device.wait(Until.hasObject(By.desc("chooseDirections")), TIMEOUT)

        // Click on the Use Current Location option
        val useCurrentLocationOption: UiObject = device.findObject(UiSelector().descriptionContains("useCurrentLocation"))
        if(useCurrentLocationOption.exists() && useCurrentLocationOption.isEnabled) {
            useCurrentLocationOption.click()
        }

        // Wait until the Get Directions dialog appears
        device.wait(Until.hasObject(By.desc("directionsDialogLayout")), TIMEOUT)

        // Get the current location
        val startLocationTextField: UiObject = device.findObject(UiSelector().descriptionContains("startLocationTextInput"))
        val currentLocation = startLocationTextField.text

        // Set the destination to Concordia University's address
        val endLocationTextField: UiObject = device.findObject(UiSelector().descriptionContains("endLocationTextInput"))
        endLocationTextField.text = concordiaUniversityAddress

        // Click on the Go Button
        val goButton: UiObject = device.findObject(UiSelector().text("GO"))
        if(goButton.exists() && goButton.isEnabled) {
            goButton.click()
        }

        // Wait until the map of DirectionsActivity is loaded
        device.wait(Until.hasObject(By.desc("directions_activity_map ready")), TIMEOUT)

        // Pick a random travel mode
        val travelMode = travelModes.random()

        // Click on the travel mode button
        val travelModeButton: UiObject = device.findObject(UiSelector().descriptionContains(travelMode))
        if (travelModeButton.exists() && travelModeButton.isEnabled) {
            travelModeButton.click()
        }

        // Wait until route is displayed
        val markerStart: UiObject = device.findObject(UiSelector().descriptionContains("Start"))
        val markerEnd: UiObject = device.findObject(UiSelector().descriptionContains("Destination"))
        device.wait(Until.hasObject(By.desc("Start")), TIMEOUT)

        assertEquals("$concordiaUniversityAddress. Destination.", markerEnd.contentDescription)
        assertEquals("$currentLocation. Start.", markerStart.contentDescription)
        assertThat(travelModeButton.text, not("-"))
    }

    @After
    fun cleanUp() {
        ObjectBox.boxStore.close()
    }
}