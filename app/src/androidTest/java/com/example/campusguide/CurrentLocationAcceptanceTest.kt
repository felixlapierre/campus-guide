package com.example.campusguide

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.filters.SdkSuppress
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.*
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val TIMEOUT = 5000L

@RunWith(AndroidJUnit4ClassRunner::class)
@SdkSuppress(minSdkVersion = 18)
class CurrentLocationAcceptanceTest {

    private lateinit var device: UiDevice

    @get:Rule
    var permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Before
    fun startMainActivityFromHomeScreen() {

        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        device.pressHome()

        val launcherPackage: String = device.launcherPackageName
        assertThat(launcherPackage, notNullValue())
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), TIMEOUT)

        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage("com.example.campusguide")
        context.startActivity(intent)

        device.wait(Until.hasObject(By.pkg("com.example.campusguide").depth(0)), TIMEOUT)
    }

    @Test
    fun clickCurrentLocationButton() {

        // Wait until the map is loaded
        device.wait(Until.hasObject(By.desc("Google Maps Ready")), TIMEOUT)


        val currentLocationButton: UiObject = device.findObject(UiSelector().descriptionContains("currentLocationButton"))

        if(currentLocationButton.exists() && currentLocationButton.isEnabled) {
            currentLocationButton.click()
        }

        // Wait until the Current Location marker appears
        device.wait(Until.hasObject(By.desc("You are here.")), TIMEOUT)

        val marker: UiObject = device.findObject(UiSelector().descriptionContains("You are here."))
        // Note: The content-description field of Google Maps markers has the following format: {markerTitle.markerSnippet}
        assertEquals("You are here.. ", marker.contentDescription)

    }
}
