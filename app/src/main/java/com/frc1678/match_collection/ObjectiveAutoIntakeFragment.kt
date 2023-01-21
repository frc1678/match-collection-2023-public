package com.frc1678.match_collection

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.collection_objective_auto_intake_fragment.*
import kotlinx.android.synthetic.main.collection_objective_auto_intake_fragment.view.*

class ObjectiveAutoIntakeFragment : Fragment(R.layout.collection_objective_auto_intake_fragment) {

    private var mainView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainView = super.onCreateView(inflater, container, savedInstanceState)!!
        initOnClicks()
        enableButtons(collectionObjectiveActivity.isIncap)
        setBackgrounds()
        return mainView
    }

    private val collectionObjectiveActivity get() = activity as CollectionObjectiveActivity


    private fun initOnClicks() {
        if (mainView != null && activity != null) {
            mainView!!.tb_collection_objective_intake_game_piece_one.setOnClickListener {
                collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.AUTO_INTAKE_GAME_PIECE_ONE)
                autoIntakeGamePieceOne = !autoIntakeGamePieceOne
                if (mainView!!.tb_collection_objective_intake_game_piece_one.isChecked) {
                    collectionObjectiveActivity.scoringScreen = true
                    mainView!!.tb_collection_objective_intake_game_piece_one.setBackgroundColor(Color.WHITE)
                } else if (!mainView!!.tb_collection_objective_intake_game_piece_one.isChecked){
                    mainView!!.tb_collection_objective_intake_game_piece_one.setBackgroundColor(Color.MAGENTA)
                }
            }

            mainView!!.tb_collection_objective_intake_game_piece_two.setOnClickListener {
                collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.AUTO_INTAKE_GAME_PIECE_TWO)
                autoIntakeGamePieceTwo = !autoIntakeGamePieceTwo
                if (mainView!!.tb_collection_objective_intake_game_piece_two.isChecked) {
                    collectionObjectiveActivity.scoringScreen = true
                    mainView!!.tb_collection_objective_intake_game_piece_two.setBackgroundColor(Color.WHITE)
                } else if (!mainView!!.tb_collection_objective_intake_game_piece_two.isChecked){
                    mainView!!.tb_collection_objective_intake_game_piece_two.setBackgroundColor(Color.MAGENTA)
                }
            }

            mainView!!.tb_collection_objective_intake_game_piece_three.setOnClickListener {
                collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.AUTO_INTAKE_GAME_PIECE_THREE)
                autoIntakeGamePieceThree = !autoIntakeGamePieceThree
                if (mainView!!.tb_collection_objective_intake_game_piece_three.isChecked) {
                    collectionObjectiveActivity.scoringScreen = true
                    mainView!!.tb_collection_objective_intake_game_piece_three.setBackgroundColor(Color.WHITE)
                } else if (!mainView!!.tb_collection_objective_intake_game_piece_three.isChecked){
                    mainView!!.tb_collection_objective_intake_game_piece_three.setBackgroundColor(Color.MAGENTA)
                }
            }

            mainView!!.tb_collection_objective_intake_game_piece_four.setOnClickListener {
                collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.AUTO_INTAKE_GAME_PIECE_FOUR)
                autoIntakeGamePieceFour = !autoIntakeGamePieceFour
                if (mainView!!.tb_collection_objective_intake_game_piece_four.isChecked) {
                    collectionObjectiveActivity.scoringScreen = true
                    mainView!!.tb_collection_objective_intake_game_piece_four.setBackgroundColor(Color.WHITE)
                } else if (!mainView!!.tb_collection_objective_intake_game_piece_four.isChecked){
                    mainView!!.tb_collection_objective_intake_game_piece_four.setBackgroundColor(Color.MAGENTA)
                }

            }
        }
    }

    private fun setBackgrounds(){
        when {
            (orientation && alliance_color == Constants.AllianceColor.BLUE) ->
                mainView!!.objective_collection_intake_map.setImageResource(R.drawable.blue_up_game_pieces)
            (!orientation && alliance_color == Constants.AllianceColor.BLUE) ->
                mainView!!.objective_collection_intake_map.setImageResource(R.drawable.blue_down_game_pieces)
            (orientation && alliance_color == Constants.AllianceColor.RED) ->
                mainView!!.objective_collection_intake_map.setImageResource(R.drawable.red_up_game_pieces)
            (!orientation && alliance_color == Constants.AllianceColor.RED) ->
                mainView!!.objective_collection_intake_map.setImageResource(R.drawable.red_down_game_pieces)
        }
        when {
            (autoIntakeGamePieceOne) ->
                mainView!!.tb_collection_objective_intake_game_piece_one.setBackgroundColor(Color.WHITE)
            (!autoIntakeGamePieceOne) ->
                mainView!!.tb_collection_objective_intake_game_piece_one.setBackgroundColor(Color.MAGENTA)
        }
        when {
            (autoIntakeGamePieceTwo) ->
                mainView!!.tb_collection_objective_intake_game_piece_two.setBackgroundColor(Color.WHITE)
            (!autoIntakeGamePieceTwo) ->
                mainView!!.tb_collection_objective_intake_game_piece_two.setBackgroundColor(Color.MAGENTA)
        }
        when {
            (autoIntakeGamePieceThree) ->
                mainView!!.tb_collection_objective_intake_game_piece_three.setBackgroundColor(Color.WHITE)
            (!autoIntakeGamePieceThree) ->
                mainView!!.tb_collection_objective_intake_game_piece_three.setBackgroundColor(Color.MAGENTA)
        }
        when {
            (autoIntakeGamePieceFour) ->
                mainView!!.tb_collection_objective_intake_game_piece_four.setBackgroundColor(Color.WHITE)
            (!autoIntakeGamePieceFour) ->
                mainView!!.tb_collection_objective_intake_game_piece_four.setBackgroundColor(Color.MAGENTA)
        }

    }

    private fun enableButtons(isIncap: Boolean) {
        if (mainView != null && activity != null) {
            for (btn in listOf(mainView!!.tb_collection_objective_intake_game_piece_one, mainView!!.tb_collection_objective_intake_game_piece_two,
                mainView!!.tb_collection_objective_intake_game_piece_three, mainView!!.tb_collection_objective_intake_game_piece_four)) {
                btn.isEnabled =
                    CollectionObjectiveActivity.comingBack == "match information edit" ||
                            CollectionObjectiveActivity.comingBack == "QRGenerate" ||
                            !(!collectionObjectiveActivity.isTimerRunning || popup_open || isIncap)
            }
        }
    }
}