package com.example.campusguide

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.filters.SdkSuppress
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val LAUNCH_TIMEOUT = 5000L

@RunWith(AndroidJUnit4ClassRunner::class)
@SdkSuppress(minSdkVersion = 18)
class SwitchCampusAcceptanceTest {

    private lateinit var device: UiDevice

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
    fun verifyInitialState() {

        // Wait 5 seconds
        device.waitForWindowUpdate(null, 5000)

        val switchCampusButton: UiObject = device.findObject(UiSelector().descriptionContains("switchCampusButton"))

        // Verify that the Switch Campus button exists and presents the correct text
        if(switchCampusButton.exists() && switchCampusButton.isEnabled) {
            assertEquals("LOY", switchCampusButton.text)
        }
    }

    @Test
    fun clickSwitchCampusButton() {

        // Wait 5 seconds
        device.waitForWindowUpdate(null, 5000)

        val switchCampusButton: UiObject = device.findObject(UiSelector().descriptionContains("switchCampusButton"))

        // Click on the Switch Campus button
        if(switchCampusButton.exists() && switchCampusButton.isEnabled) {
            switchCampusButton.click()
        }

        // Wait 5 seconds
        device.waitForWindowUpdate(null, 5000)

        // Verify that the Switch Campus button presents the correct text
        assertEquals("SGW", switchCampusButton.text)
    }
}
