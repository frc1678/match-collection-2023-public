// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.KeyEvent
import com.frc1678.match_collection.Constants.Companion.PREVIOUS_SCREEN
import com.github.sumimakito.awesomeqr.AwesomeQRCode
import kotlinx.android.synthetic.main.qr_generate_activity.*
import org.yaml.snakeyaml.Yaml
import java.io.BufferedWriter
import java.io.FileWriter
import java.util.regex.Pattern
import kotlin.text.Regex.Companion.escape

// Activity to display QR code of data collected in the match.
class QRGenerateActivity : CollectionActivity() {
    // Define regex to validate that QR only contains acceptable characters.
    private var regex: Pattern = Pattern.compile("[A-Z0-9" + escape("$%*+-./#^ ") + "]+")

    // Read YAML schema file and return its contents as a HashMap.
    private fun schemaRead(context: Context): HashMap<String, HashMap<String, Any>> {
        val inputStream = context.resources.openRawResource(R.raw.match_collection_qr_schema)
        return Yaml().load(inputStream) as HashMap<String, HashMap<String, Any>>
    }

    // Write message to a text file in the specified directory.
    private fun writeToFile(fileName: String, message: String) {
        val file = BufferedWriter(
            FileWriter(
                "/storage/emulated/0/${Environment.DIRECTORY_DOWNLOADS}/$fileName.txt", false
            )
        )
        file.write("$message\n")
        file.close()
    }

    // Generate and display QR.
    fun displayQR(contents: String) {
        AwesomeQRCode.Renderer().contents(contents)
            .size(800).margin(20).dotScale(dataDotScale = 1f)
            .renderAsync(object : AwesomeQRCode.Callback {
                override fun onRendered(renderer: AwesomeQRCode.Renderer, bitmap: Bitmap) {
                    this@QRGenerateActivity.runOnUiThread {
                        iv_display_qr.setImageBitmap(bitmap)
                    }
                }

                override fun onError(renderer: AwesomeQRCode.Renderer, e: Exception) {
                    e.printStackTrace()
                }
            })
    }

    // Initialize proceed button to increment and store match number and return to
    // MatchInformationInputActivity.kt when clicked.
    private fun initProceedButton(isAlreadyCompressed: Boolean, qrContents: String) {
        btn_proceed_new_match.setOnClickListener {
            if (!isAlreadyCompressed) {

                // Write compressed QR string to file.
                // File name is dependent on mode (objective or subjective).
                val fileName = if (collection_mode == Constants.ModeSelection.OBJECTIVE) {
                    "${match_number}_${team_number}_${getSerialNum(context = this)}_$timestamp"
                } else {
                    "${match_number}_${getSerialNum(context = this)}_$timestamp"
                }
                writeToFile(fileName = fileName, message = qrContents)
                match_number += 1
            }
            putIntoStorage(context = this, key = "match_number", value = match_number)
            val intent = Intent(this, MatchInformationInputActivity::class.java)
                .putExtra(PREVIOUS_SCREEN, Constants.Screens.QR_GENERATE)
            startActivity(
                intent, ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    btn_proceed_new_match, "proceed_button"
                ).toBundle()
            )
        }
    }

/*     Begin intent used in onKeyLongPress to go back to a pprevious activity depending
     on your mode and starting position.*/
private fun intentToPreviousActivity() {
    is_teleop_activated = true
    is_match_time_ended = true
    val intent = if (collection_mode == Constants.ModeSelection.OBJECTIVE) {
        if (starting_position.toString() != "ZERO") {
            Intent(this, CollectionObjectiveActivity::class.java)
        } else {
            Intent(this, MatchInformationEditActivity::class.java)
        }
    } else {
        Intent(this, MatchInformationInputActivity::class.java)
    }.putExtra(PREVIOUS_SCREEN, Constants.Screens.QR_GENERATE)

    startActivity(
        intent,
        ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
    )
}

    // Restart app from MatchInformationInputActivity.kt when back button is long pressed.
    override fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
        intentToPreviousActivity()
        return super.onKeyLongPress(keyCode, event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qr_generate_activity)
        if (starting_position == Constants.StartingPosition.ZERO) preloaded = Constants.Preloaded.N

        timestamp = System.currentTimeMillis() / 1000
        val compressedQR = intent.extras?.getString(Constants.COMPRESSED_QR_TAG)

        if (compressedQR == null) {
            // Populate QR code content and display QR if valid (only contains compression characters).
            val qrContents = compress(schema = schemaRead(context = this))
            initProceedButton(false, qrContents)
            if (regex.matcher(qrContents).matches()) {
                displayQR(contents = qrContents)
            } else {
                AlertDialog.Builder(this).setMessage(R.string.error_qr).show()
            }
        } else {
            initProceedButton(true, "")

            displayQR(compressedQR)
        }
    }
}
