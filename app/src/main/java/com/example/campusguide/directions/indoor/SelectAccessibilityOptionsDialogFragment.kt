package com.example.campusguide.directions.indoor

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.fragment.app.DialogFragment
import com.example.campusguide.Accessibility
import com.example.campusguide.Constants
import com.example.campusguide.DirectionsActivity
import com.example.campusguide.R

/**
 * Fragment that contains a dialog window prompting the user to
 * select the accessibility methods they would like to use for
 * indoor pathfinding.
 */
class SelectAccessibilityOptionsDialogFragment(
    private val directionsActivity: DirectionsActivity,
    private val callback: () -> Unit
) : DialogFragment() {
    private lateinit var myView: View
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.accessibility_select_in_directions, null)

        // set title of dialog box
        val builder: AlertDialog.Builder = AlertDialog.Builder(directionsActivity)
            .setTitle(Constants.SELECT_ONE_MORE_OPTIONS)

        builder.setView(view)
        myView = view
        builder.create().window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        builder.setPositiveButton(Constants.CONFIRM_CHOICE) { _, _ ->
            handleOKSelected()
        }.setNegativeButton(Constants.CANCEL_CHOICE, null)

        return builder.create()
    }

    private fun handleOKSelected() {
        handleEscalator()
        handleElevator()
        handleStairs()
        callback.invoke()
    }

    private fun boxIsChecked(box: CheckBox?): Boolean {
        if (box != null) {
            if (box.isChecked) {
                return true
            }
        }
        return false
    }

    /**
     * Methods for each different checkbox that needs handling.
     * Steps:
     * 1. Check if checkbox is selected
     * 2. If so - use Accessibility object to add node type to forbiddenRooms list
     */
    private fun handleEscalator() {
        val escalatorCheckBox =
            myView.findViewById<CheckBox>(R.id.escalators_checkbox)
        if (boxIsChecked(escalatorCheckBox))
            Accessibility.setEscalators(false)
        else
            Accessibility.setEscalators(true)
    }

    private fun handleElevator() {
        val elevatorCheckBox =
            myView.findViewById<CheckBox>(R.id.elevators_checkbox)
        if (boxIsChecked(elevatorCheckBox))
            Accessibility.setElevators(false)
        else
            Accessibility.setElevators(true)
    }

    private fun handleStairs() {
        val stairsCheckBox =
            myView.findViewById<CheckBox>(R.id.stairs_checkbox)
        if (boxIsChecked(stairsCheckBox))
            Accessibility.setStairs(false)
        else
            Accessibility.setStairs(true)
    }
}
