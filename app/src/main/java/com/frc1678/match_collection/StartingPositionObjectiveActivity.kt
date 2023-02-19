package com.frc1678.match_collection

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frc1678.match_collection.Constants.Companion.PREVIOUS_SCREEN
import kotlinx.android.synthetic.main.starting_position_activity.btn_proceed_starting_position
import kotlinx.android.synthetic.main.starting_position_activity.btn_switch_orientation
import kotlinx.android.synthetic.main.starting_position_activity.compose_map
import kotlinx.android.synthetic.main.starting_position_activity.spinner_preloaded
import kotlinx.android.synthetic.main.starting_position_activity.tv_pos_team_number

class StartingPositionObjectiveActivity : CollectionActivity() {

    private fun initOnClicks() {
        btn_switch_orientation.setOnClickListener {
            orientation = !orientation
        }
        // Moves onto the next screen if you have inputted all the information
        btn_proceed_starting_position.setOnClickListener { view ->
            if (startingPosition != null) {
                // If you did not select a starting position, the team is assumed to be a no-show.
                // This will allow you to skip the collection activity.
                intent = if (startingPosition == 0) {
                    Intent(this, MatchInformationEditActivity::class.java)
                } else {
                    Intent(this, CollectionObjectiveActivity::class.java)
                }.putExtra(PREVIOUS_SCREEN, Constants.Screens.STARTING_POSITION_OBJECTIVE)
                startActivity(
                    intent, ActivityOptions.makeSceneTransitionAnimation(
                        this, btn_proceed_starting_position, "proceed_button"
                    ).toBundle()
                )
            } else createErrorMessage(getString(R.string.error_missing_information), view)
        }
    }

    /**
     * Initializes the spinner for selecting the preloaded game object.
     */
    private fun initSpinner() {
        // Set the adapter for the spinner
        spinner_preloaded.adapter = object : ArrayAdapter<String>(
            this, R.layout.spinner_preloaded,
            Constants.Preloaded.values().map {
                when (it) {
                    Constants.Preloaded.O -> "CONE"
                    Constants.Preloaded.U -> "CUBE"
                    Constants.Preloaded.N -> "NONE"
                }
            }
        ) {
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
                super.getDropDownView(position, convertView, parent).apply {
                    // Set the background color of the spinner item depending on its value
                    setBackgroundColor(
                        resources.getColor(
                            when ((this as TextView).text) {
                                "CONE" -> R.color.cone_yellow
                                "CUBE" -> R.color.cube_purple
                                else -> R.color.light_gray
                            }, null
                        )
                    )
                }
        }

        // Saves what the preloaded piece spinner says
        spinner_preloaded.setSelection(Constants.Preloaded.values().indexOf(preloaded))

        spinner_preloaded.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                // Update the preloaded item when a spinner item is selected
                preloaded = Constants.Preloaded.values()[position]
                // Set the background color of the spinner based on its value
                spinner_preloaded.setBackgroundColor(
                    when (preloaded) {
                        Constants.Preloaded.N -> resources.getColor(R.color.light_gray, null)
                        Constants.Preloaded.O -> resources.getColor(R.color.cone_yellow, null)
                        Constants.Preloaded.U -> resources.getColor(R.color.cube_purple, null)
                    }
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    /**
     * Show dialog to restart from [MatchInformationInputActivity] when back button is long pressed.
     */
    override fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) AlertDialog.Builder(this)
            .setMessage(R.string.error_back_reset)
            .setPositiveButton("Yes") { _, _ ->
                startActivity(
                    Intent(this, MatchInformationInputActivity::class.java)
                        .putExtra("team_number", teamNumber)
                        .putExtra(PREVIOUS_SCREEN, Constants.Screens.STARTING_POSITION_OBJECTIVE),
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                )
            }
            .show()
        return super.onKeyLongPress(keyCode, event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.starting_position_activity)
        compose_map.setContent { MapContent() }

        // Initialize the team number text
        tv_pos_team_number.text = teamNumber
        tv_pos_team_number.setTextColor(
            resources.getColor(
                if (allianceColor == Constants.AllianceColor.RED) R.color.alliance_red_light
                else R.color.alliance_blue_light,
                null
            )
        )

        resetCollectionReferences()

        initOnClicks()
        initSpinner()
        spinner_preloaded.isEnabled = (startingPosition != 0)
    }

    @Composable
    fun MapContent() {
        var selected: Int? by remember { mutableStateOf(null) }
        LaunchedEffect(selected) {
            startingPosition = selected
            spinner_preloaded.isEnabled = selected != 0
            if (selected == 0) spinner_preloaded.setSelection(
                Constants.Preloaded.values().indexOf(Constants.Preloaded.N)
            )
        }
        Column {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Box(modifier = Modifier.requiredSize(600.dp)) {
                    when {
                        orientation && allianceColor == Constants.AllianceColor.BLUE -> {
                            Image(
                                painter = painterResource(id = R.drawable.blue_map_2),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                            MapButtons(
                                paddingValues = listOf(
                                    310.dp to 415.dp,
                                    310.dp to 230.dp,
                                    310.dp to 44.dp,
                                    150.dp to 44.dp
                                ),
                                selected = selected,
                                onSelect = { selected = it }
                            )
                        }

                        !orientation && allianceColor == Constants.AllianceColor.BLUE -> {
                            Image(
                                painter = painterResource(id = R.drawable.blue_map_1),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                            MapButtons(
                                paddingValues = listOf(
                                    190.dp to 117.dp,
                                    190.dp to 302.dp,
                                    190.dp to 490.dp,
                                    350.dp to 490.dp
                                ),
                                selected = selected,
                                onSelect = { selected = it }
                            )
                        }

                        orientation && allianceColor == Constants.AllianceColor.RED -> {
                            Image(
                                painter = painterResource(id = R.drawable.red_map_2),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                            MapButtons(
                                paddingValues = listOf(
                                    185.dp to 415.dp,
                                    185.dp to 230.dp,
                                    185.dp to 49.dp,
                                    348.dp to 49.dp
                                ),
                                selected = selected,
                                onSelect = { selected = it }
                            )
                        }

                        !orientation && allianceColor == Constants.AllianceColor.RED -> {
                            Image(
                                painter = painterResource(id = R.drawable.red_map_1),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                            MapButtons(
                                paddingValues = listOf(
                                    310.dp to 130.dp,
                                    310.dp to 300.dp,
                                    310.dp to 486.dp,
                                    150.dp to 486.dp
                                ),
                                selected = selected,
                                onSelect = { selected = it }
                            )
                        }
                    }
                }
            }
            Button(
                onClick = { selected = 0 },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(
                        id = if (selected == 0) R.color.selected_start else R.color.light_gray
                    ),
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 50.dp, end = 50.dp, bottom = 10.dp)
            ) {
                Text("No Show", style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold))
            }
        }
    }

    @Composable
    fun MapButtons(paddingValues: List<Pair<Dp, Dp>>, selected: Int?, onSelect: (Int) -> Unit) {
        for (i in 1..4) Button(
            onClick = { onSelect(i) },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(
                    id = if (selected == i) R.color.selected_start else R.color.light_gray
                ),
                contentColor = Color.Black
            ),
            contentPadding = PaddingValues(horizontal = 40.dp, vertical = 10.dp),
            modifier = Modifier.padding(
                start = paddingValues[i - 1].first, top = paddingValues[i - 1].second
            )
        ) {
            Text(
                "$i",
                style = LocalTextStyle.current.copy(
                    fontSize = 25.sp, fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
