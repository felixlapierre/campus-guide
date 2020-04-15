package com.example.campusguide.directions

import android.os.Bundle
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.R
import com.example.campusguide.map.GoogleMapAdapter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RoutePreviewActivity : AppCompatActivity() {
    private lateinit var map: GoogleMapAdapter
    private lateinit var pathPolyline: Test
    private lateinit var steps : List<GoogleDirectionsAPIStep>
    private lateinit var stepPath : List<LatLng>
    private lateinit var previousStepButton : ToggleButton
    private lateinit var nextStepButton : ToggleButton
    private var i : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_preview)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        map = GoogleMapAdapter()

        val string = intent.getSerializableExtra("RoutePreview")!! as String
        pathPolyline = Gson().fromJson(
            string,
            Test::class.java
        )
        steps = pathPolyline.getSteps()
        stepPath = pathPolyline.getPath()

        previousStepButton = findViewById(R.id.previousStep)
        nextStepButton = findViewById(R.id.nextStep)
        var stepInstruction = findViewById<TextView>(R.id.stepInstruction)

        stepInstruction.text = steps[0].htmlInstruction
        previousStepButton.isEnabled = false

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

    private fun previousStep(textView: TextView){
        i--
        textView.text = steps[i].htmlInstruction
        if(i==0)
            previousStepButton.isEnabled = false
        else
            nextStepButton.isEnabled = true
    }

    private fun nextStep(textView: TextView){
        i++
        textView.text = steps[i].htmlInstruction
        if(i == steps.size-1){
            nextStepButton.isEnabled = false
        } else {
            previousStepButton.isEnabled = true
        }
    }

    private fun setPathOnMapAsync(path: PathPolyline) {
        GlobalScope.launch {
            path.waitUntilCreated()
            runOnUiThread {
                map.addPath(path)
            }
        }
    }
}