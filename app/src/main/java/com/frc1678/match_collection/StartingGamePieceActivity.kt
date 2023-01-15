package com.frc1678.match_collection

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.core.content.ContextCompat.getColor
import kotlinx.android.synthetic.main.starting_game_pieces_activity.*
import kotlinx.android.synthetic.main.starting_position_activity.*

class StartingGamePieceActivity : CollectionActivity() {

    // Chooses which map you will see depending on your alliance color and orientation
    private fun setMapPicture() {
        when {
            (orientation && alliance_color == Constants.AllianceColor.BLUE) ->
                iv_starting_game_pieces_map.setImageResource(R.drawable.blue_up_game_pieces)
            (!orientation && alliance_color == Constants.AllianceColor.BLUE) ->
                iv_starting_game_pieces_map.setImageResource(R.drawable.blue_down_game_pieces)
            (orientation && alliance_color == Constants.AllianceColor.RED) ->
                iv_starting_game_pieces_map.setImageResource(R.drawable.red_up_game_pieces)
            (!orientation && alliance_color == Constants.AllianceColor.RED) ->
                iv_starting_game_pieces_map.setImageResource(R.drawable.red_down_game_pieces)
        }
    }

    // Initiates the onClicks for all the buttons
    private fun initOnClicks() {
        // When clicked, changes game_piece_one to either cone or cube depending on whatever one it was
        // then, calls setBackgrounds
        btn_game_piece_one.setOnClickListener {
            game_piece_one = when (game_piece_one) {
                Constants.GamePieceOne.CONE -> {
                    Constants.GamePieceOne.CUBE
                }
                Constants.GamePieceOne.CUBE -> {
                    Constants.GamePieceOne.CONE
                }
                Constants.GamePieceOne.NONE -> {
                    Constants.GamePieceOne.CONE
                }
            }
            setBackgrounds()
        }

        // When clicked, changes game_piece_two to either cone or cube depending on whatever one it was
        // then, calls setBackgrounds
        btn_game_piece_two.setOnClickListener {
            game_piece_two = when (game_piece_two) {
                Constants.GamePieceTwo.CONE -> {
                    Constants.GamePieceTwo.CUBE
                }
                Constants.GamePieceTwo.CUBE -> {
                    Constants.GamePieceTwo.CONE
                }
                Constants.GamePieceTwo.NONE -> {
                    Constants.GamePieceTwo.CONE
                }
            }
            setBackgrounds()
        }

        // When clicked, changes game_piece_three to either cone or cube depending on whatever one it was
        // then, calls setBackgrounds
        btn_game_piece_three.setOnClickListener {
            game_piece_three = when (game_piece_three) {
                Constants.GamePieceThree.CONE -> {
                    Constants.GamePieceThree.CUBE
                }
                Constants.GamePieceThree.CUBE -> {
                    Constants.GamePieceThree.CONE
                }
                Constants.GamePieceThree.NONE -> {
                    Constants.GamePieceThree.CONE
                }
            }
            setBackgrounds()
        }

        // When clicked, changes game_piece_four to either cone or cube depending on whatever one it was
        // then, calls setBackgrounds
        btn_game_piece_four.setOnClickListener {
            game_piece_four = when (game_piece_four) {
                Constants.GamePieceFour.CONE -> {
                    Constants.GamePieceFour.CUBE
                }
                Constants.GamePieceFour.CUBE -> {
                    Constants.GamePieceFour.CONE
                }
                Constants.GamePieceFour.NONE -> {
                    Constants.GamePieceFour.CONE
                }
            }
            setBackgrounds()
        }

        // Changes the orientation of the map and calls setMapPicture
        btn_switch_orientation_game_pieces.setOnClickListener{
            orientation = !orientation
            setMapPicture()
        }

        // Moves onto the next screen if you have inputted all the information
        btn_proceed_game_piece.setOnClickListener { view ->
            if (CollectionObjectiveActivity.comingBack == "collection subjective activity") {
                CollectionObjectiveActivity.comingBack = "Starting position game piece activity"
            }
            // If all game pieces have been selected then proceed to CollectionSubjectiveActivity.kt
            // Otherwise create warning message
            if((game_piece_one == Constants.GamePieceOne.NONE) or (game_piece_two == Constants.GamePieceTwo.NONE) or
                (game_piece_three == Constants.GamePieceThree.NONE) or (game_piece_four == Constants.GamePieceFour.NONE)){
                AlertDialog.Builder(this).setTitle("Warning! You have not selected for all game pieces!")
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.cancel()
                    }.setPositiveButton("Proceed") { _: DialogInterface, _: Int ->
                        goToNextActivity()
                    }.show()
            } else {
                // Add alliance teams to the intent to be used in MatchInformationEditActivity.kt.
                goToNextActivity()
            }
        }
    }

    // Goes to next activity
    private fun goToNextActivity() {
        startActivity(
            Intent(this, CollectionSubjectiveActivity::class.java).putExtras(intent),
            ActivityOptions.makeSceneTransitionAnimation(
                this,
                btn_proceed_game_piece, "proceed_button"
            ).toBundle()
        )
    }

    // Sets the backgrounds of gamePieceOne, gamePieceTwo, gamePieceThree, gamePieceFour
    private fun setBackgrounds() {
        // Sets backgroundColor of gamePieceOne to the corresponding game piece color
        // or if not selected, then sets background to grey
        when (game_piece_one) {
            Constants.GamePieceOne.NONE -> {
                btn_game_piece_one.setBackgroundColor(resources.getColor(R.color.light_gray))
            }
            Constants.GamePieceOne.CONE -> {
                btn_game_piece_one.setBackgroundColor(resources.getColor(R.color.action_yellow))
            }
            Constants.GamePieceOne.CUBE -> {
                btn_game_piece_one.setBackgroundColor(resources.getColor(R.color.undo_purple_pressed))
            }
        }
        // Sets backGroundColor of gamePieceTwo to the corresponding game piece color
        // or if not selected, then sets background to grey
        when (game_piece_two) {
            Constants.GamePieceTwo.NONE -> {
                btn_game_piece_two.setBackgroundColor(resources.getColor(R.color.light_gray))
            }
            Constants.GamePieceTwo.CONE -> {
                btn_game_piece_two.setBackgroundColor(resources.getColor(R.color.action_yellow))
            }
            Constants.GamePieceTwo.CUBE -> {
                btn_game_piece_two.setBackgroundColor(resources.getColor(R.color.undo_purple_pressed))
            }
        }
        // Sets backGroundColor of gamePieceThree to the corresponding game piece color
        // or if not selected, then sets background to grey
        when (game_piece_three) {
            Constants.GamePieceThree.NONE -> {
                btn_game_piece_three.setBackgroundColor(resources.getColor(R.color.light_gray))
            }
            Constants.GamePieceThree.CONE -> {
                btn_game_piece_three.setBackgroundColor(resources.getColor(R.color.action_yellow))
            }
            Constants.GamePieceThree.CUBE -> {
                btn_game_piece_three.setBackgroundColor(resources.getColor(R.color.undo_purple_pressed))
            }

        }
        // Sets backGroundColor of gamePieceFour to the corresponding game piece color
        // or if not, then sets background to grey
        when (game_piece_four) {
            Constants.GamePieceFour.NONE -> {
                btn_game_piece_four.setBackgroundColor(resources.getColor(R.color.light_gray))
            }
            Constants.GamePieceFour.CONE -> {
                btn_game_piece_four.setBackgroundColor(resources.getColor(R.color.action_yellow))
            }
            Constants.GamePieceFour.CUBE -> {
                btn_game_piece_four.setBackgroundColor(resources.getColor(R.color.undo_purple_pressed))
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

    // Creates the screen and sets everything up.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.starting_game_pieces_activity)

        setMapPicture()
        initOnClicks()
        setBackgrounds()
    }

}