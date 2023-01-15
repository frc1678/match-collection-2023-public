package com.frc1678.match_collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.collection_objective_intake_fragment.view.btn_action_one
import kotlinx.android.synthetic.main.collection_objective_intake_fragment.view.btn_action_three
import kotlinx.android.synthetic.main.collection_objective_intake_fragment.view.btn_action_two

class ObjectiveIntakeFragment : Fragment(R.layout.collection_objective_intake_fragment) {

    private var mainView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainView = super.onCreateView(inflater, container, savedInstanceState)!!
        setCounterTexts()
        initOnClicks()
        enableButtons(collectionObjectiveActivity.isIncap)
        return mainView
    }

    private val collectionObjectiveActivity get() = activity as CollectionObjectiveActivity

    private fun setCounterTexts() {
        if (mainView != null && activity != null) with(mainView!!) {
            btn_action_one.text = getString(R.string.btn_action_one, numActionOne.toString())
            btn_action_two.text = getString(R.string.btn_action_two, numActionTwo.toString())
            btn_action_three.text = getString(R.string.btn_action_three, numActionThree.toString())
        }
    }

    private fun initOnClicks() {
        if (mainView != null && activity != null) with(mainView!!) {
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

    fun enableButtons(isIncap: Boolean) {
        if (mainView != null && activity != null) with(mainView!!) {
            for (btn in listOf(btn_action_one, btn_action_two, btn_action_three)) {
                btn.isEnabled =
                    CollectionObjectiveActivity.comingBack == "match information edit" ||
                            CollectionObjectiveActivity.comingBack == "QRGenerate" ||
                            !(!collectionObjectiveActivity.isTimerRunning || popup_open || isIncap)
            }
        }
    }
}
