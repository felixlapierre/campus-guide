package com.example.campusguide.search

import android.os.Bundle
import android.view.WindowManager
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.R

class CustomAutocompleteActivity : AppCompatActivity() {
    private lateinit var textInput: AutoCompleteTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autocomplete);
        textInput = findViewById(R.id.search_input)
        if(textInput.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}