package com.frc1678.match_collection

import android.app.AlertDialog
import android.os.Bundle
import android.os.PersistableBundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val CONE_COLOR = Color.hsl(
    57f,
    .9f,
    .5f
)
val CUBE_COLOR = Color.hsl(
    260f,
    .6f,
    0.5f
)

enum class GamePiece {
    Yes,
    Supercharged;

    val color: Color
        get() = when (this) {
            Yes -> Color.Green
            Supercharged -> Color.Red
        }

}

class PlayoffActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var scoringLocation: List<List<List<GamePiece?>>> by remember {
                mutableStateOf(List(3) { List(3) { List(3) { null } } })
            }

            fun setGamePiece(x: Int, y: Int, z: Int, gamePiece: GamePiece?) {
                scoringLocation = scoringLocation.run {
                    val state = this.toMutableList()
                    val grid = state[x].toMutableList()
                    val row = grid[y].toMutableList()
                    row[z] = gamePiece
                    grid[y] = row
                    state[x] = grid
                    state
                }
            }

            /**
             * Gets the next gamepiece in the cycle.
             * @param gamePiece The current game piece.
             * @param grid The row of the game piece.
             * @param row The row of the game piece.
             * @param column The column of the game piece.
             */
            fun getNextGamePiece(gamePiece: GamePiece?): GamePiece? {
                return when (gamePiece) {
                    null -> GamePiece.Yes
                    GamePiece.Yes -> GamePiece.Supercharged
                    GamePiece.Supercharged -> null
                }
            }

            /**
             * Function that goes to the next game piece.
             * Goes from cone to cube to null if on the low row.
             * Goes from the corresponding game piece to null if on the other rows.
             */
            fun cycleGamePiece(x: Int, y: Int, z: Int) {
                val gamePiece = scoringLocation[x][y][z]
                val nextGamePiece = getNextGamePiece(gamePiece)
                setGamePiece(x, y, z, nextGamePiece)

            }

            /**
             * Gets the total game pieces in a row across all grids.
             * Yes counts as 1 and Supercharged counts as 2
             */
            fun getRowTotal(row: Int): Int {
                return scoringLocation.sumOf { grid ->
                    grid[row].sumOf {
                        when (it) {
                            null -> 0
                            GamePiece.Yes -> 1
                            GamePiece.Supercharged -> 2
                        }.toInt()
                    }
                }
            }



            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Row {
                    Column(modifier = Modifier.height(300.dp)) {
                        Row(
                            modifier = Modifier.border(5.dp, Color.Black),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(5f)) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier
                                        .aspectRatio(3f)
                                        .padding(7.dp)
                                ) {
                                    for (grid in 0 until 3) {
                                        Column(
                                            modifier = Modifier
                                                .aspectRatio(1f)
                                        ) {
                                            for (row in 0 until 3) {
                                                Row(
                                                    modifier = Modifier
                                                        .aspectRatio(3f)
                                                ) {
                                                    for (node in 0 until 3) {
                                                        Column(
                                                            modifier = Modifier
                                                                .aspectRatio(1f)
                                                                .padding(3.dp)
                                                                .border(
                                                                    3.dp,
                                                                    when (row) {
                                                                        2 -> Color.Gray
                                                                        else -> if (node % 2 == 0) CONE_COLOR else CUBE_COLOR
                                                                    }

                                                                )
                                                                .clickable {
                                                                    cycleGamePiece(
                                                                        grid,
                                                                        row,
                                                                        node
                                                                    )
                                                                }
                                                                .background(
                                                                    scoringLocation[grid][row][node]?.color
                                                                        ?: Color.Transparent
                                                                ),
                                                            verticalArrangement = Arrangement.Center,
                                                            horizontalAlignment = Alignment.CenterHorizontally
                                                        ) {

                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Row {
                                    // High row total gamepieces
                                    Text(text = "High: ${getRowTotal(0)}", fontSize = 20.sp)
                                }
                                Row {
                                    // High row total gamepieces
                                    Text(text = "Mid: ${getRowTotal(1)}", fontSize = 20.sp)
                                }
                                Row {
                                    // High row total gamepieces
                                    Text(
                                        text = "Low: ${getRowTotal(2)}", fontSize = 20.sp
                                    )
                                }
                            }
                        }
                        Row {

                        }
                    }
                }

            }

        }
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder(this).setMessage(R.string.error_back_reset)
                .setPositiveButton("Yes") { _, _ -> super.onBackPressed() }
                .show()
        }
        return super.onKeyLongPress(keyCode, event)
    }

    override fun onBackPressed() {}
}
