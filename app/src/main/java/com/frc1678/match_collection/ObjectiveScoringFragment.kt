package com.frc1678.match_collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.frc1678.match_collection.Constants.Companion.previousScreen
import kotlinx.android.synthetic.main.collection_objective_scoring_fragment.view.btn_action_eight
import kotlinx.android.synthetic.main.collection_objective_scoring_fragment.view.btn_action_five
import kotlinx.android.synthetic.main.collection_objective_scoring_fragment.view.btn_action_four
import kotlinx.android.synthetic.main.collection_objective_scoring_fragment.view.btn_action_nine
import kotlinx.android.synthetic.main.collection_objective_scoring_fragment.view.btn_action_seven
import kotlinx.android.synthetic.main.collection_objective_scoring_fragment.view.btn_action_six
import kotlinx.android.synthetic.main.collection_objective_scoring_fragment.view.btn_action_ten

/**
 * [Fragment] used for showing the scoring buttons in [CollectionObjectiveActivity].
 */
class ObjectiveScoringFragment : Fragment(R.layout.collection_objective_scoring_fragment) {

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
        enableButtons(collectionObjectiveActivity.isIncap, collectionObjectiveActivity.isCharging)
        return mainView
    }

    /**
     * The parent activity of this fragment.
     *
     * @see getActivity
     */
    private val collectionObjectiveActivity get() = activity as CollectionObjectiveActivity

    /**
     * Updates buttons with the number of times they have been pressed.
     */
    private fun setCounterTexts() {
        if (mainView != null && activity != null) with(mainView!!) {
            btn_action_four.text = getString(R.string.btn_action_four, numActionFour.toString())
            btn_action_five.text = getString(R.string.btn_action_five, numActionFive.toString())
            btn_action_six.text = getString(R.string.btn_action_six, numActionSix.toString())
            btn_action_seven.text = getString(R.string.btn_action_seven, numActionSeven.toString())
            btn_action_eight.text = getString(R.string.btn_action_eight, numActionEight.toString())
            btn_action_nine.text = getString(R.string.btn_action_nine, numActionNine.toString())
            btn_action_ten.text = getString(R.string.btn_action_ten, numActionTen.toString())
        }
    }

    /**
     * Initializes the `OnClickListener`s for the scoring buttons.
     */
    private fun initOnClicks() {
        if (mainView != null && activity != null) with(mainView!!) {
            // Increment button action one by one when clicked and add action to timeline.
            btn_action_four.setOnClickListener {
                collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.SCORE_CUBE_HIGH)
                numActionFour++
                collectionObjectiveActivity.scoringScreen = false
                setCounterTexts()
            }

            btn_action_five.setOnClickListener {
                collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.SCORE_CUBE_MID)
                numActionFive++
                collectionObjectiveActivity.scoringScreen = false
                setCounterTexts()
            }

            btn_action_six.setOnClickListener {
                collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.SCORE_CUBE_LOW)
                numActionSix++
                collectionObjectiveActivity.scoringScreen = false
                setCounterTexts()
            }

            btn_action_seven.setOnClickListener {
                collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.SCORE_CONE_HIGH)
                numActionSeven++
                collectionObjectiveActivity.scoringScreen = false
                setCounterTexts()
            }

            btn_action_eight.setOnClickListener {
                collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.SCORE_CONE_MID)
                numActionEight++
                collectionObjectiveActivity.scoringScreen = false
                setCounterTexts()
            }

            btn_action_nine.setOnClickListener {
                collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.SCORE_CONE_LOW)
                numActionNine++
                collectionObjectiveActivity.scoringScreen = false
                setCounterTexts()
            }

            btn_action_ten.setOnClickListener {
                collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.SCORE_FAIL)
                numActionTen++
                collectionObjectiveActivity.scoringScreen = false
                setCounterTexts()
            }
        }
    }

    /**
     * Updates whether the scoring buttons are enabled.
     *
     * @param isIncap Whether the robot is currently incap.
     * @param isCharging Whether the robot has charged already this game section.
     */
    fun enableButtons(isIncap: Boolean, isCharging: Boolean) {
        if (mainView != null && activity != null) with(mainView!!) {
            for (btn in listOf(
                btn_action_four,
                btn_action_five,
                btn_action_six,
                btn_action_seven,
                btn_action_eight,
                btn_action_nine,
                btn_action_ten
            )) {
                btn.isEnabled =
                    activity!!.previousScreen == Constants.Screens.MATCH_INFORMATION_EDIT ||
                            activity!!.previousScreen == Constants.Screens.QR_GENERATE ||
                            !(!collectionObjectiveActivity.isTimerRunning || popupOpen || isIncap || isCharging)
            }

            // Disable scoring buttons if their count is at the max
            btn_action_four.isEnabled =
                btn_action_four.isEnabled
            btn_action_five.isEnabled =
                btn_action_five.isEnabled
            btn_action_six.isEnabled =
                btn_action_six.isEnabled
            btn_action_seven.isEnabled =
                btn_action_seven.isEnabled
            btn_action_eight.isEnabled =
                btn_action_eight.isEnabled
            btn_action_nine.isEnabled =
                btn_action_nine.isEnabled

            /**
             * If you have yet to score your preload then this disables scoring sections that do not apply to your preload.
             * For example, if you have a cone, you can not score a cube.
             */
            if((numActionFour == 0) and (numActionFive == 0) and (numActionSix == 0) and (numActionSeven == 0)
                and (numActionEight == 0) and (numActionNine == 0) and (numActionTen == 0)) {
                if (preloaded == Constants.Preloaded.O){
                    btn_action_four.isEnabled = false
                    btn_action_five.isEnabled = false
                    btn_action_six.isEnabled = false
                } else if (preloaded == Constants.Preloaded.U) {
                    btn_action_seven.isEnabled = false
                    btn_action_eight.isEnabled = false
                    btn_action_nine.isEnabled = false
                }
            }
        }
    }
}
