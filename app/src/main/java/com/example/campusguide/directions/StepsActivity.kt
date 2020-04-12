package com.example.campusguide.directions

import android.content.Context
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

class StepsActivity : AppCompatActivity(){
    private lateinit var path: Test
    private lateinit var gson: Gson
    private lateinit var steps : List<GoogleDirectionsAPIStep>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_steps)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val string = intent.getSerializableExtra("Steps") as String
        gson = Gson()
        path = gson.fromJson(
            string,
            Test::class.java
        )

        steps = path.getSteps()
        val listView = findViewById<ListView>(R.id.stepList)
        listView.adapter = DirectionStepsAdapter(this, steps)

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private class DirectionStepsAdapter(context: Context, steps: List<GoogleDirectionsAPIStep>) : BaseAdapter(){
        private val mContext : Context
        private val direction : List<GoogleDirectionsAPIStep>

        init {
            mContext = context
            direction = steps
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val directionStepsItem = layoutInflater.inflate(R.layout.direction_steps_item, parent, false)

            val instruction = directionStepsItem.findViewById<TextView>(R.id.instructions)
            instruction.text = direction[position].htmlInstruction

            if(direction[position].maneuver != "x")
                instruction.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_directions, 0, 0, 0)

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
    }
}