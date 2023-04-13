package com.frc1678.match_collection

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import com.frc1678.match_collection.Constants.Companion.previousScreen
import kotlinx.android.synthetic.main.collection_objective_intake_fragment.view.*
import kotlinx.android.synthetic.main.intake_popup.view.*

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
            btn_action_five.text = getString(R.string.btn_action_three, numActionFive.toString())
            btn_action_thirteen.text = getString(R.string.btn_action_thirteen, numActionThirteen.toString())
        }
    }

    /**
     * Initializes the `OnClickListener`s of the intake buttons.
     */
    private fun initOnClicks() {
        if (mainView != null && activity != null) with(mainView!!) {
            btn_action_one.setOnClickListener {
                collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.INTAKE_DOUBLE)
                numActionOne++
                collectionObjectiveActivity.scoringScreen = true
                setCounterTexts()
            }

            btn_action_thirteen.setOnClickListener {
                collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.INTAKE_SINGLE)
                numActionThirteen++
                collectionObjectiveActivity.scoringScreen = true
                setCounterTexts()
            }

            /**
             * Creates a popup for intaking from different row levels
             * Sets functionality for the buttons
             */
            btn_action_row.setOnClickListener {
                val popupView = View.inflate(collectionObjectiveActivity, R.layout.intake_popup, null)
                val width = LinearLayout.LayoutParams.WRAP_CONTENT
                val height = LinearLayout.LayoutParams.WRAP_CONTENT
                val popupWindow = PopupWindow(popupView, width, height, false)
                popupWindow.showAtLocation(it, Gravity.CENTER, 0, 0)
                popupOpen = true
                collectionObjectiveActivity.enableButtons()

                popupView.btn_intake_low_row.text = getString(R.string.btn_low_row, numActionFour.toString())
                popupView.btn_intake_mid_row.text = getString(R.string.btn_mid_row, numActionThree.toString())
                popupView.btn_intake_high_row.text = getString(R.string.btn_high_row, numActionTwo.toString())

                // OnClickListeners for the buttons in the intake popup.
                popupView.btn_intake_cancel.setOnClickListener {
                    popupWindow.dismiss()
                    popupOpen = false
                    collectionObjectiveActivity.enableButtons()
                }

                popupView.btn_intake_low_row.setOnClickListener {
                    collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.INTAKE_LOW_ROW)
                    numActionFour++
                    popupWindow.dismiss()
                    popupOpen = false
                    collectionObjectiveActivity.scoringScreen = true
                    collectionObjectiveActivity.enableButtons()
                }

                popupView.btn_intake_mid_row.setOnClickListener {
                    collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.INTAKE_MID_ROW)
                    numActionThree++
                    popupWindow.dismiss()
                    popupOpen = false
                    collectionObjectiveActivity.scoringScreen = true
                    collectionObjectiveActivity.enableButtons()
                }

                popupView.btn_intake_high_row.setOnClickListener {
                    collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.INTAKE_HIGH_ROW)
                    numActionTwo++
                    popupWindow.dismiss()
                    popupOpen = false
                    collectionObjectiveActivity.scoringScreen = true
                    collectionObjectiveActivity.enableButtons()
                }
            }

            btn_action_five.setOnClickListener {
                collectionObjectiveActivity.timelineAddWithStage(action_type = Constants.ActionType.INTAKE_GROUND)
                numActionFive++
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
            for (btn in listOf(btn_action_one, btn_action_row, btn_action_five, btn_action_thirteen)) {
                btn.isEnabled =
                    requireActivity().previousScreen == Constants.Screens.MATCH_INFORMATION_EDIT ||
                            requireActivity().previousScreen == Constants.Screens.QR_GENERATE ||
                            !(!collectionObjectiveActivity.isTimerRunning || popupOpen || isIncap ||
                                (timeline.size > 0 && timeline[timeline.size - 1]["action_type"].toString() == Constants.ActionType.SUPERCHARGE.toString()))
            }
        }
    }
}
