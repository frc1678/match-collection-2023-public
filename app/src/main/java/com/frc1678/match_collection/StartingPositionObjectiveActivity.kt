package com.frc1678.match_collection

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
    private fun setMapImage() {
        when {
            (orientation && alliance_color == Constants.AllianceColor.BLUE) ->
                iv_starting_position_map.setImageResource(R.drawable.blue_map_1)

            (!orientation && alliance_color == Constants.AllianceColor.BLUE) ->
                iv_starting_position_map.setImageResource(R.drawable.blue_map_2)

            (orientation && alliance_color == Constants.AllianceColor.RED) ->
                iv_starting_position_map.setImageResource(R.drawable.red_map_1)

            (!orientation && alliance_color == Constants.AllianceColor.RED) ->
                iv_starting_position_map.setImageResource(R.drawable.red_map_2)
        }
    }

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
        when (starting_position) {
            Constants.StartingPosition.ZERO -> btn_zero.setBackgroundColor(Color.YELLOW)
            Constants.StartingPosition.ONE -> btn_one.setBackgroundColor(Color.YELLOW)
            Constants.StartingPosition.TWO -> btn_two.setBackgroundColor(Color.YELLOW)
            Constants.StartingPosition.THREE -> btn_three.setBackgroundColor(Color.YELLOW)
            Constants.StartingPosition.FOUR -> btn_four.setBackgroundColor(Color.YELLOW)
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
                // If you did not select a starting position the team is assumed to be a no show.
                // This will allow you to skip the information collection screen
                if (starting_position == Constants.StartingPosition.ZERO) {
                    intent = Intent(this, MatchInformationEditActivity::class.java)
                } else {
                    intent = Intent(this, CollectionObjectiveActivity::class.java)
                    if (comingBack == "collection objective activity") {
                        comingBack = "Starting position activity"
                    }
                }
                startActivity(
                    intent,
                    ActivityOptions.makeSceneTransitionAnimation(
                        this,
                        btn_proceed_starting_position, "proceed_button"
                    ).toBundle()
                )
            } else {
                createErrorMessage(
                    message = getString(R.string.error_missing_information),
                    view = view
                )
            }
        }
    }

    /**
     * Initializes the spinner for selecting the preloaded game object.
     */
    private fun initSpinner() {
        spinner_preloaded.adapter = ArrayAdapter(
            this, R.layout.spinner_text_view, Constants.Preloaded.values().map { it.name }
        )
        spinner_preloaded.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                preloaded = Constants.Preloaded.values()[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    /**
     * Begin intent used in onKeyLongPress to restart app from MatchInformationInputActivity.
     */
    private fun intentToPreviousActivity() = startActivity(
        Intent(this, MatchInformationInputActivity::class.java),
        ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
    )

    /**
     * Restart app from MatchInformationInputActivity.kt when back button is long pressed.
     */
    override fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder(this).setMessage(R.string.error_back_reset)
                .setPositiveButton("Yes") { _, _ -> intentToPreviousActivity() }
                .show()
        }
        return super.onKeyLongPress(keyCode, event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.starting_position_activity)

        tv_pos_team_number.setTextColor(
            resources.getColor(
                if (alliance_color == Constants.AllianceColor.RED) R.color.alliance_red_light
                else R.color.alliance_blue_light,
                null
            )
        )

        tv_pos_team_number.text = team_number

        resetCollectionReferences()

        setMapImage()
        setBackgrounds()
        initOnClicks()
        initSpinner()
    }
}
