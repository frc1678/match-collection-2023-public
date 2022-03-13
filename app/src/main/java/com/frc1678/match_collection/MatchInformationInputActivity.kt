// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.app.ActivityOptions
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.id_scout_dialog.*
import kotlinx.android.synthetic.main.match_information_input_activity_objective.*
import java.io.File
import java.io.FileReader
import java.lang.Integer.parseInt

// Activity to input the match information before the start of the match.
class MatchInformationInputActivity : MatchInformationActivity() {
    private var leftToggleButtonColor: Int = 0
    private var rightToggleButtonColor: Int = 0
    private var leftToggleButtonColorDark: Int = 0
    private var rightToggleButtonColorDark: Int = 0

    private lateinit var leftToggleButton: Button
    private lateinit var rightToggleButton: Button

    /** Static storage of the match schedule to avoid retrieving from storage multiple times.
     * Keep in mind that any changes made to the file will require a restart of the app or proceeding
     * to the next match for changes to apply. */
    object MatchSchedule {

        /**
         * The contents of the match schedule. An example of a match schedule can be found
         * [here](https://github.com/frc1678/Cardinal/blob/main/cardinal/api/hardcoded_test_data/match_schedule.json).
         * */
        var contents: JsonObject? = null

        private val file = File("/storage/emulated/0/${Environment.DIRECTORY_DOWNLOADS}/match_schedule.json")

        /** Initializes [`contents`][contents] with the JSON data from the [file]. This should only be called once.
         * After calling this, use `MatchSchedule.contents` to access the data.
         * @return Whether the reading was successful. */
        fun read(): Boolean {
            try { contents = JsonParser.parseReader(FileReader(file)).asJsonObject }
            catch (e: Exception) { return false }
            return true
        }

        /** Whether the match schedule file exists. */
        val fileExists: Boolean
            get() = file.exists()
    }

    /**
     * Static storage of the scout orders list to avoid retrieving rom storage multiple times. Keep
     * in mind that any changes made to the file will require a restart of the app or proceeding to
     * the next match for changes to apply.
     */
    object ScoutOrders {
        /**
         * The contents of the scout orders list. The format of the JSON object should be match
         * number, as a string, to arrays of integers denoting the orders.
         */
        private var contents: JsonObject? = null

        private val file = File(
            "/storage/emulated/0/${Environment.DIRECTORY_DOWNLOADS}/scout_orders.json"
        )

        /**
         * Initializes [`contents`][contents] with the JSON data from the [file]. This should only
         * be called once.
         * @return Whether the reading was successful.
         */
        fun read(): Boolean {
            try { contents = JsonParser.parseReader(FileReader(file)).asJsonObject }
            catch (e: Exception) { return false }
            return true
        }

        /**
         * Whether the [scout orders file][file] exists.
         */
        val fileExists: Boolean
            get() = file.exists()

        /**
         * Gets the new assignment for the scout given the match number and the scout ID. Requires
         * [`read()`][read] to have already been called.
         */
        fun getNewAssignment(matchNumber: String, scoutID: Int): Int? {
            if (contents == null) return null
            return (
                    contents!!.getAsJsonArray(matchNumber)[scoutID - 1].asInt - 1
                    ) % 6
        }
    }


    /**
     * Assign team number and alliance color for Objective Scout based on the team index given by
     * [`getNewAssignment()`][ScoutOrders.getNewAssignment].
     */
    private fun assignTeamObjective(
        teamInput: EditText,
        teamIndex: Int?,
        matchNumber: String
    ) {
        val team = MatchSchedule.contents!!
            .getAsJsonObject(matchNumber)
            ?.getAsJsonArray("teams")
            ?.get(teamIndex ?: return)?.asJsonObject
            ?: return
        teamInput.setText(team.get("number")!!.asString)
        alliance_color =
            if (team.get("color")?.asString == "red") {
                switchBorderToRedToggle()
                Constants.AllianceColor.RED
            } else {
                switchBorderToBlueToggle()
                Constants.AllianceColor.BLUE
            }
    }

    /**
     * Assign team numbers for Subjective Scout based on alliance color.
     */
    private fun assignTeamsSubjective(
        teamInput: EditText, allianceColor: Constants.AllianceColor,
        matchNumber: String, iterationNumber: Int
    ) {
        val team = MatchSchedule.contents!!
            .get(matchNumber)?.asJsonObject
            ?.get("teams")?.asJsonArray
            ?.get(iterationNumber + if (allianceColor == Constants.AllianceColor.RED) 3 else 0)?.asJsonObject
            ?: return
        teamInput.setText(team.get("number").asString)
    }

