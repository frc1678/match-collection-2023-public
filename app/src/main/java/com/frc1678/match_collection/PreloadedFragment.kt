package com.frc1678.match_collection

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.preloaded_fragment.view.*

class PreloadedFragment : Fragment(R.layout.preloaded_fragment) {

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
        setColors()
        initOnClicks()
        enableButtons()
        return mainView
    }

    private val collectionObjectiveActivity get() = activity as CollectionObjectiveActivity

    /**
     * Changes the preloaded piece based on the button pressed
     * Changes the screen to scoring/auto depending on the changed preload
     */
    private fun initOnClicks() {
        if (mainView != null && activity != null) with(mainView!!) {
            // Remove previous action from timeline when undo button is clicked.
            btn_cone.setOnClickListener {
                preloaded = Constants.Preloaded.O
                setColors()
                setScreen()
            }

            // Replace previously undone action to timeline when redo button is clicked.
            btn_cube.setOnClickListener {
                preloaded = Constants.Preloaded.U
                setColors()
                setScreen()
            }

            btn_none.setOnClickListener {
                preloaded = Constants.Preloaded.N
                setColors()
                setScreen()
            }
        }
    }

    /**
     * Updates whether the undo or redo buttons are enabled
     */
    fun enableButtons() {
        if (mainView != null && activity != null) with(mainView!!) {
            // Enables the cone preload button if the match has started and a popup isn't open
            btn_cone.isEnabled = (!(popupOpen))

            // Enables the cube preload button if the match has started and a popup isn't open
            btn_cube.isEnabled = (!(popupOpen))

            // Enables the none preload button if the match has started and a popup isn't open
            btn_none.isEnabled = (!(popupOpen))
        }
    }

    /**
     * Sets the colors of the preloaded buttons depending on which preload is set
     * All unselected preloads are set to gray
     * The button for the selected preload is set the color of the gamepiece
     */
    private fun setColors() {
        if (mainView != null && activity != null) with(mainView!!) {
            val preloadButtons = listOf<Button>(btn_cone, btn_cube, btn_none)

            // Sets the background of all preload buttons to gray
            for (btn in preloadButtons) {
                btn.setBackgroundColor(resources.getColor(R.color.light_gray))
            }

            // Sets the background of the selected preload button to the corresponding color
            when (preloaded) {
                Constants.Preloaded.O -> btn_cone.setBackgroundColor(resources.getColor(R.color.cone_yellow))
                Constants.Preloaded.U -> btn_cube.setBackgroundColor(resources.getColor(R.color.cube_purple))
                Constants.Preloaded.N -> btn_none.setBackgroundColor(resources.getColor(R.color.gray))
            }
        }
    }

    /**
     * Sets the screen depending on the preload
     * If Cone is preloaded, sets to scoring screen and disables cube buttons
     * If Cube is preloaded, sets to scoring screen and disables cone buttons
     * If None is preloaded, sets to intake screen
     */
    private fun setScreen() {
        if (mainView != null && activity != null) {

            collectionObjectiveActivity.scoringScreen = preloaded != Constants.Preloaded.N

            collectionObjectiveActivity.enableButtons()
        }
    }
}