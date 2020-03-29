package com.example.campusguide

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.hamcrest.CoreMatchers

class SystemTestUtils {

    companion object {

        fun startActivityFromHomeScreen(device: UiDevice, timeout: Long) {

            device.pressHome()

            val launcherPackage: String = device.launcherPackageName
            ViewMatchers.assertThat(launcherPackage, CoreMatchers.notNullValue())
            device.wait(
                Until.hasObject(By.pkg(launcherPackage).depth(0)),
                timeout
            )

            val context = ApplicationProvider.getApplicationContext<Context>()
            val intent = context.packageManager.getLaunchIntentForPackage("com.example.campusguide")
            context.startActivity(intent)

            device.wait(
                Until.hasObject(By.pkg("com.example.campusguide").depth(0)),
                timeout
            )
        }
    }
}