    /**
     * Automatically assign team number(s) based on collection mode.
     */
    private fun autoAssignTeamInputsGivenMatch() {
            if (assign_mode == Constants.AssignmentMode.OVERRIDE) return
            if (MatchSchedule.fileExists and ScoutOrders.fileExists) {
                if (assign_mode == Constants.AssignmentMode.AUTOMATIC_ASSIGNMENT) {
                    // Assign three scouts per robot based on the scout order list in Objective
                    // Match Collection.
                    if (collection_mode == Constants.ModeSelection.OBJECTIVE) {
                        if (scout_id.isNotEmpty() and (scout_id != (Constants.NONE_VALUE))) {
                            assignTeamObjective(
                                teamInput = et_team_one,
                                teamIndex = ScoutOrders.getNewAssignment(
                                    matchNumber = et_match_number.text.toString(),
                                    scoutID = scout_id.toInt()
                                ),
                                matchNumber = et_match_number.text.toString()
                            )
                        }
                    } else {
                        // Assign an alliance to a scout based on alliance color in Subjective Match
                        // Collection.
                        var iterationNumber = 0
                        listOf<EditText>(et_team_one, et_team_two, et_team_three).forEach {
                            assignTeamsSubjective(
                                teamInput = it,
                                allianceColor = alliance_color,
                                matchNumber = et_match_number.text.toString(),
                                iterationNumber = iterationNumber
                            )
                            iterationNumber++
                        }

                    }
                } else {
                    et_team_one.setText("")
                    et_team_two.setText("")
                    et_team_three.setText("")

                    AlertDialog.Builder(this).setMessage(R.string.error_files_missing).show()
                }

                if (collection_mode == Constants.ModeSelection.OBJECTIVE) {
                    if ((btn_scout_id.text == "Scout ID: NONE") or (scout_id.toIntOrNull() == null)) {
                        AlertDialog.Builder(this).setMessage(R.string.error_scout_id_not_found)
                            .show()
                    }
                }
            }
    }

