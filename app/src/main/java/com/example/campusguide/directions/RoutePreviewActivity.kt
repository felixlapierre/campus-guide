package com.example.campusguide.directions

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.R
import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.map.GoogleMapInitializer
import com.example.campusguide.map.displayIndoor.FloorPlans
import com.example.campusguide.search.indoor.BuildingIndexSingleton
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class RoutePreviewActivity : AppCompatActivity() {
    private lateinit var map: GoogleMapAdapter
    private lateinit var routePreviewData: RoutePreviewData
    private lateinit var steps: List<GoogleDirectionsAPIStep>
    private lateinit var stepPath: List<Path>
    private var currentStepPath: MutableList<Path> = mutableListOf()
    private lateinit var previousStepButton: Button
    private lateinit var nextStepButton: Button
    private var i: Int = 0
    private val floorPlans = FloorPlans()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_preview)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        map = GoogleMapAdapter()
        val initializer = GoogleMapInitializer(
            this,
            map,
            "route_preview_activity_map",
            null,
            null,
            null,
            BuildingIndexSingleton.getInstance(assets),
            floorPlans
        )

        val string = intent.getSerializableExtra("RoutePreview")!! as String
        routePreviewData = Gson().fromJson(
            string,
            RoutePreviewData::class.java
        )
        steps = routePreviewData.getSteps()
        stepPath = routePreviewData.getPath()

        previousStepButton = findViewById(R.id.previousStep)
        nextStepButton = findViewById(R.id.nextStep)
        var stepInstruction = findViewById<TextView>(R.id.stepInstruction)

        stepInstruction.text = parseHTMLString(steps[0].htmlInstruction)
        previousStepButton.isEnabled = false
        stepInstruction.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_turn_right, 0, 0, 0)

        initializer.setOnMapReadyListener {
            val path =
                PathPolyline(routePreviewData.getStart(), routePreviewData.getEnd(), routePreviewData.getPath())
            setPathOnMapAsync(path)
            setCurrentStep(0)
            focusCameraOnCurrentStep(PathPolyline("", "", currentStepPath), 0)
        }

        previousStepButton.setOnClickListener {
            previousStep(stepInstruction)
        }

        nextStepButton.setOnClickListener {
            nextStep(stepInstruction)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun previousStep(textView: TextView) {
        i--
        textView.text = parseHTMLString(steps[i].htmlInstruction)
        setIcon(textView, i)
        setCurrentStep(i)
        focusCameraOnCurrentStep(
            PathPolyline("", "", currentStepPath),
            i
        )
        if (i == 0)
            previousStepButton.isEnabled = false
        else
            nextStepButton.isEnabled = true
    }

    private fun nextStep(textView: TextView) {
        i++
        textView.text = parseHTMLString(steps[i].htmlInstruction)
        setIcon(textView, i)
        setCurrentStep(i)
        focusCameraOnCurrentStep(
            PathPolyline("", "", currentStepPath),
            i
        )
        if (i == steps.size - 1) {
            nextStepButton.isEnabled = false
        } else {
            previousStepButton.isEnabled = true
        }
    }

    private fun setPathOnMapAsync(path: PathPolyline) {
        GlobalScope.launch {
            path.waitUntilCreated()
            runOnUiThread {
                map.addPath(path, floorPlans.getCurrentFloor())
            }
        }
    }

    private fun focusCameraOnCurrentStep(path: PathPolyline, step: Int) {
        GlobalScope.launch {
            path.waitUntilCreated()
            runOnUiThread {
                map.moveCamera(path.getPathBounds())
            }
        }
    }

    private fun parseHTMLString(string: String): String {
        return Jsoup.parse(string).text()
    }

    private fun setCurrentStep(i: Int) {
        currentStepPath.clear()
        var startLatLng = LatLng(
            steps[i].startLocation.lat.toDouble(),
            steps[i].startLocation.lng.toDouble()
        )
        var endLatLng = LatLng(
            steps[i].endLocation.lat.toDouble(),
            steps[i].endLocation.lng.toDouble()
        )
        currentStepPath.add(Path(mutableListOf(startLatLng)))
        currentStepPath.add(Path(mutableListOf(endLatLng)))
    }

    private fun setIcon(instruction: TextView, step: Int) {
        val drawable: Int =
            when (steps[step].maneuver) {
                "turn-right" -> R.drawable.ic_turn_right
                "turn-left" -> R.drawable.ic_turn_left
                "ramp-right" -> R.drawable.ic_turn_right
                "ramp-left" -> R.drawable.ic_turn_left
                else -> R.drawable.ic_straight
            }
        instruction.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
    }
}
