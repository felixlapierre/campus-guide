package com.example.campusguide

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.filters.SdkSuppress
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import com.example.campusguide.utils.LocalResources
import database.ObjectBox
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val TIMEOUT = 5000L

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
    fun clickSwitchCampusButtonOnce() {

        // Wait until the map is loaded
        device.wait(Until.hasObject(By.desc("Google Maps Ready")), TIMEOUT)

        val switchCampusButton: UiObject = device.findObject(UiSelector().descriptionContains("switchCampusButton"))

        // Click the Switch Campus Button
        if(switchCampusButton.exists() && switchCampusButton.isEnabled) {
            switchCampusButton.click()
        }

        // Wait until the text of the Switch Campus button has changed
        device.wait(Until.hasObject(By.text(LocalResources.getContext().getString(R.string.sgw_campus))), TIMEOUT)

        // Verify that the Switch Campus button presents the correct text
        assertEquals(LocalResources.getContext().getString(R.string.sgw_campus), switchCampusButton.text)
    }

    @Test
    fun clickSwitchCampusButtonTwice() {

        // Wait until the map is loaded
        device.wait(Until.hasObject(By.desc("Google Maps Ready")), TIMEOUT)

        val switchCampusButton: UiObject = device.findObject(UiSelector().descriptionContains("switchCampusButton"))

        // Click the Switch Campus button twice
        if(switchCampusButton.exists() && switchCampusButton.isEnabled) {
            switchCampusButton.click()
            switchCampusButton.click()
        }

        // Wait until the text of the Switch Campus button has changed
        device.wait(Until.hasObject(By.text(LocalResources.getContext().getString(R.string.loy_campus))), TIMEOUT)

        // Verify that the Switch Campus button presents the correct text
        assertEquals(LocalResources.getContext().getString(R.string.loy_campus), switchCampusButton.text)
    }

    @After
    fun cleanUp() {
        ObjectBox.boxStore.close()
    }
}
