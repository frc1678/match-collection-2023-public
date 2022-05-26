// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.Manifest
import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.mode_collection_select_activity.*

// Activity for selecting objective or subjective mode.
class ModeCollectionSelectActivity : CollectionActivity() {
    // Initialize button onClickListeners for the objective and subjective mode selection buttons.
    // When clicked, buttons set collection_mode and start MatchInformationInputActivity.kt.
    private fun initButtonOnClicks() {
        btn_objective_collection_select.setOnClickListener {
            collection_mode = Constants.ModeSelection.OBJECTIVE
            startMatchInformationInputActivity()
        }
        btn_subjective_collection_select.setOnClickListener {
            collection_mode = Constants.ModeSelection.SUBJECTIVE
            startMatchInformationInputActivity()
        }
    }

    // Create the intent to start the respective mode activity.
    private fun startMatchInformationInputActivity() {
        putIntoStorage(context = this, key = "collection_mode", value = collection_mode)
        // Start respective mode activity.
        when (collection_mode) {
            Constants.ModeSelection.OBJECTIVE -> {
                finish()
                startActivity(
                    Intent(this, MatchInformationInputActivity::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(
                        this,
                        btn_objective_collection_select, "proceed_button"
                    ).toBundle()
                )
            }
            Constants.ModeSelection.SUBJECTIVE -> {
                finish()
                startActivity(
                    Intent(this, MatchInformationInputActivity::class.java),
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
            collection_mode = when (retrieveFromStorage(context = this, key = "collection_mode")) {
                Constants.ModeSelection.SUBJECTIVE.toString() ->
                    Constants.ModeSelection.SUBJECTIVE
                Constants.ModeSelection.OBJECTIVE.toString() ->
                    Constants.ModeSelection.OBJECTIVE
                else -> return
            }
            startMatchInformationInputActivity()
        }
        initButtonOnClicks()
    }
}
