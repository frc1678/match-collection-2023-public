package com.frc1678.match_collection

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import kotlinx.android.synthetic.main.starting_position_activity.*

class StartingPositionObjectiveActivity:CollectionActivity() {

    private fun setMapImage() {
        when {
            (orientation && alliance_color == Constants.AllianceColor.BLUE) ->
                iv_starting_position_map.setImageResource(R.drawable.blue_up_start)
            (!orientation && alliance_color == Constants.AllianceColor.BLUE) ->
                iv_starting_position_map.setImageResource(R.drawable.blue_down_start)
            (orientation && alliance_color == Constants.AllianceColor.RED) ->
                iv_starting_position_map.setImageResource(R.drawable.red_up_start)
            (!orientation && alliance_color == Constants.AllianceColor.RED) ->
                iv_starting_position_map.setImageResource(R.drawable.red_down_start)
        }
    }

    private fun setBackgrounds() {
        if (alliance_color == Constants.AllianceColor.RED) {
            btn_one.setBackgroundColor(resources.getColor(R.color.red_start_one))
            btn_two.setBackgroundColor(resources.getColor(R.color.red_start_two))
            btn_three.setBackgroundColor(resources.getColor(R.color.red_start_three))
            btn_four.setBackgroundColor(resources.getColor(R.color.red_start_four))
        } else {
            btn_one.setBackgroundColor(resources.getColor(R.color.blue_start_one))
            btn_two.setBackgroundColor(resources.getColor(R.color.blue_start_two))
            btn_three.setBackgroundColor(resources.getColor(R.color.blue_start_three))
            btn_four.setBackgroundColor(resources.getColor(R.color.blue_start_four))
        }
        when {
            (starting_position == Constants.StartingPosition.ONE) -> btn_one.setBackgroundColor(Color.YELLOW)
            (starting_position == Constants.StartingPosition.TWO) -> btn_two.setBackgroundColor(Color.YELLOW)
            (starting_position == Constants.StartingPosition.THREE) -> btn_three.setBackgroundColor(Color.YELLOW)
            (starting_position == Constants.StartingPosition.FOUR) -> btn_four.setBackgroundColor(Color.YELLOW)
        }
    }

    private fun initOnClicks() {
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
            //true = UP, false = DOWN
            orientation = !orientation
            setMapImage()
        }
        btn_proceed_starting_position.setOnClickListener { view ->
            val intent = Intent(this, CollectionObjectiveActivity::class.java)
            if (starting_position != Constants.StartingPosition.NONE) {
                startActivity(
                    intent, ActivityOptions.makeSceneTransitionAnimation(
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

    // Begin intent used in onKeyLongPress to restart app from MatchInformationInputActivity.kt.
    private fun intentToPreviousActivity() {
        startActivity(
            Intent(this, MatchInformationInputActivity::class.java),
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    // Restart app from MatchInformationInputActivity.kt when back button is long pressed.
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

        resetCollectionReferences()

        setMapImage()
        setBackgrounds()
        initOnClicks()
    }

}