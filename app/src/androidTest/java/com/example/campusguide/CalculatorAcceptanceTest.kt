package com.example.campusguide

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
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
        onView(withId(R.id.firstNumber)).perform(typeText("3"), closeSoftKeyboard())
        onView(withId(R.id.secondNumber)).perform(typeText("5"), closeSoftKeyboard())
        onView(withId(R.id.calculateButton)).perform(click())

        onView(withId(R.id.resultTextView)).check(matches(withText("8")))
    }
}
