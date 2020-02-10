package com.example.campusguide

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class CalculatorAcceptanceTest {
    @get:Rule
    var activityRule: ActivityTestRule<CalculatorActivity>
        = ActivityTestRule(CalculatorActivity::class.java)

    @Test
    fun addTwoNumbers() {
        val device = UiDevice.getInstance(getInstrumentation())

        val text1 = device.findObject(UiSelector().className("android.widget.EditText").instance(0))
        text1.setText("3")

        val text2 = device.findObject(UiSelector().className("android.widget.EditText").instance(1))
        text2.setText("5")
        val button = device.findObject(By.text("CALCULATE"))
        button.click()

        val result = device.findObject(UiSelector().className("android.widget.TextView"))

        assert(result.text.equals("8"))
    }
}
