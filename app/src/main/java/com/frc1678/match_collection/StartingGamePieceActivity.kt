package com.frc1678.match_collection

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import kotlinx.android.synthetic.main.starting_game_pieces_activity.*

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
            gamePiecePositionList[0] = when (gamePiecePositionList[0]) {
                Constants.GamePiecePositions.CONE -> {
                    Constants.GamePiecePositions.CUBE
                }
                Constants.GamePiecePositions.CUBE -> {
                    Constants.GamePiecePositions.CONE
                }
                Constants.GamePiecePositions.NONE -> {
                    Constants.GamePiecePositions.CONE
                }
            }
            setBackgrounds()
        }

        // When clicked, changes game_piece_two to either cone or cube depending on whatever one it was
        // then, calls setBackgrounds
        btn_game_piece_two.setOnClickListener {
            gamePiecePositionList[1] = when (gamePiecePositionList[1]) {
                Constants.GamePiecePositions.CONE -> {
                    Constants.GamePiecePositions.CUBE
                }
                Constants.GamePiecePositions.CUBE -> {
                    Constants.GamePiecePositions.CONE
                }
                Constants.GamePiecePositions.NONE -> {
                    Constants.GamePiecePositions.CONE
                }
            }
            setBackgrounds()
        }

        // When clicked, changes game_piece_three to either cone or cube depending on whatever one it was
        // then, calls setBackgrounds
        btn_game_piece_three.setOnClickListener {
            gamePiecePositionList[2] = when (gamePiecePositionList[2]) {
                Constants.GamePiecePositions.CONE -> {
                    Constants.GamePiecePositions.CUBE
                }
                Constants.GamePiecePositions.CUBE -> {
                    Constants.GamePiecePositions.CONE
                }
                Constants.GamePiecePositions.NONE -> {
                    Constants.GamePiecePositions.CONE
                }
            }
            setBackgrounds()
        }

        // When clicked, changes game_piece_four to either cone or cube depending on whatever one it was
        // then, calls setBackgrounds
        btn_game_piece_four.setOnClickListener {
            gamePiecePositionList[3] = when (gamePiecePositionList[3]) {
                Constants.GamePiecePositions.CONE -> {
                    Constants.GamePiecePositions.CUBE
                }
                Constants.GamePiecePositions.CUBE -> {
                    Constants.GamePiecePositions.CONE
                }
                Constants.GamePiecePositions.NONE -> {
                    Constants.GamePiecePositions.CONE
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
            if((gamePiecePositionList[0] == Constants.GamePiecePositions.NONE) or (gamePiecePositionList[1] == Constants.GamePiecePositions.NONE) or
                (gamePiecePositionList[2] == Constants.GamePiecePositions.NONE) or (gamePiecePositionList[3] == Constants.GamePiecePositions.NONE)){
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
        when (gamePiecePositionList[0]) {
            Constants.GamePiecePositions.NONE -> {
                btn_game_piece_one.setBackgroundColor(resources.getColor(R.color.light_gray))
            }
            Constants.GamePiecePositions.CONE -> {
                btn_game_piece_one.setBackgroundColor(resources.getColor(R.color.cone_yellow))
            }
            Constants.GamePiecePositions.CUBE -> {
                btn_game_piece_one.setBackgroundColor(resources.getColor(R.color.cube_purple))
            }
        }
        // Sets backGroundColor of gamePieceTwo to the corresponding game piece color
        // or if not selected, then sets background to grey
        when (gamePiecePositionList[1]) {
            Constants.GamePiecePositions.NONE -> {
                btn_game_piece_two.setBackgroundColor(resources.getColor(R.color.light_gray))
            }
            Constants.GamePiecePositions.CONE -> {
                btn_game_piece_two.setBackgroundColor(resources.getColor(R.color.cone_yellow))
            }
            Constants.GamePiecePositions.CUBE -> {
                btn_game_piece_two.setBackgroundColor(resources.getColor(R.color.cube_purple))
            }
        }
        // Sets backGroundColor of gamePieceThree to the corresponding game piece color
        // or if not selected, then sets background to grey
        when (gamePiecePositionList[2]) {
            Constants.GamePiecePositions.NONE -> {
                btn_game_piece_three.setBackgroundColor(resources.getColor(R.color.light_gray))
            }
            Constants.GamePiecePositions.CONE -> {
                btn_game_piece_three.setBackgroundColor(resources.getColor(R.color.cone_yellow))
            }
            Constants.GamePiecePositions.CUBE -> {
                btn_game_piece_three.setBackgroundColor(resources.getColor(R.color.cube_purple))
            }

        }
        // Sets backGroundColor of gamePieceFour to the corresponding game piece color
        // or if not, then sets background to grey
        when (gamePiecePositionList[3]) {
            Constants.GamePiecePositions.NONE -> {
                btn_game_piece_four.setBackgroundColor(resources.getColor(R.color.light_gray))
            }
            Constants.GamePiecePositions.CONE -> {
                btn_game_piece_four.setBackgroundColor(resources.getColor(R.color.cone_yellow))
            }
            Constants.GamePiecePositions.CUBE -> {
                btn_game_piece_four.setBackgroundColor(resources.getColor(R.color.cube_purple))
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

private operator fun <R1> (() -> R1).set(i: Int, value: () -> R1) {

}
