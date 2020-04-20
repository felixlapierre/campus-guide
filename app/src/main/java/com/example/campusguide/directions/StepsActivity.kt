package com.example.campusguide.directions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_steps.*
import org.jsoup.Jsoup

class StepsActivity : AppCompatActivity() {
    private lateinit var path: RoutePreviewData
    private lateinit var gson: Gson
    private var steps: List<GoogleDirectionsAPIStep> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_steps)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val string = intent.getSerializableExtra("Steps") as String
        gson = Gson()
        path = gson.fromJson(
            string,
            RoutePreviewData::class.java
        )

        steps_duration.text = "${path.getDuration()} min"
        steps_distance.text = "(${path.getDistance()})"

        steps = path.getSteps()
        val listView = findViewById<ListView>(R.id.stepList)
        listView.adapter = DirectionStepsAdapter(this, steps)

        start.setOnClickListener {
            val routeIntent = Intent(this, RoutePreviewActivity::class.java)
            routeIntent.putExtra("RoutePreview", string)
            this.startActivity(routeIntent)
        }

        showMap.setOnClickListener {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private class DirectionStepsAdapter(context: Context, steps: List<GoogleDirectionsAPIStep>) : BaseAdapter() {
        private val mContext: Context
        private val direction: List<GoogleDirectionsAPIStep>

        init {
            mContext = context
            direction = steps
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val directionStepsItem = layoutInflater.inflate(R.layout.direction_steps_item, parent, false)

            val instruction = directionStepsItem.findViewById<TextView>(R.id.instructions)
            instruction.text = Jsoup.parse(direction[position].htmlInstruction).text()
            setIcon(instruction, position)

            return directionStepsItem
        }

        override fun getItem(position: Int): Any {
            return "TEST STRING"
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return direction.size
        }

        private fun setIcon(textView: TextView, position: Int) {
            val drawable: Int =
                when (direction[position].maneuver) {
                    "turn-right" -> R.drawable.ic_turn_right
                    "turn-left" -> R.drawable.ic_turn_left
                    "ramp-right" -> R.drawable.ic_turn_right
                    "ramp-left" -> R.drawable.ic_turn_left
                    "shuttle" -> R.drawable.ic_directions_bus
                    else -> R.drawable.ic_straight
                }
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
        }
    }
}
