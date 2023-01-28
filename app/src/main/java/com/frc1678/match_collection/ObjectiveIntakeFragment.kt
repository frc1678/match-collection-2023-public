package com.frc1678.match_collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.frc1678.match_collection.Constants.Companion.previousScreen
import kotlinx.android.synthetic.main.collection_objective_intake_fragment.view.btn_action_one
import kotlinx.android.synthetic.main.collection_objective_intake_fragment.view.btn_action_three
import kotlinx.android.synthetic.main.collection_objective_intake_fragment.view.btn_action_two

/**
 * [Fragment] used for showing the intake buttons in [CollectionObjectiveActivity].
 */
class ObjectiveIntakeFragment : Fragment(R.layout.collection_objective_intake_fragment) {

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
        setCounterTexts()
        initOnClicks()
        enableButtons(collectionObjectiveActivity.isIncap)
        return mainView
    }

    /**
     * The parent activity of this fragment.
     *
     * @see getActivity
     */
    private val collectionObjectiveActivity get() = activity as CollectionObjectiveActivity

    /**
     * Updates the text of the buttons with the number of times they have been pressed.
     */
    private fun setCounterTexts() {
        if (mainView != null && activity != null) with(mainView!!) {
            btn_action_one.text = getString(R.string.btn_action_one, numActionOne.toString())
            btn_action_two.text = getString(R.string.btn_action_two, numActionTwo.toString())
            btn_action_three.text = getString(R.string.btn_action_three, numActionThree.toString())
        }
    }

    /**
     * Initializes the `OnClickListener`s of the intake buttons.
     */
    private fun initOnClicks() {
        if (mainView != null && activity != null) with(mainView!!) {
            btn_action_one.setOnClickListener {
                collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.INTAKE_STATION)
                numActionOne++
                collectionObjectiveActivity.scoringScreen = true
                setCounterTexts()
            }

            btn_action_two.setOnClickListener {
                collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.INTAKE_LOW_ROW)
                numActionTwo++
                collectionObjectiveActivity.scoringScreen = true
                setCounterTexts()
            }

            btn_action_three.setOnClickListener {
                collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.INTAKE_GROUND)
                numActionThree++
                collectionObjectiveActivity.scoringScreen = true
                setCounterTexts()
            }
        }
    }

    /**
     * Updates whether the intake buttons are enabled.
     *
     * @param isIncap Whether the robot is currently incap.
     */
    fun enableButtons(isIncap: Boolean) {
        if (mainView != null && activity != null) with(mainView!!) {
            for (btn in listOf(btn_action_one, btn_action_two, btn_action_three)) {
                btn.isEnabled =
                    activity!!.previousScreen == Constants.Screens.MATCH_INFORMATION_EDIT ||
                            activity!!.previousScreen == Constants.Screens.QR_GENERATE ||
                            !(!collectionObjectiveActivity.isTimerRunning || popup_open || isIncap)
            }
        }
    }
}
