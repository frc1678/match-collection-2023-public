/*
* MatchInformationInputActivity.kt
* match-collection
*
* Created on 1/2/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.match_information_input_activity.*

/* Class used to input the information of the match before the match's beginning.
* Information such as: teams in alliances, match number, and other.
 */
class MatchInformationInputActivity : AppCompatActivity() {

    var collectionMode: Constants.MODE_SELECTION = Constants.MODE_SELECTION.NONE
    private lateinit var allianceColorToggle: ColoredToggleButtonElement

    //Create the onclick listener for the proceed button.
    private fun initProceedButton() {
        btn_proceed_match_start.setOnClickListener { view ->
            if (getSerialNum(this) != null) {
                if ((checkInputNotEmpty(et_scout_name, et_match_number)
                            && (alliance_color != Constants.ALLIANCE_COLOR.NONE))) {

                    //Reassigning variables in References.kt to inputed text.
                    scout_name = et_scout_name.text.toString()
                    match_number = Integer.parseInt(et_match_number.text.toString())

                    //Switch statement to separate subjective and objective input safety.
                    when (collectionMode) {
                        //Check to make sure all objective related inputs are not empty.
                        Constants.MODE_SELECTION.OBJECTIVE -> if (checkInputNotEmpty(et_team_one)) {
                            //todo start match for objective
                            team_number = Integer.parseInt(et_team_one.text.toString())
                            startMatchActivity()
                        } else {
                            createErrorMessage("Please input the team number!", view)
                        }

                        //Check to make sure all subjective related inputs are not empty.
                        Constants.MODE_SELECTION.SUBJECTIVE -> if (checkInputNotEmpty(et_team_one, et_team_two, et_team_three)) {
                            //todo start match for subjective
                            startMatchActivity()
                        } else {
                            createErrorMessage("Please input the team numbers!", view)
                        }
                        else -> {
                            //If collectionMode is NONE: exit out of the whole overall setOnClickListener function.
                            return@setOnClickListener
                        }
                    }
                } else {
                    createErrorMessage("Please input the scout name, alliance color, and match number!", view)
                }
            }
        }
    }

    //Create a snackbar error message with the given text. If no View is given, use 'this' as its value.
    private fun createErrorMessage(text: String, view: View) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show()
    }

    //Check if the given text inputs are not empty.
    private fun checkInputNotEmpty(vararg views: EditText): Boolean {
        for (view in views) {
            if (view.text.isEmpty()) return false
        }
        return true
    }

    //Create the ColoredToggleButtonElement given its parameters.
    private fun initAllianceColorToggle() {
        allianceColorToggle = ColoredToggleButtonElement(
            leftToggleButton = leftToggleButton,
            rightToggleButton = rightToggleButton,
            leftToggleButtonColor = ContextCompat.getColor(this, R.color.light_blue),
            rightToggleButtonColor = ContextCompat.getColor(this, R.color.light_red),
            leftToggleButtonColorDark = ContextCompat.getColor(this, R.color.dark_blue),
            rightToggleButtonColorDark = ContextCompat.getColor(this, R.color.dark_red)
        )
    }

    //Used to transition into the next activity.
    //Calls MatchTimerThread from TimerUtility.kt and starts the timer
    private fun startMatchActivity() {
        if (collectionMode.equals(Constants.MODE_SELECTION.OBJECTIVE)) {
            startActivity(Intent(this, ObjectiveMatchCollectionActivity::class.java))
        }
        TimerUtility.MatchTimerThread().initTimer()
    }

    //Checks if collection mode is subjective and if true, makes other team inputs visible
    private fun checkCollectionMode() {
        if (collectionMode == Constants.MODE_SELECTION.SUBJECTIVE) {
            makeViewVisible(et_team_two, et_team_three, view_team_view_separator_one, view_team_view_separator_two)
        }
    }

    //Allows for inputs to become visible
    private fun makeViewVisible(vararg views: View) {
        for (view in views) {
            view.visibility = View.VISIBLE
        }
    }

    //Makes inputs and buttons have animations
    private fun makeViewAnimation(vararg views: View, animation: Int) {
        for (view in views) {
            view.startAnimation(AnimationUtils.loadAnimation(this, animation))
        }
    }

    //Calls for the animations, and adds animations for the subjective specific objects.
    private fun initViewAnimations() {
        makeViewAnimation(et_team_one, et_match_number, et_scout_name, leftToggleButton, rightToggleButton, btn_proceed_match_start,
            view_alliance_color_separator, view_proceed_button_separator, view_scout_name_match_number_separator,
            view_team_view_separator_three, animation = R.anim.fade_in)
        if (collectionMode == Constants.MODE_SELECTION.SUBJECTIVE)
            makeViewAnimation(et_team_two, et_team_three, view_team_view_separator_one, view_team_view_separator_two,
                animation = R.anim.fade_in)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.match_information_input_activity)
        collectionMode = intent!!.extras!!.getSerializable("collection_mode") as Constants.MODE_SELECTION

        checkCollectionMode()

        initViewAnimations()
        initAllianceColorToggle()
        initProceedButton()
    }
}