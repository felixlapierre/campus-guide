package com.example.campusguide

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.filters.SdkSuppress
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.*
import database.ObjectBox
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val TIMEOUT = 5000L

@RunWith(AndroidJUnit4ClassRunner::class)
@SdkSuppress(minSdkVersion = 18)
class DirectionsSystemTest {

    private lateinit var device: UiDevice
    private val travelModes = arrayOf("driving", "walking", "transit")

    @get:Rule
    var permissionRule: GrantPermissionRule? = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

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

        // Wait until the ChooseDestinationOptions dialog appears
        device.wait(Until.hasObject(By.desc("chooseDestinationOptionsLayout")), TIMEOUT)

        // Click on the Search For Location button
        val searchForLocationButton: UiObject = device.findObject(UiSelector().descriptionContains("searchForLocationButton"))
        if(searchForLocationButton.exists() && searchForLocationButton.isEnabled) {
            searchForLocationButton.click()
        }

        // Wait until the CustomSearchActivity activity is launched
        device.wait(Until.hasObject(By.desc("customSearchLayout")), TIMEOUT)

        // Search for Concordia University
        // Note: For some reason, the regular setText() method doesn't work on a SearchView
        // So I had to resort to legacySetText()
        val searchTextField: UiObject = device.findObject(UiSelector().descriptionContains("searchView"))
        searchTextField.legacySetText("Concordia University")

        // Select the first option
        val firstResult: UiObject = device.findObject(UiSelector().className("android.widget.TextView").text("Concordia University"))
        if (firstResult.exists() && firstResult.isEnabled) {
            firstResult.click()
        }

        // Wait until the ChooseOriginOptions dialog appears
        device.wait(Until.hasObject(By.desc("chooseOriginOptionsLayout")), TIMEOUT)

        // Click on the Use Current Location button
        val useCurrentLocationButton: UiObject = device.findObject(UiSelector().descriptionContains("useCurrentLocationButton"))
        if(useCurrentLocationButton.exists() && useCurrentLocationButton.isEnabled) {
            useCurrentLocationButton.click()
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

        assertThat(markerStart.contentDescription, containsString("Start"))
        assertThat(markerEnd.contentDescription, containsString("Destination"))
        assertThat(travelModeButton.text, not("-"))
    }

//    @Test
//    fun getDirectionsToConcordiaFromMcGill() {
//
//        // Wait until the map of MapsActivity is loaded
//        device.wait(Until.hasObject(By.desc("maps_activity_map ready")), TIMEOUT)
//
//        // Click the Navigate button
//        val navigateButton: UiObject = device.findObject(UiSelector().descriptionContains("navigateButton"))
//        if(navigateButton.exists() && navigateButton.isEnabled) {
//            navigateButton.click()
//        }
//
//        // Wait until the Choose Directions dialog appears
//        device.wait(Until.hasObject(By.desc("chooseDirections")), TIMEOUT)
//
//        // Click on the Search for Location option
//        val searchForLocationOption: UiObject = device.findObject(UiSelector().descriptionContains("searchForLocation"))
//        if(searchForLocationOption.exists() && searchForLocationOption.isEnabled) {
//            searchForLocationOption.click()
//        }
//
//        // Wait until the Get Directions dialog appears
//        device.wait(Until.hasObject(By.desc("directionsDialogLayout")), TIMEOUT)
//
//        // Set the origin to Concordia University's address
//        val startLocationTextField: UiObject = device.findObject(UiSelector().descriptionContains("startLocationTextInput"))
//        startLocationTextField.text = concordiaUniversityAddress
//
//        // Set the destination to McGill University's address
//        val endLocationTextField: UiObject = device.findObject(UiSelector().descriptionContains("endLocationTextInput"))
//        endLocationTextField.text = mcgillUniversityAddress
//
//        // Click on the Go Button
//        val goButton: UiObject = device.findObject(UiSelector().text("GO"))
//        if(goButton.exists() && goButton.isEnabled) {
//            goButton.click()
//        }
//
//        // Wait until the map of DirectionsActivity is loaded
//        device.wait(Until.hasObject(By.desc("directions_activity_map ready")), TIMEOUT)
//
//        // Pick a random travel mode
//        val travelMode = travelModes.random()
//
//        // Click on the travel mode button
//        val travelModeButton: UiObject = device.findObject(UiSelector().descriptionContains(travelMode))
//        if (travelModeButton.exists() && travelModeButton.isEnabled) {
//            travelModeButton.click()
//        }
//
//        // Wait until route is displayed
//        val markerStart: UiObject = device.findObject(UiSelector().descriptionContains("Start"))
//        val markerEnd: UiObject = device.findObject(UiSelector().descriptionContains("Destination"))
//        device.wait(Until.hasObject(By.desc("Start")), TIMEOUT)
//
//        assertEquals("$concordiaUniversityAddress. Start.", markerStart.contentDescription)
//        assertEquals("$mcgillUniversityAddress. Destination.", markerEnd.contentDescription)
//        assertThat(travelModeButton.text, not("-"))
//    }

    @After
    fun cleanUp() {
        ObjectBox.boxStore.close()
    }
}