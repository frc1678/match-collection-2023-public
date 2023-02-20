package com.frc1678.match_collection

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frc1678.match_collection.Constants.Companion.PREVIOUS_SCREEN
import kotlinx.android.synthetic.main.starting_game_pieces_activity.btn_proceed_game_piece
import kotlinx.android.synthetic.main.starting_game_pieces_activity.btn_switch_orientation_game_pieces
import kotlinx.android.synthetic.main.starting_game_pieces_activity.compose_map

class StartingGamePieceActivity : CollectionActivity() {

    // Initiates the onClicks for all the buttons
    private fun initOnClicks() {
        // Changes the orientation of the map and calls setMapPicture
        btn_switch_orientation_game_pieces.setOnClickListener {
            orientation = !orientation
        }

        // Moves onto the next screen if you have inputted all the information
        btn_proceed_game_piece.setOnClickListener {
            // If all game pieces have been selected then proceed to CollectionSubjectiveActivity.kt
            // Otherwise create warning message
            if (Constants.GamePiecePositions.N in gamePiecePositionList) {
                AlertDialog.Builder(this)
                    .setTitle("Warning! You have not selected the type for all of the game pieces!")
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.cancel()
                    }.setPositiveButton("Proceed") { _: DialogInterface, _: Int ->
                        goToNextActivity()
                    }.show()
            } else {
                // Add alliance teams to the intent to be used in MatchInformationEditActivity.kt.
                goToNextActivity()
            }
        }
    }

    // Goes to next activity
    private fun goToNextActivity() {
        startActivity(
            Intent(this, CollectionSubjectiveActivity::class.java).putExtras(intent)
                .putExtra(PREVIOUS_SCREEN, Constants.Screens.STARTING_GAME_PIECE),
            ActivityOptions.makeSceneTransitionAnimation(
                this,
                btn_proceed_game_piece, "proceed_button"
            ).toBundle()
        )
    }

    // Begin intent used in onKeyLongPress to restart app from MatchInformationInputActivity.kt.
    private fun intentToPreviousActivity() = startActivity(
        Intent(this, MatchInformationInputActivity::class.java)
            .putExtra(PREVIOUS_SCREEN, Constants.Screens.STARTING_GAME_PIECE)
            .putExtras(intent),
        ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
    )

    // Restart app from MatchInformationInputActivity.kt when back button is long pressed.
    override fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder(this).setMessage(R.string.error_back_reset)
                .setPositiveButton("Yes") { _, _ -> intentToPreviousActivity() }
                .show()
        }
        return super.onKeyLongPress(keyCode, event)
    }

    // Creates the screen and sets everything up.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.starting_game_pieces_activity)
        compose_map.setContent { MapContent() }
        initOnClicks()
    }

    @Composable
    fun MapContent() {
        var gamePieces by remember { mutableStateOf(gamePiecePositionList.toList()) }
        LaunchedEffect(gamePieces) {
            gamePiecePositionList = gamePieces.toMutableList()
        }
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.requiredSize(600.dp)) {
                when {
                    orientation && allianceColor == Constants.AllianceColor.BLUE -> Map(
                        drawableId = R.drawable.blue_up_game_pieces,
                        paddingValues = listOf(
                            100.dp to 450.dp,
                            100.dp to 325.dp,
                            100.dp to 200.dp,
                            100.dp to 75.dp
                        ),
                        gamePieces = gamePieces,
                        setGamePieces = { gamePieces = it }
                    )

                    !orientation && allianceColor == Constants.AllianceColor.BLUE -> Map(
                        drawableId = R.drawable.blue_down_game_pieces,
                        paddingValues = listOf(
                            400.dp to 75.dp,
                            400.dp to 200.dp,
                            400.dp to 325.dp,
                            400.dp to 450.dp
                        ),
                        gamePieces = gamePieces,
                        setGamePieces = { gamePieces = it }
                    )

                    orientation && allianceColor == Constants.AllianceColor.RED -> Map(
                        drawableId = R.drawable.red_up_game_pieces,
                        paddingValues = listOf(
                            400.dp to 450.dp,
                            400.dp to 325.dp,
                            400.dp to 200.dp,
                            400.dp to 75.dp
                        ),
                        gamePieces = gamePieces,
                        setGamePieces = { gamePieces = it }
                    )

                    !orientation && allianceColor == Constants.AllianceColor.RED -> Map(
                        drawableId = R.drawable.red_down_game_pieces,
                        paddingValues = listOf(
                            100.dp to 75.dp,
                            100.dp to 200.dp,
                            100.dp to 325.dp,
                            100.dp to 450.dp
                        ),
                        gamePieces = gamePieces,
                        setGamePieces = { gamePieces = it }
                    )
                }
            }
        }
    }

    @Composable
    fun Map(
        @DrawableRes drawableId: Int,
        paddingValues: List<Pair<Dp, Dp>>,
        gamePieces: List<Constants.GamePiecePositions>,
        setGamePieces: (List<Constants.GamePiecePositions>) -> Unit
    ) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        for (i in 1..4) Button(
            onClick = {
                setGamePieces(gamePieces.toMutableList().apply { this[i - 1] = this[i - 1].next() })
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(
                    id = when (gamePieces[i - 1]) {
                        Constants.GamePiecePositions.N -> R.color.light_gray
                        Constants.GamePiecePositions.O -> R.color.cone_yellow
                        Constants.GamePiecePositions.U -> R.color.cube_purple
                    }
                ),
                contentColor = Color.Black
            ),
            contentPadding = PaddingValues(horizontal = 40.dp, vertical = 10.dp),
            modifier = Modifier.padding(
                start = paddingValues[i - 1].first, top = paddingValues[i - 1].second
            )
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "$i",
                    style = LocalTextStyle.current.copy(
                        fontSize = 25.sp, fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    when (gamePieces[i - 1]) {
                        Constants.GamePiecePositions.N -> "NONE"
                        Constants.GamePiecePositions.O -> "CONE"
                        Constants.GamePiecePositions.U -> "CUBE"
                    },
                    style = LocalTextStyle.current.copy(
                        fontSize = 16.sp, fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }

    private fun Constants.GamePiecePositions.next() = when (this) {
        Constants.GamePiecePositions.N -> Constants.GamePiecePositions.O
        Constants.GamePiecePositions.O -> Constants.GamePiecePositions.U
        Constants.GamePiecePositions.U -> Constants.GamePiecePositions.N
    }
}
