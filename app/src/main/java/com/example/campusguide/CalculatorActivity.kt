package com.example.campusguide

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.utils.Calculator

import kotlinx.android.synthetic.main.activity_calculator.*
import kotlinx.android.synthetic.main.content_calculator.*

class CalculatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)
        setSupportActionBar(toolbar)

        calculateButton.setOnClickListener(::onCalculate)
    }

    fun onCalculate(view: View) {
        var first = firstNumber.text.toString()
        var second = secondNumber.text.toString()
        if (first.isEmpty() || second.isEmpty()) {
            resultTextView.text = "Please enter two numbers."
        } else {
            var result = Calculator().add(first.toInt(), second.toInt())
            resultTextView.text = result.toString()
        }
    }

}
