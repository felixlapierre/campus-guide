package com.example.campusguide

import androidx.test.filters.SdkSuppress
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import com.example.campusguide.utils.LocalResources
import database.ObjectBox
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val TIMEOUT = 5000L

@RunWith(AndroidJUnit4ClassRunner::class)
@SdkSuppress(minSdkVersion = 18)
class SwitchCampusSystemTest {

    private var device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Before
    fun setUp() {
        SystemTestUtils.startActivityFromHomeScreen(device, TIMEOUT)
    }

    @Test
    fun clickSwitchCampusButtonOnce() {

        // Wait until the map is loaded
        device.wait(Until.hasObject(By.desc(Constants.MAPS_ACTIVITY_CONTENT_DESCRIPTION)), TIMEOUT)

        val switchCampusButton: UiObject = device.findObject(UiSelector().descriptionContains("switchCampusButton"))

        // Click the Switch Campus Button
        if (switchCampusButton.exists() && switchCampusButton.isEnabled) {
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
        device.wait(Until.hasObject(By.desc(Constants.MAPS_ACTIVITY_CONTENT_DESCRIPTION)), TIMEOUT)

        val switchCampusButton: UiObject = device.findObject(UiSelector().descriptionContains("switchCampusButton"))

        // Click the Switch Campus button twice
        if (switchCampusButton.exists() && switchCampusButton.isEnabled) {
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
