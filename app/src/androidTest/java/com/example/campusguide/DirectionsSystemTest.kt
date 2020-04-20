package com.example.campusguide

import androidx.test.annotation.UiThreadTest
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.filters.SdkSuppress
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import com.google.android.material.internal.ContextUtils.getActivity
import database.ObjectBox
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val TIMEOUT = 5000L

@RunWith(AndroidJUnit4ClassRunner::class)
@SdkSuppress(minSdkVersion = 18)
class DirectionsSystemTest {

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    private val travelModes = arrayOf("driving", "walking", "transit")

    @get:Rule
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Before
    fun setUp() {
        SystemTestUtils.startActivityFromHomeScreen(device, TIMEOUT)
    }

    @Test
    fun getDirectionsToConcordiaFromCurrentLocation() {
        // Wait until the map of MapsActivity is loaded
        device.wait(Until.hasObject(By.desc(Constants.MAPS_ACTIVITY_CONTENT_DESCRIPTION)), TIMEOUT)

        // Click the Navigate button
        val navigateButton: UiObject = device.findObject(UiSelector().descriptionContains("directions"))
        if (navigateButton.exists() && navigateButton.isEnabled) {
            navigateButton.click()
        }

        // Wait until the ChooseDestinationOptions dialog appears
        device.wait(Until.hasObject(By.desc("chooseDestinationOptionsLayout")), TIMEOUT)

        // Click on the Search For Location button
        val searchForLocationButton: UiObject = device.findObject(UiSelector().descriptionContains("searchForLocationButton"))
        if (searchForLocationButton.exists() && searchForLocationButton.isEnabled) {
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
        if (useCurrentLocationButton.exists() && useCurrentLocationButton.isEnabled) {
            useCurrentLocationButton.click()
        }

        // Wait until the map of DirectionsActivity is loaded
        device.wait(Until.hasObject(By.desc(Constants.DIRECTIONS_ACTIVITY_CONTENT_DESCRIPTION)), TIMEOUT)

        // Pick a random travel mode
        // val travelMode = travelModes.random()
        val travelMode = "walking"

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

    @Test
    fun getDirectionsToConcordiaFromMcgill() {
        // Wait until the map of MapsActivity is loaded
        device.wait(
            Until.hasObject(By.desc(Constants.MAPS_ACTIVITY_CONTENT_DESCRIPTION)),
            TIMEOUT
        )

        // Click the Navigate button
        val navigateButton: UiObject =
            device.findObject(UiSelector().descriptionContains("directions"))
        if (navigateButton.exists() && navigateButton.isEnabled) {
            navigateButton.click()
        }

        // Wait until the ChooseDestinationOptions dialog appears
        device.wait(Until.hasObject(By.desc("chooseDestinationOptionsLayout")), TIMEOUT)

        // Click on the Search For Location button
        val searchForLocationButton: UiObject =
            device.findObject(UiSelector().descriptionContains("searchForLocationButton"))
        if (searchForLocationButton.exists() && searchForLocationButton.isEnabled) {
            searchForLocationButton.click()
        }

        // Wait until the CustomSearchActivity activity is launched
        device.wait(Until.hasObject(By.desc("customSearchLayout")), TIMEOUT)

        // Search for Concordia University
        // Note: For some reason, the regular setText() method doesn't work on a SearchView
        // So I had to resort to legacySetText()
        val searchTextField: UiObject =
            device.findObject(UiSelector().description("searchView"))
        searchTextField.legacySetText("Concordia University")

        // Select the first option
        var firstResult: UiObject = device.findObject(
            UiSelector().className("android.widget.TextView").text("Concordia University")
        )
        if (firstResult.exists() && firstResult.isEnabled) {
            firstResult.click()
        }

        // Wait until the ChooseOriginOptions dialog appears
        device.wait(Until.hasObject(By.desc("chooseOriginOptionsLayout")), TIMEOUT)

        // Click on the Search For Location button
        if (searchForLocationButton.exists() && searchForLocationButton.isEnabled) {
            searchForLocationButton.click()
        }

        // Wait until the CustomSearchActivity activity is launched
        device.wait(Until.hasObject(By.desc("customSearchLayout")), TIMEOUT)

        // Search for McGill University
        searchTextField.legacySetText("McGill University")

        // Select the first option
        firstResult = device.findObject(
            UiSelector().className("android.widget.TextView").text("McGill University")
        )
        if (firstResult.exists() && firstResult.isEnabled) {
            firstResult.click()
        }

        // Wait until the map of DirectionsActivity is loaded
        device.wait(
            Until.hasObject(By.desc(Constants.DIRECTIONS_ACTIVITY_CONTENT_DESCRIPTION)),
            TIMEOUT
        )

        // Pick a random travel mode
        // val travelMode = travelModes.random()
        val travelMode = "walking"

        // Click on the travel mode button
        val travelModeButton: UiObject =
            device.findObject(UiSelector().descriptionContains(travelMode))
        if (travelModeButton.exists() && travelModeButton.isEnabled) {
            travelModeButton.click()
        }

        // Wait until route is displayed
        val markerStart: UiObject = device.findObject(UiSelector().descriptionContains("Start"))
        val markerEnd: UiObject =
            device.findObject(UiSelector().descriptionContains("Destination"))
        device.wait(Until.hasObject(By.desc("Start")), TIMEOUT)

        assertThat(markerStart.contentDescription, containsString("Start"))
        assertThat(markerEnd.contentDescription, containsString("Destination"))
        assertThat(travelModeButton.text, not("-"))
    }

    @After
    fun cleanUp() {
        ObjectBox.boxStore.close()
    }
}