    // Assign team number based on collection mode when match number is changed.
    private fun initMatchNumberTextChangeListener() {
        et_match_number.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if (checkInputNotEmpty(et_match_number)) {
                    if (et_match_number.text.toString() != "") {
                        if (MatchSchedule.fileExists) {
                            if (parseInt(et_match_number.text.toString()) > MatchSchedule.contents!!.keySet()!!.size) {
                                et_team_one.setText("")
                                et_team_two.setText("")
                                et_team_three.setText("")
                            } else {
                                autoAssignTeamInputsGivenMatch()
                                match_number = parseInt(et_match_number.text.toString())
                            }
                        }
                    }
                } else {
                    et_team_one.setText("")
                    et_team_two.setText("")
                    et_team_three.setText("")
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })
    }

    // Create an alliance color toggle button given its specifications.
    private fun createToggleButton(
        isBordered: Boolean, toggleButton: Button,
        toggleButtonColor: Int, toggleButtonColorDark: Int
    ) {
        val backgroundDrawable = GradientDrawable()

        if (isBordered) {
            backgroundDrawable.setStroke(10, toggleButtonColorDark)
        }

        backgroundDrawable.setColor(toggleButtonColor)
        backgroundDrawable.cornerRadius = 10f
        toggleButton.background = backgroundDrawable
    }

    // Update alliance color toggle button with unselected backgrounds.
    private fun resetBackground() {
        // Create the unselected alliance color left toggle.
        createToggleButton(
            isBordered = false,
            toggleButton = leftToggleButton,
            toggleButtonColor = leftToggleButtonColor,
            toggleButtonColorDark = leftToggleButtonColorDark
        )

        // Create the unselected alliance color right toggle.
        createToggleButton(
            isBordered = false,
            toggleButton = rightToggleButton,
            toggleButtonColor = rightToggleButtonColor,
            toggleButtonColorDark = rightToggleButtonColorDark
        )
    }

    // Apply border to red alliance toggle when red alliance selected.
    private fun switchBorderToRedToggle() {
        resetBackground()

        // Create selected red toggle.
        createToggleButton(
            isBordered = true,
            toggleButton = rightToggleButton,
            toggleButtonColor = rightToggleButtonColor,
            toggleButtonColorDark = rightToggleButtonColorDark
        )
    }

    // Apply border to blue alliance toggle when blue alliance is selected.
    private fun switchBorderToBlueToggle() {
        resetBackground()

        // Create selected blue toggle.
        createToggleButton(
            isBordered = true,
            toggleButton = leftToggleButton,
            toggleButtonColor = leftToggleButtonColor,
            toggleButtonColorDark = leftToggleButtonColorDark
        )
    }

    // Initialize alliance toggle button onClickListeners.
    private fun initToggleButtons() {
        rightToggleButtonColor = ContextCompat.getColor(this, R.color.alliance_red_light)
        leftToggleButtonColor = ContextCompat.getColor(this, R.color.alliance_blue_light)
        rightToggleButtonColorDark = ContextCompat.getColor(this, R.color.alliance_red_dark)
        leftToggleButtonColorDark = ContextCompat.getColor(this, R.color.alliance_blue_dark)
        leftToggleButton = left_toggle_button
        rightToggleButton = right_toggle_button

        resetBackground()

        when (retrieveFromStorage(context = this, key = "alliance_color")) {
            Constants.AllianceColor.BLUE.toString() -> {
                switchBorderToBlueToggle()
                alliance_color = Constants.AllianceColor.BLUE
            }
            Constants.AllianceColor.RED.toString() -> {
                switchBorderToRedToggle()
                alliance_color = Constants.AllianceColor.RED
            }
        }

        // Set onClickListeners to set alliance color when in objective collection mode.
        leftToggleButton.setOnClickListener {
            if (collection_mode == Constants.ModeSelection.OBJECTIVE) {
                alliance_color = Constants.AllianceColor.BLUE
                switchBorderToBlueToggle()
            }
        }
        rightToggleButton.setOnClickListener {
            if (collection_mode == Constants.ModeSelection.OBJECTIVE) {
                alliance_color = Constants.AllianceColor.RED
                switchBorderToRedToggle()
            }
        }

        // Set onLongClickListeners to set alliance color in subjective collection mode when long clicked.
        leftToggleButton.setOnLongClickListener {
            if (collection_mode == Constants.ModeSelection.SUBJECTIVE) {
                alliance_color = Constants.AllianceColor.BLUE
                putIntoStorage(context = this, key = "alliance_color", value = alliance_color)
                autoAssignTeamInputsGivenMatch()
                switchBorderToBlueToggle()
            }
            return@setOnLongClickListener true
        }
        rightToggleButton.setOnLongClickListener {
            if (collection_mode == Constants.ModeSelection.SUBJECTIVE) {
                alliance_color = Constants.AllianceColor.RED
                putIntoStorage(context = this, key = "alliance_color", value = alliance_color)
                autoAssignTeamInputsGivenMatch()
                switchBorderToRedToggle()
            }
            return@setOnLongClickListener true
        }
    }

    // Return a list of the scout IDs based on NUMBER_OF_ACTIVE_SCOUTS defined in Constants.kt.
    private fun scoutIdContentsList(): ArrayList<Any> {
        val scoutIdContents = ArrayList<Any>()
        scoutIdContents.add(Constants.NONE_VALUE)
        (1..Constants.NUMBER_OF_ACTIVE_SCOUTS).forEach { scoutIdContents.add(it) }
        return scoutIdContents
    }

    // Initialize onLongClickListener for the scout ID button to prompt for a scout ID input.
    private fun initScoutIdLongClick() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.id_scout_dialog)

        // Set scout ID spinner to previously set scout ID from internal storage.
        if (this.getSharedPreferences("PREFS", 0).contains("scout_id")) {
            btn_scout_id.text = getString(
                R.string.btn_scout_id_message,
                retrieveFromStorage(context = this, key = "scout_id")
            )
            scout_id = retrieveFromStorage(context = this, key = "scout_id")
        }

        btn_scout_id.setOnLongClickListener {
            dialog.show()
            val adapter = ArrayAdapter(
                this, R.layout.spinner_text_view,
                scoutIdContentsList()
            )
            dialog.lv_scout_id_view.adapter = adapter
            dialog.lv_scout_id_view.setOnItemClickListener { _, _, position, _ ->
                btn_scout_id.text = getString(
                    R.string.btn_scout_id_message,
                    scoutIdContentsList()[position].toString()
                )
                // Set scout ID and save it to internal storage.
                scout_id = scoutIdContentsList()[position].toString()
                putIntoStorage(context = this, key = "scout_id", value = scout_id)
                autoAssignTeamInputsGivenMatch()
                dialog.dismiss()
            }
            return@setOnLongClickListener true
        }
    }

    // Initialize the adapter and onItemSelectedListener for assignment mode input.
    private fun initAssignModeSpinner() {
        when (retrieveFromStorage(context = this, key = "assignment_mode")) {
            "0" ->
                assign_mode = Constants.AssignmentMode.AUTOMATIC_ASSIGNMENT
            "1" ->
                assign_mode = Constants.AssignmentMode.OVERRIDE
        }

        val adapter = ArrayAdapter(
            this, R.layout.spinner_text_view,
            arrayOf(getString(R.string.btn_assignment), getString(R.string.btn_override))
        )
        spinner_assign_mode.adapter = adapter

        // Set assignment mode spinner to previously set scout ID from internal storage.
        if (this.getSharedPreferences("PREFS", 0).contains("assignment_mode")) {
            spinner_assign_mode.setSelection(
                parseInt(
                    retrieveFromStorage(
                        context = this,
                        key = "assignment_mode"
                    )
                )
            )
        }

        spinner_assign_mode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Save selected assignment mode into internal storage.
                putIntoStorage(
                    context = this@MatchInformationInputActivity,
                    key = "assignment_mode",
                    value = position
                )

                // Automatically assign teams if in automatic assignment mode and disable user input.
                // Otherwise, enable team number edit texts and alliance color toggles.
                if (position == 0) {
                    assign_mode = Constants.AssignmentMode.AUTOMATIC_ASSIGNMENT
                    et_team_one.isEnabled = false
                    et_team_two.isEnabled = false
                    et_team_three.isEnabled = false
                    if (collection_mode == Constants.ModeSelection.OBJECTIVE) {
                        left_toggle_button.isEnabled = false
                        right_toggle_button.isEnabled = false
                    }
                    autoAssignTeamInputsGivenMatch()
                } else {
                    assign_mode = Constants.AssignmentMode.OVERRIDE
                    et_team_one.isEnabled = true
                    et_team_two.isEnabled = true
                    et_team_three.isEnabled = true
                    if (collection_mode == Constants.ModeSelection.OBJECTIVE) {
                        left_toggle_button.isEnabled = true
                        right_toggle_button.isEnabled = true
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                assign_mode = Constants.AssignmentMode.NONE
            }
        }
    }

    // Transition into the next activity and set timestamp for specific match.
    private fun startMatchActivity() {
        match_number = parseInt(et_match_number.text.toString())

        putIntoStorage(context = this, key = "match_number", value = match_number)
        putIntoStorage(context = this, key = "alliance_color", value = alliance_color)

        if (collection_mode == Constants.ModeSelection.OBJECTIVE) {
            team_number = et_team_one.text.toString()
            val intent = Intent(this, StartingPositionObjectiveActivity::class.java)
            startActivity(
                intent, ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    btn_proceed_match_start, "proceed_button"
                ).toBundle()
            )
        } else {
            val intent = Intent(this, CollectionSubjectiveActivity::class.java)
            intent.putExtra("team_one", et_team_one.text.toString())
                .putExtra("team_two", et_team_two.text.toString())
                .putExtra("team_three", et_team_three.text.toString())
            startActivity(
                intent, ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    btn_proceed_match_start, "proceed_button"
                ).toBundle()
            )
        }
    }

    // Initialize the onClickListener for the proceed button to go the next screen if inputs pass safety checks.
    private fun initProceedButton() {
        btn_proceed_match_start.setOnClickListener { view ->
            if (safetyCheck(view = view)) {
                startMatchActivity()
            }
        }
    }

    // Begin intent used in onKeyLongPress to restart app from ModeCollectionSelectActivity.kt.
    // Remove collection mode from internal storage.
    private fun intentToMatchInput() {
        this.getSharedPreferences("PREFS", 0).edit().remove("collection_mode").apply()
        startActivity(
            Intent(this, ModeCollectionSelectActivity::class.java),
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    // Restart app from ModeCollectionSelectActivity.kt when back button is long pressed.
    override fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder(this).setMessage(R.string.error_back_reset)
                .setPositiveButton("Yes") { _, _ -> intentToMatchInput() }
                .show()
        }
        return super.onKeyLongPress(keyCode, event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (collection_mode == Constants.ModeSelection.OBJECTIVE){
            setContentView(R.layout.match_information_input_activity_objective)
            initScoutIdLongClick()
        } else if (collection_mode == Constants.ModeSelection.SUBJECTIVE){
            setContentView(R.layout.match_information_input_activity_subjective)
        }

        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        et_match_number.setText(retrieveFromStorage(context = this, key = "match_number"))
        tv_version_number.text = getString(R.string.tv_version_num, match_collection_version_number)

        serial_number = getSerialNum(context = this)

        resetCollectionReferences()
        resetStartingReferences()

        MatchSchedule.read()
        ScoutOrders.read()

        initToggleButtons()
        initScoutNameSpinner(context = this, spinner = spinner_scout_name)
        initMatchNumberTextChangeListener()
        initProceedButton()
        initAssignModeSpinner()
    }
}
