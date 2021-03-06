package com.example.campusguide

import com.example.campusguide.directions.AccessiblePathfindingTest
import com.example.campusguide.directions.CallbackDirectionsConfirmTest
import com.example.campusguide.directions.DirectionsTest
import com.example.campusguide.directions.EmptyDirectionsGuardTest
import com.example.campusguide.directions.GraphTest
import com.example.campusguide.directions.IndoorPathfindingTest
import com.example.campusguide.location.CenterLocationListenerTest
import com.example.campusguide.location.LocationTest
import com.example.campusguide.location.SwitchCampusTest
import com.example.campusguide.map.BuildingClickListenerTest
import com.example.campusguide.search.BuildingIndexSingletonTest
import com.example.campusguide.search.IndoorLocationProviderTest
import com.example.campusguide.search.IndoorSearchResultProviderTest
import com.example.campusguide.search.SearchResultAdapterTest
import com.example.campusguide.utils.DatabaseTest
import com.example.campusguide.utils.HelperTest
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
    SwitchCampusTest::class,
    IndoorSearchResultProviderTest::class,
    IndoorLocationProviderTest::class,
    BuildingIndexSingletonTest::class,
    GraphTest::class,
    IndoorPathfindingTest::class,
    SearchResultAdapterTest::class,
    HelperTest::class,
    PolygonUtilsTest::class,
    BuildingClickListenerTest::class,
    LocationTest::class,
    AccessiblePathfindingTest::class
)

class RunAllTestSuite
