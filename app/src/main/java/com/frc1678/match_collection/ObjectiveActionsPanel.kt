package com.frc1678.match_collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.collection_objective_intake_activity.*
import kotlinx.android.synthetic.main.collection_objective_scoring_activity.*

class ObjectiveScoringPanel : Fragment(R.layout.collection_objective_scoring_activity) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        setCounterTexts()
        return view
    }

    private val collectionObjectiveActivity get() = activity as CollectionObjectiveActivity

    fun setCounterTexts() {
            btn_action_four.text = getString(R.string.btn_action_four, numActionFour.toString())
            btn_action_five.text = getString(R.string.btn_action_five, numActionFive.toString())
            btn_action_six.text = getString(R.string.btn_action_six, numActionSix.toString())
            btn_action_seven.text = getString(R.string.btn_action_seven, numActionSeven.toString())
            btn_action_eight.text = getString(R.string.btn_action_eight, numActionEight.toString())
            btn_action_nine.text = getString(R.string.btn_action_nine, numActionNine.toString())
            btn_action_ten.text = getString(R.string.btn_action_ten, numActionTen.toString())
        }

    fun initOnClicks() {
        // Increment button action one by one when clicked and add action to timeline.
        btn_action_four.setOnClickListener {
            // FALSE = LOW
            collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.SCORE_CUBE_HIGH)
            numActionFour++
            collectionObjectiveActivity.scoringScreen = false
            setCounterTexts()
        }

        btn_action_five.setOnClickListener {
            // FALSE = LOW
            collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.SCORE_CUBE_MID)
            numActionFive++
            collectionObjectiveActivity.scoringScreen = false
            setCounterTexts()
        }

        btn_action_six.setOnClickListener {
            // FALSE = LOW
            collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.SCORE_CUBE_LOW)
            numActionSix++
            collectionObjectiveActivity.scoringScreen = false
            setCounterTexts()
        }

        btn_action_seven.setOnClickListener {
            // FALSE = LOW
            collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.SCORE_CONE_HIGH)
            numActionSeven++
            collectionObjectiveActivity.scoringScreen = false
            setCounterTexts()
        }

        btn_action_eight.setOnClickListener {
            // FALSE = LOW
            collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.SCORE_CONE_MID)
            numActionEight++
            collectionObjectiveActivity.scoringScreen = false
            setCounterTexts()
        }

        btn_action_nine.setOnClickListener {
            // FALSE = LOW
            collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.SCORE_CONE_LOW)
            numActionNine++
            collectionObjectiveActivity.scoringScreen = false
            setCounterTexts()
        }

        btn_action_ten.setOnClickListener {
            // FALSE = LOW
            collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.FAIL)
            numActionTen++
            collectionObjectiveActivity.scoringScreen = false
            setCounterTexts()
        }
    }
}

class ObjectiveIntakePanel : Fragment(R.layout.collection_objective_intake_activity) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        setCounterTexts()
        initOnClicks()
        return view
    }

    private val collectionObjectiveActivity get() = activity as CollectionObjectiveActivity

    fun setCounterTexts() {
        btn_action_one.text = getString(R.string.btn_action_one, numActionOne.toString())
        btn_action_two.text = getString(R.string.btn_action_two, numActionTwo.toString())
        btn_action_three.text = getString(R.string.btn_action_three, numActionThree.toString())
    }
     fun initOnClicks() {
         btn_action_one.setOnClickListener {
             // FALSE = LOW
             collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.STATION_INTAKE)
             numActionOne++
             collectionObjectiveActivity.scoringScreen = true
             setCounterTexts()
         }

         // Increment button action two by one when clicked and add action to timeline.
         btn_action_two.setOnClickListener {
             // FALSE = LOW
             collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.LOW_ROW_INTAKE)
             numActionTwo++
             collectionObjectiveActivity.scoringScreen = true
             setCounterTexts()
         }

         // Increment button action three by one when clicked and add action to timeline.
         btn_action_three.setOnClickListener {
             collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.GROUND_INTAKE)
             numActionThree++
             collectionObjectiveActivity.scoringScreen = true
             setCounterTexts()
         }
     }

    }

