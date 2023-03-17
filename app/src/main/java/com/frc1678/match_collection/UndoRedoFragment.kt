package com.frc1678.match_collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.undo_redo_fragment.view.*

class UndoRedoFragment : Fragment(R.layout.undo_redo_fragment) {

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
        updateUndoRedo()
        return mainView
    }

    private val collectionObjectiveActivity get() = activity as CollectionObjectiveActivity

    fun updateUndoRedo() {
        if (mainView != null && activity != null) with(mainView!!) {
            // Get the "Undo" text
            val undoText = resources.getText(R.string.btn_undo)
            btn_undo.text = if (timeline.isEmpty()) {
                // Nothing to undo
                undoText
            } else {
                // Uses \n to put the action name on a newline
                "$undoText\n${
                    timeline.last()["action_type"]?.split('_')?.joinToString(" ") {
                        it.lowercase().replaceFirstChar { char -> char.uppercaseChar() }
                    }
                }"
            }
            // Get the "Redo" text
            val redoText = resources.getText(R.string.btn_redo)
            btn_redo.text = if (collectionObjectiveActivity.removedTimelineActions.isEmpty()) {
                // Nothing to redo
                redoText
            } else {
                // Uses \n to put the action name on a newline
                "$redoText\n${
                    collectionObjectiveActivity.removedTimelineActions.last()["action_type"]?.split(
                        '_'
                    )?.joinToString(" ") {
                        it.lowercase().replaceFirstChar { char -> char.uppercaseChar() }
                    }
                }"
            }
        }
    }

    /**
     * Removes the last timeline action when the undo button is clicked
     * Replaces the last timeline action that was removed when the redo button is clicked
     */

    private fun initOnClicks() {
        if (mainView != null && activity != null) with(mainView!!) {
            collectionObjectiveActivity.undoRedoScreen = true

            // Remove previous action from timeline when undo button is clicked.
            btn_undo.setOnClickListener {
                collectionObjectiveActivity.timelineRemove()
            }

            // Replace previously undone action to timeline when redo button is clicked.
            btn_redo.setOnClickListener {
                collectionObjectiveActivity.timelineReplace()
            }
        }
    }

    /**
     * Updates whether the undo or redo buttons are enabled
     */
    fun enableButtons() {
        if (mainView != null && activity != null) with(mainView!!) {
            // Enables the undo button if the timeline is not empty and a popup isn't open
            btn_undo.isEnabled = ((timeline.size > 0) && !popupOpen)

            // Enables the redo button if the removedTimelineActions is not empty and a popup isn't open
            btn_redo.isEnabled =
                ((collectionObjectiveActivity.removedTimelineActions.size > 0) && !popupOpen)
        }
    }
}