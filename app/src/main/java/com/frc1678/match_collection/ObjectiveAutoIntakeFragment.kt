package com.frc1678.match_collection

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.frc1678.match_collection.Constants.AllianceColor
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
                    autoIntakeGamePieceOne = true
                    collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.AUTO_INTAKE_GAME_PIECE_ONE)
                    mainView!!.tb_collection_objective_intake_game_piece_one.setBackgroundColor(Color.LTGRAY)
                    collectionObjectiveActivity.scoringScreen = true
                }
            }

            mainView!!.tb_collection_objective_intake_game_piece_two.setOnClickListener {
                if (!autoIntakeGamePieceTwo) {
                    autoIntakeGamePieceTwo = true
                    collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.AUTO_INTAKE_GAME_PIECE_TWO)
                    mainView!!.tb_collection_objective_intake_game_piece_two.setBackgroundColor(Color.LTGRAY)
                    collectionObjectiveActivity.scoringScreen = true
                }
            }

            mainView!!.tb_collection_objective_intake_game_piece_three.setOnClickListener {
                if (!autoIntakeGamePieceThree) {
                    autoIntakeGamePieceThree = true
                    collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.AUTO_INTAKE_GAME_PIECE_THREE)
                    mainView!!.tb_collection_objective_intake_game_piece_three.setBackgroundColor(Color.LTGRAY)
                    collectionObjectiveActivity.scoringScreen = true
                }
            }

            mainView!!.tb_collection_objective_intake_game_piece_four.setOnClickListener {
                if (!autoIntakeGamePieceFour) {
                    autoIntakeGamePieceFour = true
                    collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.AUTO_INTAKE_GAME_PIECE_FOUR)
                    mainView!!.tb_collection_objective_intake_game_piece_four.setBackgroundColor(Color.LTGRAY)
                    collectionObjectiveActivity.scoringScreen = true
                }
            }
        }
    }

    private fun setBackgrounds(){
        when {
            (orientation && alliance_color == AllianceColor.BLUE) ->
                mainView!!.objective_collection_intake_map.setImageResource(R.drawable.blue_up_game_pieces)
            (!orientation && alliance_color == AllianceColor.BLUE) ->
                mainView!!.objective_collection_intake_map.setImageResource(R.drawable.blue_down_game_pieces)
            (orientation && alliance_color == AllianceColor.RED) ->
                mainView!!.objective_collection_intake_map.setImageResource(R.drawable.red_up_game_pieces)
            (!orientation && alliance_color == AllianceColor.RED) ->
                mainView!!.objective_collection_intake_map.setImageResource(R.drawable.red_down_game_pieces)
        }
        when {
            (autoIntakeGamePieceOne) -> {
                mainView!!.tb_collection_objective_intake_game_piece_one.setBackgroundColor(Color.LTGRAY)
                mainView!!.tb_collection_objective_intake_game_piece_one.text = "${getString(R.string.taken)} ${getString(R.string.one_starting_position)}"
                mainView!!.tb_collection_objective_intake_game_piece_one.textSize = 18F
            } (!autoIntakeGamePieceOne) -> {
            if(alliance_color == AllianceColor.BLUE) {
                mainView!!.tb_collection_objective_intake_game_piece_one.setBackgroundColor(Color.BLUE)
            } else {
                mainView!!.tb_collection_objective_intake_game_piece_one.setBackgroundColor(Color.RED)
            }
                mainView!!.tb_collection_objective_intake_game_piece_one.text = getString(R.string.one_starting_position)
                mainView!!.tb_collection_objective_intake_game_piece_one.textSize = 50F
            }
        }
        when {
            (autoIntakeGamePieceTwo) -> {
                mainView!!.tb_collection_objective_intake_game_piece_two.setBackgroundColor(Color.LTGRAY)
                mainView!!.tb_collection_objective_intake_game_piece_two.text = "${getString(R.string.taken)} ${getString(R.string.two_starting_position)}"
                mainView!!.tb_collection_objective_intake_game_piece_two.textSize = 18F
            } (!autoIntakeGamePieceTwo) -> {
            if(alliance_color == AllianceColor.BLUE) {
                mainView!!.tb_collection_objective_intake_game_piece_two.setBackgroundColor(Color.BLUE)
            } else {
                mainView!!.tb_collection_objective_intake_game_piece_two.setBackgroundColor(Color.RED)
            }
                mainView!!.tb_collection_objective_intake_game_piece_two.text = getString(R.string.two_starting_position)
                mainView!!.tb_collection_objective_intake_game_piece_two.textSize = 50F
            }
        }
        when {
            (autoIntakeGamePieceThree) -> {
                mainView!!.tb_collection_objective_intake_game_piece_three.setBackgroundColor(Color.LTGRAY)
                mainView!!.tb_collection_objective_intake_game_piece_three.text = "${getString(R.string.taken)} ${getString(R.string.three_starting_position)}"
                mainView!!.tb_collection_objective_intake_game_piece_three.textSize = 18F
            } (!autoIntakeGamePieceThree) -> {
            if(alliance_color == AllianceColor.BLUE) {
                mainView!!.tb_collection_objective_intake_game_piece_three.setBackgroundColor(Color.BLUE)
            } else {
                mainView!!.tb_collection_objective_intake_game_piece_three.setBackgroundColor(Color.RED)
            }
                mainView!!.tb_collection_objective_intake_game_piece_three.text = getString(R.string.three_starting_position)
                mainView!!.tb_collection_objective_intake_game_piece_three.textSize = 50F
            }
        }
        when {
            (autoIntakeGamePieceFour) -> {
                mainView!!.tb_collection_objective_intake_game_piece_four.setBackgroundColor(Color.LTGRAY)
                mainView!!.tb_collection_objective_intake_game_piece_four.text = "${getString(R.string.taken)} ${getString(R.string.four_starting_position)}"
                mainView!!.tb_collection_objective_intake_game_piece_four.textSize = 18F
            } (!autoIntakeGamePieceFour) -> {
                if(alliance_color == AllianceColor.BLUE) {
                    mainView!!.tb_collection_objective_intake_game_piece_four.setBackgroundColor(Color.BLUE)
                } else {
                    mainView!!.tb_collection_objective_intake_game_piece_four.setBackgroundColor(Color.RED)
                }
                mainView!!.tb_collection_objective_intake_game_piece_four.text = getString(R.string.four_starting_position)
                mainView!!.tb_collection_objective_intake_game_piece_four.textSize = 50F
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