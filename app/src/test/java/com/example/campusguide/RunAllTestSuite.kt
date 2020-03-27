package com.example.campusguide

import com.example.campusguide.directions.*
import com.example.campusguide.location.CenterLocationListenerTest
import com.example.campusguide.search.BuildingIndexSingletonTest
import com.example.campusguide.search.IndoorLocationProviderTest
import com.example.campusguide.search.IndoorSearchResultProviderTest
import com.example.campusguide.utils.DatabaseTest
import com.example.campusguide.utils.MessageDialogFragmentTest
import com.example.campusguide.utils.request.ApiKeyRequestDecoratorTest
import com.example.campusguide.utils.request.RequestQueueTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    EmptyDirectionsGuardTest::class,
    DatabaseTest::class,
    RequestQueueTest::class,
    DirectionsTest::class,
    ApiKeyRequestDecoratorTest::class,
    MessageDialogFragmentTest::class,
    CallbackDirectionsConfirmTest::class,
    CenterLocationListenerTest::class,
    IndoorSearchResultProviderTest::class,
    IndoorLocationProviderTest::class,
    BuildingIndexSingletonTest::class,
    GraphTest::class,
    PathfindingTest::class
)

class RunAllTestSuite