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

private const val LAUNCH_TIMEOUT = 5000L

@RunWith(AndroidJUnit4ClassRunner::class)
@SdkSuppress(minSdkVersion = 18)
class AcceptanceTest {

    private lateinit var device: UiDevice

    @get:Rule
    var permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Before
    fun startMainActivityFromHomeScreen() {

        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        device.pressHome()

        val launcherPackage: String = device.launcherPackageName
        assertThat(launcherPackage, notNullValue())
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT)

        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage("com.example.campusguide")
        context.startActivity(intent)

        device.wait(Until.hasObject(By.pkg("com.example.campusguide").depth(0)), LAUNCH_TIMEOUT)
    }

    @Test
    fun checkPreconditions() {
        assertThat(device, notNullValue())
    }

    @Test
    fun clickCurrentLocationButton() {

        // Wait 5 seconds
        device.waitForWindowUpdate(null, 5000)

        val currentLocationButton: UiObject = device.findObject(UiSelector().descriptionContains("currentLocationButton"))

        if(currentLocationButton.exists() && currentLocationButton.isEnabled) {
            currentLocationButton.click()
        }

        // Wait 5 seconds
        device.waitForWindowUpdate(null, 5000)

        val marker: UiObject = device.findObject(UiSelector().descriptionContains("You are here."))
        // Note: The content-description field of Google Maps markers has the following format: {markerTitle.markerSnippet}
        assertEquals("You are here.. ", marker.contentDescription)

    }
}
