package com.frc1678.match_collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.collection_objective_auto_intake_fragment.*

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
        return mainView
    }

    private val collectionObjectiveActivity get() = activity as CollectionObjectiveActivity


    fun initOnClicks() {
        collection_objective_intake_game_peice_one.setOnClickListener {
            collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.AUTO_INTAKE_GAME_PIECE_ONE)
            autoIntakeGamePieceOne =! autoIntakeGamePieceOne
            collectionObjectiveActivity.scoringScreen = true
        }

        collection_objective_intake_game_peice_two.setOnClickListener {
            collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.AUTO_INTAKE_GAME_PIECE_TWO)
            autoIntakeGamePieceTwo =! autoIntakeGamePieceTwo
            collectionObjectiveActivity.scoringScreen = true
        }

        collection_objective_intake_game_peice_three.setOnClickListener {
            collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.AUTO_INTAKE_GAME_PIECE_THREE)
            autoIntakeGamePieceThree = !autoIntakeGamePieceThree
            collectionObjectiveActivity.scoringScreen = true
        }

        collection_objective_intake_game_peice_four.setOnClickListener {
            collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.AUTO_INTAKE_GAME_PIECE_FOUR)
            autoIntakeGamePieceFour = !autoIntakeGamePieceFour
            collectionObjectiveActivity.scoringScreen = true
        }
    }

    fun enableButtons(isIncap: Boolean) {
        if (mainView != null && activity != null) with (mainView!!) {
            for (btn in listOf(collection_objective_intake_game_peice_one, collection_objective_intake_game_peice_two,
                collection_objective_intake_game_peice_three,collection_objective_intake_game_peice_four)) {
                btn.isEnabled =
                    CollectionObjectiveActivity.comingBack == "match information edit" ||
                            CollectionObjectiveActivity.comingBack == "QRGenerate" ||
                            !(!collectionObjectiveActivity.isTimerRunning || popup_open || isIncap)
            }
        }
    }

}