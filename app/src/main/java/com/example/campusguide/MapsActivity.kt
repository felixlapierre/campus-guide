package com.example.campusguide

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import database.AppDatabase
import database.RoomDatabaseSingleton

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker on Hall Building and move the camera
        val hall = LatLng(45.497290, -73.578824)
        mMap.addMarker(MarkerOptions().position(hall).title("Hall Building"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hall, 17.0f))
        addBuildingHighlights(googleMap)
    }

    fun addBuildingHighlights(googleMap: GoogleMap){
        val hall = PolygonOptions()
            .add(LatLng(45.497165, -73.579545),
            LatLng(45.497710, -73.579034),
            LatLng(45.497373, -73.578338),
            LatLng(45.496830, -73.578850)
            )

        val websterLibrary = PolygonOptions()
            .add(LatLng(45.496913, -73.578330),
                LatLng(45.496897, -73.578290),
                LatLng(45.496941, -73.578247),
                LatLng(45.496965, -73.578295),
                LatLng(45.497002, -73.578257),
                LatLng(45.497018, -73.578295),
                LatLng(45.497260, -73.578058),
                LatLng(45.497142, -73.577818),
                LatLng(45.497108, -73.577849),
                LatLng(45.497086, -73.577800),
                LatLng(45.497078, -73.577806),
                LatLng(45.497071, -73.577792),
                LatLng(45.497116, -73.577748),
                LatLng(45.497041, -73.577589),
                LatLng(45.496994, -73.577631),
                LatLng(45.496989, -73.577620),
                LatLng(45.496999, -73.577612),
                LatLng(45.496978, -73.577568),
                LatLng(45.497008, -73.577538),
                LatLng(45.496892, -73.577294),
                LatLng(45.496616, -73.577557),
                LatLng(45.496637, -73.577601),
                LatLng(45.496583, -73.577651),
                LatLng(45.496496, -73.577466),
                LatLng(45.496256, -73.577698),
                LatLng(45.496669, -73.578567),
                LatLng(45.496706, -73.578531),
                LatLng(45.496729, -73.578579),
                LatLng(45.496889, -73.578415),
                LatLng(45.496869, -73.578372)
            )

        val bAnnex = PolygonOptions()
            .add(
                LatLng(45.497883, -73.579384),
                LatLng(45.497917, -73.579453),
                LatLng(45.497741, -73.579636),
                LatLng(45.497705, -73.579560)
            )

        val ev = PolygonOptions()
            .add(
                LatLng(45.4952462, -73.5780194),
                LatLng(45.4952631, -73.5779996),
                LatLng(45.4952465, -73.5779128),
                LatLng(45.4952328, -73.5779141),
                LatLng(45.4952375, -73.5778752),
                LatLng(45.4952965, -73.5777948),
                LatLng(45.4953531, -73.5777415),
                LatLng(45.4953541, -73.5776955),
                LatLng(45.4954455, -73.5776097),
                LatLng(45.4958638, -73.5784952),
                LatLng(45.4955938, -73.5787597)
                //LatLng(45.4952453, -73.5780184)
        )

        val gm = PolygonOptions()
            .add(
                LatLng(45.4957625, -73.5791047),
                LatLng(45.4957797, -73.5791463),
                LatLng(45.4961278, -73.5788070),
                LatLng(45.4959454, -73.5784318),
                LatLng(45.4958591, -73.5785072),
                LatLng(45.4956175, -73.5787449),
                LatLng(45.4957781, -73.5790909)
            )

        val jmsb = PolygonOptions()
            .add(
                LatLng(45.495187, -73.578526),
                LatLng(45.495006, -73.578737),
                LatLng(45.495038, -73.578791),
                LatLng(45.495007, -73.578824),
                LatLng(45.495166, -73.579171),
                LatLng(45.495222, -73.579115),
                LatLng(45.495358, -73.579367),
                LatLng(45.495519, -73.579200),
                LatLng(45.495440, -73.578962)
            )

        val muAnnex = PolygonOptions()
            .add(
                LatLng(45.497963, -73.579537),
                LatLng(45.497785, -73.579704),
                LatLng(45.497748, -73.579629),
                LatLng(45.497920, -73.579460)
            )

        val dAnnex = PolygonOptions()
            .add(
                LatLng(45.497849, -73.579311),
                LatLng(45.497811, -73.579232),
                LatLng(45.497707, -73.579342),
                LatLng(45.497742, -73.579412)
            )

        val miAnnex = PolygonOptions()
            .add(
                LatLng(45.497811, -73.579233),
                LatLng(45.497779, -73.579167),
                LatLng(45.497682, -73.579260),
                LatLng(45.497695, -73.579285),
                LatLng(45.497623, -73.579354),
                LatLng(45.497648, -73.579403)
            )

        val ciAnnex = PolygonOptions()
            .add(
                LatLng(45.497407, -73.580016),
                LatLng(45.497585, -73.579839),
                LatLng(45.497546, -73.579760),
                LatLng(45.497366, -73.579933)
            )

        val sAnnex = PolygonOptions()
            .add(
                LatLng(45.497366, -73.579933),
                LatLng(45.497325, -73.579851),
                LatLng(45.497426, -73.579752),
                LatLng(45.497437, -73.579773),
                LatLng(45.497460, -73.579752),
                LatLng(45.497491, -73.579815)
            )

        val mAnnex = PolygonOptions()
            .add(
                LatLng(45.497325, -73.579851),
                LatLng(45.497290, -73.579781),
                LatLng(45.497390, -73.579681),
                LatLng(45.497426, -73.579752)
            )

        val cbBuilding = PolygonOptions()
            .add(
                LatLng(45.495030, -73.574065),
                LatLng(45.495107, -73.573991),
                LatLng(45.495137, -73.574054),
                LatLng(45.495195, -73.573997),
                LatLng(45.495165, -73.573934),
                LatLng(45.495246, -73.573856),
                LatLng(45.495403, -73.574190),
                LatLng(45.495061, -73.574520),
                LatLng(45.494942, -73.574270),
                LatLng(45.495009, -73.574204),
                LatLng(45.495002, -73.574189),
                LatLng(45.495062, -73.574131)
            )

        val enAnnex = PolygonOptions()
            .add(
                LatLng(45.496932, -73.579553),
                LatLng(45.496892, -73.579470),
                LatLng(45.496788, -73.579574),
                LatLng(45.496803, -73.579607),
                LatLng(45.496676, -73.579734),
                LatLng(45.496700, -73.579784)
            )

        val erBuilding = PolygonOptions()
            .add(
                LatLng(45.496262, -73.580352),
                LatLng(45.496162, -73.580070),
                LatLng(45.496249, -73.579973),
                LatLng(45.496217, -73.579906),
                LatLng(45.496523, -73.579633),
                LatLng(45.496679, -73.579971)
            )

        val fbBuilding = PolygonOptions()
            .add(
                LatLng(45.494696, -73.578039),
                LatLng(45.494913, -73.577786),
                LatLng(45.494870, -73.577713),
                LatLng(45.494877, -73.577705),
                LatLng(45.494836, -73.577633),
                LatLng(45.494843, -73.577626),
                LatLng(45.494799, -73.577550),
                LatLng(45.494807, -73.577541),
                LatLng(45.494764, -73.577465),
                LatLng(45.494775, -73.577453),
                LatLng(45.494692, -73.577309),
                LatLng(45.494701, -73.577299),
                LatLng(45.494655, -73.577219),
                LatLng(45.494397, -73.577521)
            )

        val gsBuilding = PolygonOptions()
            .add(
                LatLng(45.496578, -73.581437),
                LatLng(45.496415, -73.580943),
                LatLng(45.496487, -73.580874),
                LatLng(45.496477, -73.580845),
                LatLng(45.496518, -73.580804),
                LatLng(45.496653, -73.581172),
                LatLng(45.496711, -73.581128),
                LatLng(45.496785, -73.581302),
                LatLng(45.4966265, -73.5814486)
            )

        val kAnnex = PolygonOptions()
            .add(
                LatLng(45.497883, -73.579384),
                LatLng(45.497849, -73.579311),
                LatLng(45.497754, -73.579401),
                LatLng(45.497772, -73.579439),
                LatLng(45.497697, -73.579510),
                LatLng(45.497686, -73.579487),
                LatLng(45.497597, -73.579572),
                LatLng(45.497627, -73.579635)
            )

        val ldBuilding = PolygonOptions()
            .add(
                LatLng(45.496859, -73.577171),
                LatLng(45.496564, -73.577471),
                LatLng(45.496522, -73.577387),
                LatLng(45.496818, -73.577087)
            )

        val gaAnnex = PolygonOptions()
            .add(
                LatLng(45.4943430, -73.5777361),
                LatLng(45.4942826, -73.5776241),
                LatLng(45.494053, -73.577868),
                LatLng(45.4940739, -73.5779064),
                LatLng(45.4937893, -73.5782534),
                LatLng(45.4938474, -73.5783523),
                LatLng(45.4941332, -73.5780087),
                LatLng(45.4941191, -73.5779832)
            )

        val fgBuilding = PolygonOptions()
            .add(
                LatLng(45.4946949, -73.5780358),
                LatLng(45.4944521, -73.5776147),
                LatLng(45.4943853, -73.5776892),
                LatLng(45.4944260, -73.5777592),
                LatLng(45.4943914, -73.5778018),
                LatLng(45.4943696, -73.5777666),
                LatLng(45.4941849, -73.5779842),
                LatLng(45.4942018, -73.5780144),
                LatLng(45.4941108, -73.5781247),
                LatLng(45.4941043, -73.5781119),
                LatLng(45.4939087, -73.5783403),
                LatLng(45.4939207, -73.5783614),
                LatLng(45.4938918, -73.5783969),
                LatLng(45.4938817, -73.5783835),
                LatLng(45.4938337, -73.5784372),
                LatLng(45.4938481, -73.5784600),
                LatLng(45.4936253, -73.5787272),
                LatLng(45.4938213, -73.5790655),
                LatLng(45.4942932, -73.5785096),
                LatLng(45.4943007, -73.5785200),
                LatLng(45.4943677, -73.5784388),
                LatLng(45.4943611, -73.5784264)
            )

        val faAnnex = PolygonOptions()
            .add(
                LatLng(45.4967752, -73.5795838),
                LatLng(45.4968697, -73.5794846),
                LatLng(45.4968302, -73.5794031),
                LatLng(45.4967329, -73.5794996)
            )

        val clAnnex = PolygonOptions()
            .add(
                LatLng(45.494471,-73.579282),
                LatLng(45.4941639, -73.5796579),
                LatLng(45.4939839, -73.5793525),
                LatLng(45.4939785, -73.5793243),
                LatLng(45.4939900, -73.5792874),
                LatLng(45.4940112, -73.5792472),
                LatLng(45.4940328, -73.5792106),
                LatLng(45.494260, -73.578942)
            )

        val lsBuilding = PolygonOptions()
            .add(
                LatLng(45.4961889, -73.5797400),
                LatLng(45.4961574, -73.5796646),
                LatLng(45.4961825, -73.5796395),
                LatLng(45.4961137, -73.5794799),
                LatLng(45.4959508, -73.5796230),
                LatLng(45.4960528, -73.5798567)
            )

        val pAnnex = PolygonOptions()
            .add(
                LatLng(45.496723, -73.579171),
                LatLng(45.496677, -73.579081),
                LatLng(45.496582, -73.579177),
                LatLng(45.496629, -73.579267)
            )

        val prAnnex = PolygonOptions()
            .add(
                LatLng(45.497018, -73.579829),
                LatLng(45.496979, -73.579749),
                LatLng(45.496895, -73.579833),
                LatLng(45.496933, -73.579914)
            )

        val qAnnex = PolygonOptions()
            .add(
                LatLng(45.496676, -73.579082),
                LatLng(45.496646, -73.579023),
                LatLng(45.496553, -73.579120),
                LatLng(45.496582, -73.579177)
            )

        val rAnnex = PolygonOptions()
            .add(
                LatLng(45.496838, -73.579400),
                LatLng(45.496803, -73.579329),
                LatLng(45.496711, -73.579421),
                LatLng(45.496746, -73.579492)
            )

        val rrAnnex = PolygonOptions()
            .add(
                LatLng(45.496799, -73.579333),
                LatLng(45.496760, -73.579254),
                LatLng(45.496613, -73.579400),
                LatLng(45.496652, -73.579480)
            )
        val sbBuilding = PolygonOptions() //Needs a hole
            .add(
                LatLng(45.496438, -73.586207),
                LatLng(45.496468, -73.585738),
                LatLng(45.496493, -73.585739),
                LatLng(45.496511, -73.585779),
                LatLng(45.496510, -73.585792),
                LatLng(45.496554, -73.585792),
                LatLng(45.496574, -73.585836),
                LatLng(45.496582, -73.585830),
                LatLng(45.496659, -73.586008),
                LatLng(45.496652, -73.586014),
                LatLng(45.496684, -73.586088),
                LatLng(45.496538, -73.586212),
                LatLng(45.496519, -73.586168),
                LatLng(45.496521, -73.586136),
                LatLng(45.496506, -73.586136),
                LatLng(45.496501, -73.586208)
            )

        val tAnnex = PolygonOptions()
            .add(
                LatLng(45.496752, -73.579261),
                LatLng(45.496712, -73.579184),
                LatLng(45.496626, -73.579262),
                LatLng(45.496668, -73.579345)
            )

        val tdBuilding = PolygonOptions()
            .add(
                LatLng(45.495128, -73.578501),
                LatLng(45.495190, -73.578428),
                LatLng(45.495038, -73.578074),
                LatLng(45.494944, -73.578178),
                LatLng(45.495025, -73.578324),
                LatLng(45.495043, -73.578300),
                LatLng(45.495066, -73.578342),
                LatLng(45.495048, -73.578365)
            )

        val vAnnex = PolygonOptions()
            .add(
                LatLng(45.497089, -73.579918),
                LatLng(45.497049, -73.579834),
                LatLng(45.496946, -73.579938),
                LatLng(45.496985, -73.580019)
            )

        val vaBuilding = PolygonOptions()
            .add(
                LatLng(45.495403, -73.573766),
                LatLng(45.495668, -73.573509),
                LatLng(45.495817, -73.573811),
                LatLng(45.496070, -73.573559),
                LatLng(45.496187, -73.573796),
                LatLng(45.495670, -73.574310)
            )

        val xAnnex = PolygonOptions()
            .add(
                LatLng(45.496949, -73.579666),
                LatLng(45.496907, -73.579579),
                LatLng(45.496815, -73.579671),
                LatLng(45.496857, -73.579758)
            )

        val zAnnex = PolygonOptions()
            .add(
                LatLng(45.496986, -73.579742),
                LatLng(45.496949, -73.579666),
                LatLng(45.496851, -73.579764),
                LatLng(45.496875, -73.579813),
                LatLng(45.496898, -73.579790),
                LatLng(45.496910, -73.579817)
            )

        googleMap.addPolygon(hall)
        googleMap.addPolygon(websterLibrary)
        googleMap.addPolygon(bAnnex)
        googleMap.addPolygon(ev)
        googleMap.addPolygon(gm)
        googleMap.addPolygon(jmsb)
        googleMap.addPolygon(muAnnex)
        googleMap.addPolygon(dAnnex)
        googleMap.addPolygon(miAnnex)
        googleMap.addPolygon(ciAnnex)
        googleMap.addPolygon(sAnnex)
        googleMap.addPolygon(mAnnex)
        googleMap.addPolygon(cbBuilding)
        googleMap.addPolygon(enAnnex)
        googleMap.addPolygon(erBuilding)
        googleMap.addPolygon(fbBuilding)
        googleMap.addPolygon(gsBuilding)
        googleMap.addPolygon(kAnnex)
        googleMap.addPolygon(ldBuilding)
        googleMap.addPolygon(gaAnnex)
        googleMap.addPolygon(fgBuilding)
        googleMap.addPolygon(faAnnex)
        googleMap.addPolygon(clAnnex)
        googleMap.addPolygon(lsBuilding)
        googleMap.addPolygon(pAnnex)
        googleMap.addPolygon(prAnnex)
        googleMap.addPolygon(qAnnex)
        googleMap.addPolygon(rAnnex)
        googleMap.addPolygon(rrAnnex)
        googleMap.addPolygon(sbBuilding)
        googleMap.addPolygon(tAnnex)
        googleMap.addPolygon(tdBuilding)
        googleMap.addPolygon(vaBuilding)
        googleMap.addPolygon(xAnnex)
        googleMap.addPolygon(zAnnex)
    }
}
