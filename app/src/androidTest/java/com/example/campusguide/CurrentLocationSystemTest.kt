package com.example.campusguide

import androidx.test.filters.SdkSuppress
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import database.ObjectBox
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val TIMEOUT = 5000L

@RunWith(AndroidJUnit4ClassRunner::class)
@SdkSuppress(minSdkVersion = 18)
class CurrentLocationSystemTest {

    private var device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @get:Rule
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Before
    fun setUp() {
        SystemTestUtils.startActivityFromHomeScreen(device, TIMEOUT)
    }

    @Test
    fun clickCurrentLocationButton() {

        // Wait until the map is loaded
        device.wait(Until.hasObject(By.desc(Constants.MAPS_ACTIVITY_CONTENT_DESCRIPTION)), TIMEOUT)

        val currentLocationButton: UiObject = device.findObject(UiSelector().descriptionContains("currentLocationButton"))

        if (currentLocationButton.exists() && currentLocationButton.isEnabled) {
            currentLocationButton.click()
        }

        // Wait until the Current Location marker appears
        device.wait(Until.hasObject(By.desc("You are here.")), TIMEOUT)

        val marker: UiObject = device.findObject(UiSelector().descriptionContains("You are here."))
        // Note: The content-description field of Google Maps markers has the following format: {markerTitle.markerSnippet}
        assertEquals("You are here. ", marker.contentDescription)
    }

    @After
    fun cleanUp() {
        ObjectBox.boxStore.close()
    }
}
