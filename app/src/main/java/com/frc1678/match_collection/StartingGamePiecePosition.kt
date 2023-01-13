package com.frc1678.match_collection

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import kotlinx.android.synthetic.main.starting_game_pieces_activity.*
import kotlinx.android.synthetic.main.starting_position_activity.*

class StartingGamePieceActivity : CollectionActivity() {

    // Chooses which map you will see depending on your alliance color
    private fun setMapPicture() {
        when {
            (alliance_color == Constants.AllianceColor.BLUE) ->
                iv_starting_game_pieces_map.setImageResource(R.drawable.)
            (alliance_color == Constants.AllianceColor.RED) ->
                iv_starting_game_pieces_map.setImageResource(R.drawable.)
        }
    }

    private fun initOnClicks() {
        btn_game_piece_one.setOnClickListener {
            game_piece_one = when (game_piece_one) {
                Constants.GamePieceOne.CONE -> {
                    btn_game_piece_one.setBackgroundColor(0x8F00FF)
                    Constants.GamePieceOne.CUBE
                }
                Constants.GamePieceOne.CUBE -> {
                    btn_game_piece_one.setBackgroundColor(0xFFFF00)
                    Constants.GamePieceOne.CONE
                }
                Constants.GamePieceOne.NONE -> {
                    btn_game_piece_one.setBackgroundColor(0xFFFF00)
                    Constants.GamePieceOne.CONE
                }
            }
        }

        btn_game_piece_two.setOnClickListener {
            game_piece_two = when (game_piece_two) {
                Constants.GamePieceTwo.CONE -> {
                    btn_game_piece_two.setBackgroundColor(0x8F00FF)
                    Constants.GamePieceTwo.CUBE
                }
                Constants.GamePieceTwo.CUBE -> {
                    btn_game_piece_two.setBackgroundColor(0xFFFF00)
                    Constants.GamePieceTwo.CONE
                }
                Constants.GamePieceTwo.NONE -> {
                    btn_game_piece_two.setBackgroundColor(0xFFFF00)
                    Constants.GamePieceTwo.CONE
                }
            }
        }

        btn_game_piece_three.setOnClickListener {
            game_piece_three = when (game_piece_three) {
                Constants.GamePieceThree.CONE -> {
                    btn_game_piece_three.setBackgroundColor(0x8F00FF)
                    Constants.GamePieceThree.CUBE
                }
                Constants.GamePieceThree.CUBE -> {
                    btn_game_piece_three.setBackgroundColor(0xFFFF00)
                    Constants.GamePieceThree.CONE
                }
                Constants.GamePieceThree.NONE -> {
                    btn_game_piece_three.setBackgroundColor(0xFFFF00)
                    Constants.GamePieceThree.CONE
                }
            }
        }

        btn_game_piece_four.setOnClickListener {
            game_piece_four = when (game_piece_four) {
                Constants.GamePieceFour.CONE -> {
                    btn_game_piece_four.setBackgroundColor(0x8F00FF)
                    Constants.GamePieceFour.CUBE
                }
                Constants.GamePieceFour.CUBE -> {
                    btn_game_piece_four.setBackgroundColor(0xFFFF00)
                    Constants.GamePieceFour.CONE
                }
                Constants.GamePieceFour.NONE -> {
                    btn_game_piece_four.setBackgroundColor(0xFFFF00)
                    Constants.GamePieceFour.CONE
                }
            }
        }
        // Moves onto the next screen if you have inputted all the information
        btn_proceed_starting_position.setOnClickListener { view ->
            intent = Intent(this, CollectionSubjectiveActivity::class.java)
            if (CollectionObjectiveActivity.comingBack == "collection subjective activity") {
                CollectionObjectiveActivity.comingBack = "Starting position game piece activity"
            }
            startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    btn_proceed_starting_position, "proceed_button"
                ).toBundle()
            )
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
        setContentView(R.layout.starting_game_pieces_activity)

        setMapPicture()
        initOnClicks()
    }

}