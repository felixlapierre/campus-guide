package com.example.campusguide

<<<<<<< HEAD
import com.example.campusguide.directions.CallbackDirectionsConfirmTest
import com.example.campusguide.directions.DirectionsTest
import com.example.campusguide.directions.EmptyDirectionsGuardTest
import com.example.campusguide.utils.DatabaseTest
import com.example.campusguide.utils.MessageDialogFragmentTest
import com.example.campusguide.utils.request.ApiKeyRequestDecoratorTest
import com.example.campusguide.utils.request.RequestQueueTest
=======
import com.example.campusguide.directions.EmptyDirectionsGuardTest
import com.example.campusguide.utils.DatabaseTest
import com.example.campusguide.utils.RequestQueueTest
>>>>>>> 973f2e3c5e1955b6153f6462568db5ecb5780ea7
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    EmptyDirectionsGuardTest::class,
    DatabaseTest::class,
<<<<<<< HEAD
    RequestQueueTest::class,
    DirectionsTest::class,
    ApiKeyRequestDecoratorTest::class,
    MessageDialogFragmentTest::class,
    CallbackDirectionsConfirmTest::class
=======
    RequestQueueTest::class
>>>>>>> 973f2e3c5e1955b6153f6462568db5ecb5780ea7
)

class RunAllTestSuite