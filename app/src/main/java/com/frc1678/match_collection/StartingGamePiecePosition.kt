package com.frc1678.match_collection

import com.frc1678.match_collection.R.color.redo_green
import kotlinx.android.synthetic.main.starting_game_pieces_activity.*

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
        btn_game_piece_one.setOnClickListener{
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
    }

}