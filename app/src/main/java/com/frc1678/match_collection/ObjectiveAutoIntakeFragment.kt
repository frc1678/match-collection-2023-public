package com.frc1678.match_collection

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.frc1678.match_collection.Constants.AllianceColor
import kotlinx.android.synthetic.main.collection_objective_auto_intake_fragment.view.*

/**
 * [Fragment] used for showing intake buttons in [ObjectiveAutoIntakeFragment]
 */
class ObjectiveAutoIntakeFragment : Fragment(R.layout.collection_objective_auto_intake_fragment) {
    /**
     * The main view of this fragment.
     */
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

    /**
     * Parent activity of this fragment
     */
    private val collectionObjectiveActivity get() = activity as CollectionObjectiveActivity

    /**
     * Initialize button and toggle button `onClickListeners`.
     */
    private fun initOnClicks() {
        if (mainView != null) {

            /**
             * If 0 it goes to 1 and adds to the timeline.
             * Changes the color of game piece one to light gray.
             */
            mainView!!.tb_collection_objective_intake_game_piece_one.setOnClickListener {
                if (autoIntakeGamePieceOne == 0) {
                    autoIntakeGamePieceOne = 1
                    collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.AUTO_INTAKE_ONE)
                    mainView!!.tb_collection_objective_intake_game_piece_one.setBackgroundColor(Color.LTGRAY)
                    collectionObjectiveActivity.scoringScreen = true
                }
            }
            /**
             * If 0 it goes to 1 and adds to the timeline.
             * Changes the color of game piece two to light gray.
             */
            mainView!!.tb_collection_objective_intake_game_piece_two.setOnClickListener {
                if (autoIntakeGamePieceTwo == 0) {
                    autoIntakeGamePieceTwo = 1
                    collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.AUTO_INTAKE_TWO)
                    mainView!!.tb_collection_objective_intake_game_piece_two.setBackgroundColor(Color.LTGRAY)
                    collectionObjectiveActivity.scoringScreen = true
                }
            }
            /**
             * If 0 it goes to 1 and adds to the timeline.
             * Changes the color of game piece three to light gray.
             */
            mainView!!.tb_collection_objective_intake_game_piece_three.setOnClickListener {
                if (autoIntakeGamePieceThree == 0) {
                    autoIntakeGamePieceThree = 1
                    collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.AUTO_INTAKE_THREE)
                    mainView!!.tb_collection_objective_intake_game_piece_three.setBackgroundColor(Color.LTGRAY)
                    collectionObjectiveActivity.scoringScreen = true
                }
            }
            /**
             * If 0 it goes to 1 and adds to the timeline.
             * Changes the color of game piece four to light gray.
             */
            mainView!!.tb_collection_objective_intake_game_piece_four.setOnClickListener {
                if (autoIntakeGamePieceFour == 0) {
                    autoIntakeGamePieceFour = 1
                    collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.AUTO_INTAKE_FOUR)
                    mainView!!.tb_collection_objective_intake_game_piece_four.setBackgroundColor(Color.LTGRAY)
                    collectionObjectiveActivity.scoringScreen = true
                }
            }
        }
    }

    private fun setBackgrounds(){
        /**
         * When the alliance color is blue, map shows blue game pieces
         * When the alliance color is red, map shows red game pieces
         */
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
            (autoIntakeGamePieceOne == 1) -> {
                mainView!!.tb_collection_objective_intake_game_piece_one.setBackgroundColor(Color.LTGRAY)
                mainView!!.tb_collection_objective_intake_game_piece_one.text = "${getString(R.string.taken)} ${getString(R.string.one_starting_position)}"
                mainView!!.tb_collection_objective_intake_game_piece_one.textSize = 18F
            } (autoIntakeGamePieceOne == 0) -> {
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
            (autoIntakeGamePieceTwo == 1) -> {
                mainView!!.tb_collection_objective_intake_game_piece_two.setBackgroundColor(Color.LTGRAY)
                mainView!!.tb_collection_objective_intake_game_piece_two.text = "${getString(R.string.taken)} ${getString(R.string.two_starting_position)}"
                mainView!!.tb_collection_objective_intake_game_piece_two.textSize = 18F
            } (autoIntakeGamePieceTwo == 0) -> {
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
            (autoIntakeGamePieceThree == 1) -> {
                mainView!!.tb_collection_objective_intake_game_piece_three.setBackgroundColor(Color.LTGRAY)
                mainView!!.tb_collection_objective_intake_game_piece_three.text = "${getString(R.string.taken)} ${getString(R.string.three_starting_position)}"
                mainView!!.tb_collection_objective_intake_game_piece_three.textSize = 18F
            } (autoIntakeGamePieceThree == 0) -> {
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
            (autoIntakeGamePieceFour == 1) -> {
                mainView!!.tb_collection_objective_intake_game_piece_four.setBackgroundColor(Color.LTGRAY)
                mainView!!.tb_collection_objective_intake_game_piece_four.text = "${getString(R.string.taken)} ${getString(R.string.four_starting_position)}"
                mainView!!.tb_collection_objective_intake_game_piece_four.textSize = 18F
            } (autoIntakeGamePieceFour == 0) -> {
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