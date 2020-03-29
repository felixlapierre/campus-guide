package com.example.campusguide

import com.example.campusguide.directions.CallbackDirectionsConfirmTest
import com.example.campusguide.directions.DirectionsTest
import com.example.campusguide.directions.EmptyDirectionsGuardTest
import com.example.campusguide.location.CenterLocationListenerTest
import com.example.campusguide.map.BuildingClickListenerTest
import com.example.campusguide.search.BuildingIndexSingletonTest
import com.example.campusguide.search.IndoorLocationProviderTest
import com.example.campusguide.search.IndoorSearchResultProviderTest
import com.example.campusguide.utils.DatabaseTest
import com.example.campusguide.utils.MessageDialogFragmentTest
import com.example.campusguide.utils.PolygonUtilsTest
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
    PolygonUtilsTest::class,
    BuildingClickListenerTest::class
)

class RunAllTestSuite