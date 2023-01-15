package com.frc1678.match_collection

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.frc1678.match_collection.CollectionObjectiveActivity.Companion.comingBack
import kotlinx.android.synthetic.main.starting_position_activity.btn_four
import kotlinx.android.synthetic.main.starting_position_activity.btn_one
import kotlinx.android.synthetic.main.starting_position_activity.btn_proceed_starting_position
import kotlinx.android.synthetic.main.starting_position_activity.btn_switch_orientation
import kotlinx.android.synthetic.main.starting_position_activity.btn_three
import kotlinx.android.synthetic.main.starting_position_activity.btn_two
import kotlinx.android.synthetic.main.starting_position_activity.btn_zero
import kotlinx.android.synthetic.main.starting_position_activity.iv_starting_position_map
import kotlinx.android.synthetic.main.starting_position_activity.spinner_preloaded
import kotlinx.android.synthetic.main.starting_position_activity.tv_pos_team_number

class StartingPositionObjectiveActivity : CollectionActivity() {

    /**
     * Chooses which map you will see depending on your alliance color and orientation.
     */
    private fun setMapImage() = iv_starting_position_map.setImageResource(
        when {
            (orientation && alliance_color == Constants.AllianceColor.BLUE) -> R.drawable.blue_map_1
            (!orientation && alliance_color == Constants.AllianceColor.BLUE) -> R.drawable.blue_map_2
            (orientation && alliance_color == Constants.AllianceColor.RED) -> R.drawable.red_map_1
            (!orientation && alliance_color == Constants.AllianceColor.RED) -> R.drawable.red_map_2
            else -> error("Error setting map image")
        }
    )

    /**
     * Sets the colors of the buttons depending on the alliance color.
     */
    private fun setBackgrounds() {
        if (alliance_color == Constants.AllianceColor.RED) {
            btn_zero.setBackgroundColor(resources.getColor(R.color.light_gray, null))
            btn_one.setBackgroundColor(resources.getColor(R.color.red_start_one, null))
            btn_two.setBackgroundColor(resources.getColor(R.color.red_start_two, null))
            btn_three.setBackgroundColor(resources.getColor(R.color.red_start_three, null))
            btn_four.setBackgroundColor(resources.getColor(R.color.red_start_four, null))
        } else {
            btn_zero.setBackgroundColor(resources.getColor(R.color.light_gray, null))
            btn_one.setBackgroundColor(resources.getColor(R.color.blue_start_one, null))
            btn_two.setBackgroundColor(resources.getColor(R.color.blue_start_two, null))
            btn_three.setBackgroundColor(resources.getColor(R.color.blue_start_three, null))
            btn_four.setBackgroundColor(resources.getColor(R.color.blue_start_four, null))
        }

        // Changes the color of the button if that starting position is selected
        val selectedColor = resources.getColor(R.color.selected_start, null)
        when (starting_position) {
            Constants.StartingPosition.ZERO -> btn_zero.setBackgroundColor(selectedColor)
            Constants.StartingPosition.ONE -> btn_one.setBackgroundColor(selectedColor)
            Constants.StartingPosition.TWO -> btn_two.setBackgroundColor(selectedColor)
            Constants.StartingPosition.THREE -> btn_three.setBackgroundColor(selectedColor)
            Constants.StartingPosition.FOUR -> btn_four.setBackgroundColor(selectedColor)
            else -> {}
        }
    }

    private fun initOnClicks() {
        btn_zero.setOnClickListener {
            starting_position = Constants.StartingPosition.ZERO
            setBackgrounds()
        }
        btn_one.setOnClickListener {
            starting_position = Constants.StartingPosition.ONE
            setBackgrounds()
        }
        btn_two.setOnClickListener {
            starting_position = Constants.StartingPosition.TWO
            setBackgrounds()
        }
        btn_three.setOnClickListener {
            starting_position = Constants.StartingPosition.THREE
            setBackgrounds()
        }
        btn_four.setOnClickListener {
            starting_position = Constants.StartingPosition.FOUR
            setBackgrounds()
        }
        btn_switch_orientation.setOnClickListener {
            orientation = !orientation
            setMapImage()
        }
        // Moves onto the next screen if you have inputted all the information
        btn_proceed_starting_position.setOnClickListener { view ->
            if (starting_position != Constants.StartingPosition.NONE) {
                // If you did not select a starting position, the team is assumed to be a no-show.
                // This will allow you to skip the collection activity.
                if (starting_position == Constants.StartingPosition.ZERO) {
                    intent = Intent(this, MatchInformationEditActivity::class.java)
                } else {
                    intent = Intent(this, CollectionObjectiveActivity::class.java)
                    if (comingBack == "collection objective activity") {
                        comingBack = "Starting position activity"
                    }
                }
                startActivity(
                    intent, ActivityOptions.makeSceneTransitionAnimation(
                        this, btn_proceed_starting_position, "proceed_button"
                    ).toBundle()
                )
            } else createErrorMessage(getString(R.string.error_missing_information), view)
        }
    }

    /**
     * Initializes the spinner for selecting the preloaded game object.
     */
    private fun initSpinner() {
        // Set the adapter for the spinner
        spinner_preloaded.adapter = object : ArrayAdapter<String>(
            this, R.layout.spinner_preloaded, Constants.Preloaded.values().map { it.name }
        ) {
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
                super.getDropDownView(position, convertView, parent).apply {
                    // Set the background color of the spinner item depending on its value
                    setBackgroundColor(
                        resources.getColor(
                            when ((this as TextView).text) {
                                Constants.Preloaded.CONE.toString() -> R.color.cone_yellow
                                Constants.Preloaded.CUBE.toString() -> R.color.cube_purple
                                else -> R.color.light_gray
                            }, null
                        )
                    )
                }
        }
        spinner_preloaded.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                // Update the preloaded item when a spinner item is selected
                preloaded = Constants.Preloaded.values()[position]
                // Set the background color of the spinner based on its value
                spinner_preloaded.setBackgroundColor(
                    when (preloaded) {
                        Constants.Preloaded.NONE -> resources.getColor(R.color.light_gray, null)
                        Constants.Preloaded.CONE -> resources.getColor(R.color.cone_yellow, null)
                        Constants.Preloaded.CUBE -> resources.getColor(R.color.cube_purple, null)
                    }
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    /**
     * Show dialog to restart from [MatchInformationInputActivity] when back button is long pressed.
     */
    override fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) AlertDialog.Builder(this)
            .setMessage(R.string.error_back_reset)
            .setPositiveButton("Yes") { _, _ ->
                startActivity(
                    Intent(this, MatchInformationInputActivity::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                )
            }
            .show()
        return super.onKeyLongPress(keyCode, event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.starting_position_activity)

        // Initialize the team number text
        tv_pos_team_number.text = team_number
        tv_pos_team_number.setTextColor(
            resources.getColor(
                if (alliance_color == Constants.AllianceColor.RED) R.color.alliance_red_light
                else R.color.alliance_blue_light,
                null
            )
        )

        resetCollectionReferences()

        setMapImage()
        setBackgrounds()
        initOnClicks()
        initSpinner()
    }
}
