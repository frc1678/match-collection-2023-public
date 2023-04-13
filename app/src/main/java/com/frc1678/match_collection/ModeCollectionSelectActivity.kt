// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.Manifest
import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import androidx.core.app.ActivityCompat
import com.frc1678.match_collection.Constants.Companion.PREVIOUS_SCREEN
import kotlinx.android.synthetic.main.match_information_input_activity_objective.*
import kotlinx.android.synthetic.main.mode_collection_select_activity.*
import java.io.File

// Activity for selecting objective or subjective mode.
class ModeCollectionSelectActivity : CollectionActivity() {
    // Initialize button onClickListeners for the objective and subjective mode selection buttons.
    // When clicked, buttons set collection_mode and start MatchInformationInputActivity.kt.
    private fun initButtonOnClicks() {
        btn_objective_collection_select.setOnClickListener {
            collectionMode = Constants.ModeSelection.OBJECTIVE
            startMatchInformationInputActivity()
        }
        btn_subjective_collection_select.setOnClickListener {
            collectionMode = Constants.ModeSelection.SUBJECTIVE
            startMatchInformationInputActivity()
        }
    }

    // Create the intent to start the respective mode activity.
    private fun startMatchInformationInputActivity() {
        putIntoStorage(context = this, key = "collection_mode", value = collectionMode)
        // Start respective mode activity.
        when (collectionMode) {
            Constants.ModeSelection.OBJECTIVE -> {
                finish()
                startActivity(
                    Intent(this, MatchInformationInputActivity::class.java)
                        .putExtra(PREVIOUS_SCREEN, Constants.Screens.MODE_COLLECTION_SELECT),
                    ActivityOptions.makeSceneTransitionAnimation(
                        this,
                        btn_objective_collection_select, "proceed_button"
                    ).toBundle()
                )
            }
            Constants.ModeSelection.SUBJECTIVE -> {
                finish()
                startActivity(
                    Intent(this, MatchInformationInputActivity::class.java)
                        .putExtra(PREVIOUS_SCREEN, Constants.Screens.MODE_COLLECTION_SELECT),
                    ActivityOptions.makeSceneTransitionAnimation(
                        this,
                        btn_subjective_collection_select, "proceed_button"
                    ).toBundle()
                )
            }
            else -> return
        }
    }

    // Continually check write and read external storage and read phone state permissions
    // to request permissions if not granted.
    // Continue to prompt user to accept usage of permissions until they are accepted.
    override fun onResume() {
        super.onResume()
        if ((ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) or ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) or ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE))
            != PackageManager.PERMISSION_GRANTED
        ) {
            try {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE
                    ),
                    100
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        File("/storage/emulated/0/${Environment.DIRECTORY_DOWNLOADS}").mkdirs()
        setContentView(R.layout.mode_collection_select_activity)

        // If the collection mode exists on the device, retrieve it and skip to its match input screen.
        // Otherwise, don't skip and prompt for the mode selection input.
        if (this.getSharedPreferences(
                "PREFS",
                0
            ).contains("collection_mode") and (retrieveFromStorage(
                context = this,
                key = "collection_mode"
            ) != Constants.ModeSelection.NONE.toString())
        ) {
            collectionMode = when (retrieveFromStorage(context = this, key = "collection_mode")) {
                Constants.ModeSelection.SUBJECTIVE.toString() ->
                    Constants.ModeSelection.SUBJECTIVE
                Constants.ModeSelection.OBJECTIVE.toString() ->
                    Constants.ModeSelection.OBJECTIVE
                else -> return
            }
            startMatchInformationInputActivity()
        }
        tv_version_number_mode_select.text = getString(R.string.tv_version_num, Constants.VERSION_NUMBER)
        tv_event_name.text = getString(R.string.tv_event_name, Constants.EVENT_KEY)
        initButtonOnClicks()
    }
}
