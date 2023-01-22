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
        enableButtons()
        setBackgrounds()
        return mainView
    }

    private val collectionObjectiveActivity get() = activity as CollectionObjectiveActivity


    private fun initOnClicks() {
        if (mainView != null) {
            mainView!!.tb_collection_objective_intake_game_piece_one.setOnClickListener {
                if (!autoIntakeGamePieceOne) {
                    tb_collection_objective_intake_game_piece_one.text = "Taken"
                    autoIntakeGamePieceOne = true
                    collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.AUTO_INTAKE_GAME_PIECE_ONE)
                    mainView!!.tb_collection_objective_intake_game_piece_one.setBackgroundColor(Color.WHITE)
                    collectionObjectiveActivity.scoringScreen = true
                }
            }

            mainView!!.tb_collection_objective_intake_game_piece_two.setOnClickListener {

                if (!autoIntakeGamePieceTwo) {
                    tb_collection_objective_intake_game_piece_two.text = "Taken"
                    autoIntakeGamePieceTwo = true
                    collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.AUTO_INTAKE_GAME_PIECE_TWO)
                    mainView!!.tb_collection_objective_intake_game_piece_two.setBackgroundColor(Color.WHITE)
                    collectionObjectiveActivity.scoringScreen = true
                }
            }

            mainView!!.tb_collection_objective_intake_game_piece_three.setOnClickListener {
                if (!autoIntakeGamePieceThree) {
                    tb_collection_objective_intake_game_piece_three.text = "Taken"
                    autoIntakeGamePieceThree = true
                    collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.AUTO_INTAKE_GAME_PIECE_THREE)
                    mainView!!.tb_collection_objective_intake_game_piece_three.setBackgroundColor(Color.WHITE)
                    collectionObjectiveActivity.scoringScreen = true
                }
            }

            mainView!!.tb_collection_objective_intake_game_piece_four.setOnClickListener {
                if (!autoIntakeGamePieceFour) {
                    tb_collection_objective_intake_game_piece_four.text = "Taken"
                    autoIntakeGamePieceFour = true
                    collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.AUTO_INTAKE_GAME_PIECE_FOUR)
                    mainView!!.tb_collection_objective_intake_game_piece_four.setBackgroundColor(Color.WHITE)
                    collectionObjectiveActivity.scoringScreen = true
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
            (autoIntakeGamePieceOne) -> {
                mainView!!.tb_collection_objective_intake_game_piece_one.setBackgroundColor(Color.WHITE)
                mainView!!.tb_collection_objective_intake_game_piece_one.text = "Taken"
            } (!autoIntakeGamePieceOne) -> {
                mainView!!.tb_collection_objective_intake_game_piece_one.setBackgroundColor(Color.MAGENTA)
                mainView!!.tb_collection_objective_intake_game_piece_one.text = "On Ground"
            }
        }
        when {
            (autoIntakeGamePieceTwo) -> {
                mainView!!.tb_collection_objective_intake_game_piece_two.setBackgroundColor(Color.WHITE)
                mainView!!.tb_collection_objective_intake_game_piece_two.text = "Taken"
            } (!autoIntakeGamePieceTwo) -> {
                mainView!!.tb_collection_objective_intake_game_piece_two.setBackgroundColor(Color.MAGENTA)
                mainView!!.tb_collection_objective_intake_game_piece_two.text = "On Ground"
            }
        }
        when {
            (autoIntakeGamePieceThree) -> {
                mainView!!.tb_collection_objective_intake_game_piece_three.setBackgroundColor(Color.WHITE)
                mainView!!.tb_collection_objective_intake_game_piece_three.text = "Taken"
            } (!autoIntakeGamePieceThree) -> {
                mainView!!.tb_collection_objective_intake_game_piece_three.setBackgroundColor(Color.MAGENTA)
                mainView!!.tb_collection_objective_intake_game_piece_three.text = "On Ground"
            }
        }
        when {
            (autoIntakeGamePieceFour) -> {
                mainView!!.tb_collection_objective_intake_game_piece_four.setBackgroundColor(Color.WHITE)
                mainView!!.tb_collection_objective_intake_game_piece_four.text = "Taken"
            } (!autoIntakeGamePieceFour) -> {
                mainView!!.tb_collection_objective_intake_game_piece_four.setBackgroundColor(Color.MAGENTA)
                mainView!!.tb_collection_objective_intake_game_piece_four.text = "On Ground"
            }
        }
    }

    fun enableButtons() {
        if (mainView != null && activity != null) with(mainView!!) {
            for (btn in listOf(
                mainView!!.tb_collection_objective_intake_game_piece_one,
                mainView!!.tb_collection_objective_intake_game_piece_two,
                mainView!!.tb_collection_objective_intake_game_piece_three,
                mainView!!.tb_collection_objective_intake_game_piece_four
            )) {
                btn.isEnabled = (collectionObjectiveActivity.isTimerRunning and !popup_open)
            }
        }
    }
}