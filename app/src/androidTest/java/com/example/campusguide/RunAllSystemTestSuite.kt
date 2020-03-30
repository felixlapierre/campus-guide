package com.example.campusguide

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    CurrentLocationSystemTest::class,
    SwitchCampusSystemTest::class,
    DirectionsSystemTest::class
)

class RunAllSystemTestSuite