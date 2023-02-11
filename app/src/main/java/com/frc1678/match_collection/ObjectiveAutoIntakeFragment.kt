package com.frc1678.match_collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.frc1678.match_collection.Constants.AllianceColor
import kotlinx.android.synthetic.main.collection_objective_auto_intake_fragment.view.*
import kotlinx.android.synthetic.main.starting_game_pieces_activity.*

/**
 * [Fragment] used for showing intake buttons in [ObjectiveAutoIntakeFragment]
 */
class ObjectiveAutoIntakeFragment : Fragment(R.layout.collection_objective_auto_intake_fragment) {

    /*
     The main view of this fragment.
     */
    private var mainView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainView = super.onCreateView(inflater, container, savedInstanceState)!!
        setContent()
        return mainView
    }

    /**
     * Parent activity of this fragment
     */
    private val collectionObjectiveActivity get() = activity as CollectionObjectiveActivity

    /**
     * This is the compose function that creates the layout for the compose view in collection_objective_auto_intake_fragment.
     */
    private fun setContent() {
        mainView!!.compose_view.setContent {
            /*
            Everything within this content view will also be inside a box.
             This allows us to overlap different objects, getting buttons on top of an image.
             */
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {

                /*
                This image view is behind everything else in the box
                and displays one of four images based on your alliance color and orientation.
                */
                Image(
                    painter = painterResource(
                        id = when {
                            (orientation && allianceColor == AllianceColor.BLUE) ->
                                R.drawable.blue_up_game_pieces
                            (orientation && allianceColor == AllianceColor.RED) ->
                                R.drawable.red_up_game_pieces
                            (!orientation && allianceColor == AllianceColor.BLUE) ->
                                R.drawable.blue_down_game_pieces
                            (!orientation && allianceColor == AllianceColor.RED) ->
                                R.drawable.red_down_game_pieces
                            else -> R.drawable.red_down_game_pieces
                        }
                    ), contentDescription = "Map with game pieces",
                    modifier = Modifier.size(950.dp)
                )

                /*
                A horizontal layout where the first objects are to the left and the last objects are to the right.
                */
                Row {
                    // Pushes the buttons right when the background is red_up_game_pieces
                    if (orientation && allianceColor == AllianceColor.RED) {
                        Spacer(modifier = Modifier.width(195.dp))
                    }
                    // Pushes the buttons right when the background is blue_down_game_pieces
                    if (!orientation && allianceColor == AllianceColor.BLUE) {
                        Spacer(modifier = Modifier.width(195.dp))
                    }

                    /*
                    A vertical layout where the first objects are the top and the last objects are on the bottom.
                     */
                    Column() {

                        // Pushes the buttons down when the background is red_down_game_pieces
                        if (!orientation && allianceColor == AllianceColor.RED) {
                            Spacer(modifier = Modifier.height(50.dp))
                        }
                        // Pushes the buttons down when the background is blue_down_game_pieces.
                        if (!orientation && allianceColor == AllianceColor.BLUE) {
                            Spacer(modifier = Modifier.height(60.dp))
                        }

                        // The first button
                        TextButton(

                            /*
                            The equivalent to a onClickListener for this button.
                            If the match has started when you click the button it checks your orientation.
                            Your orientation determines whether this button is for game piece 4 or game piece 1.
                            It then checks to see if whichever game piece it is has already been taken.
                            If it has not it marks that it has, adds it to the timeline, and switches screens.
                            */
                            onClick = {
                                if (matchTimer != null) {
                                    if (orientation) {
                                        if (autoIntakeGamePieceFour == 0) {
                                            autoIntakeGamePieceFour = 1
                                            collectionObjectiveActivity.timelineAddWithStage(
                                                action_type = Constants.ActionType.AUTO_INTAKE_FOUR
                                            )
                                            collectionObjectiveActivity.scoringScreen = true
                                        }
                                    } else {
                                        if (autoIntakeGamePieceOne == 0) {
                                            autoIntakeGamePieceOne = 1
                                            collectionObjectiveActivity.timelineAddWithStage(
                                                action_type = Constants.ActionType.AUTO_INTAKE_ONE
                                            )
                                            collectionObjectiveActivity.scoringScreen = true
                                        }
                                    }
                                }
                            },
                            /*
                            It checks your alliance color and whether or not you have already intaken the piece.
                            If you have taken the piece, sets the background color to light grey.
                            Otherwise, sets it to your alliance color.
                             */
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(
                                    id =
                                    if (allianceColor == Constants.AllianceColor.RED) {
                                        if ((orientation && autoIntakeGamePieceFour == 0) || (!orientation && autoIntakeGamePieceOne == 0)) {
                                            R.color.action_red
                                        } else {
                                            R.color.light_gray
                                        }
                                    } else {
                                        if ((orientation && autoIntakeGamePieceFour == 0) || (!orientation && autoIntakeGamePieceOne == 0)) {
                                            R.color.action_blue
                                        } else {
                                            R.color.light_gray
                                        }
                                    }
                                )
                            ),
                            // Sets the size and padding of the button.
                            modifier = Modifier
                                .size(120.dp)
                                .padding(10.dp)
                        ) {

                            // Sets the text of the depending on which button # you are and if you have taken it.
                            if (orientation) {
                                if (autoIntakeGamePieceFour == 0) {
                                    Text(
                                        text = getString(R.string.four_starting_position),
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                } else {
                                    Text(
                                        text = getString(R.string.taken),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                if (autoIntakeGamePieceOne == 0) {
                                    Text(
                                        text = getString(R.string.one_starting_position),
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                } else {
                                    Text(
                                        text = getString(R.string.taken),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        // The second button.
                        TextButton(

                            /*
                            The equivalent to a onClickListener for this button.
                            If the match has started when you click the button it checks your orientation.
                            Your orientation determines whether this button is for game piece 2 or game piece 3.
                            It then checks to see if whichever game piece it is has already been taken.
                            If it has not it marks that it has, adds it to the timeline, and switches screens.
                            */
                            onClick = {
                                if (matchTimer != null) {
                                    if (orientation) {
                                        if (autoIntakeGamePieceThree == 0) {
                                            autoIntakeGamePieceThree = 1
                                            collectionObjectiveActivity.timelineAddWithStage(
                                                action_type = Constants.ActionType.AUTO_INTAKE_THREE
                                            )
                                            collectionObjectiveActivity.scoringScreen = true
                                        }
                                    } else {
                                        if (autoIntakeGamePieceTwo == 0) {
                                            autoIntakeGamePieceTwo = 1
                                            collectionObjectiveActivity.timelineAddWithStage(
                                                action_type = Constants.ActionType.AUTO_INTAKE_TWO
                                            )
                                            collectionObjectiveActivity.scoringScreen = true
                                        }
                                    }
                                }
                            },

                            /*
                            It checks your alliance color and whether or not you have already intaken the piece.
                            If you have taken the piece, sets the background color to light grey.
                            Otherwise, sets it to your alliance color.
                             */
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(
                                    id =
                                    if (allianceColor == Constants.AllianceColor.RED) {
                                        if ((orientation && autoIntakeGamePieceThree == 0) || (!orientation && autoIntakeGamePieceTwo == 0)) {
                                            R.color.action_red
                                        } else {
                                            R.color.light_gray
                                        }
                                    } else {
                                        if ((orientation && autoIntakeGamePieceThree == 0) || (!orientation && autoIntakeGamePieceTwo == 0)) {
                                            R.color.action_blue
                                        } else {
                                            R.color.light_gray
                                        }
                                    }
                                )
                            ),

                            // Sets the size and padding of the button.
                            modifier = Modifier
                                .size(120.dp)
                                .padding(10.dp)
                        ) {
                            // Sets the text of the depending on which button # you are and if you have taken it.
                            if (orientation) {
                                if (autoIntakeGamePieceThree == 0) {
                                    Text(
                                        text = getString(R.string.three_starting_position),
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                } else {
                                    Text(
                                        text = getString(R.string.taken),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                if (autoIntakeGamePieceTwo == 0) {
                                    Text(
                                        text = getString(R.string.two_starting_position),
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                } else {
                                    Text(
                                        text = getString(R.string.taken),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // The third button.
                        TextButton(

                            /*
                            The equivalent to a onClickListener for this button.
                            If the match has started when you click the button it checks your orientation.
                            Your orientation determines whether this button is for game piece 2 or game piece 3.
                            It then checks to see if whichever game piece it is has already been taken.
                            If it has not it marks that it has, adds it to the timeline, and switches screens.
                            */
                            onClick = {
                                if (matchTimer != null) {
                                    if (orientation) {
                                        if (autoIntakeGamePieceTwo == 0) {
                                            autoIntakeGamePieceTwo = 1
                                            collectionObjectiveActivity.timelineAddWithStage(
                                                action_type = Constants.ActionType.AUTO_INTAKE_TWO
                                            )
                                            collectionObjectiveActivity.scoringScreen = true
                                        }
                                    } else {
                                        if (autoIntakeGamePieceThree == 0) {
                                            autoIntakeGamePieceThree = 1
                                            collectionObjectiveActivity.timelineAddWithStage(
                                                action_type = Constants.ActionType.AUTO_INTAKE_THREE
                                            )
                                            collectionObjectiveActivity.scoringScreen = true
                                        }
                                    }
                                }
                            },

                            /*
                           It checks your alliance color and whether or not you have already intaken the piece.
                           If you have taken the piece, sets the background color to light grey.
                           Otherwise, sets it to your alliance color.
                            */
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(
                                    id =
                                    if (allianceColor == Constants.AllianceColor.RED) {
                                        if ((orientation && autoIntakeGamePieceTwo == 0) || (!orientation && autoIntakeGamePieceThree == 0)) {
                                            R.color.action_red
                                        } else {
                                            R.color.light_gray
                                        }
                                    } else {
                                        if ((orientation && autoIntakeGamePieceTwo == 0) || (!orientation && autoIntakeGamePieceThree == 0)) {
                                            R.color.action_blue
                                        } else {
                                            R.color.light_gray
                                        }
                                    }
                                )

                                // Sets the size and padding of the button.
                            ),
                            modifier = Modifier
                                .size(120.dp)
                                .padding(10.dp)
                        ) {

                            // Sets the text of the depending on which button # you are and if you have taken it.
                            if (orientation) {
                                if (autoIntakeGamePieceTwo == 0) {
                                    Text(
                                        text = getString(R.string.two_starting_position),
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                } else {
                                    Text(
                                        text = getString(R.string.taken),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                if (autoIntakeGamePieceThree == 0) {
                                    Text(
                                        text = getString(R.string.three_starting_position),
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                } else {
                                    Text(
                                        text = getString(R.string.taken),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // The fourth button.
                        TextButton(

                            /*
                           The equivalent to a onClickListener for this button.
                           If the match has started when you click the button it checks your orientation.
                           Your orientation determines whether this button is for game piece 1 or game piece 4.
                           It then checks to see if whichever game piece it is has already been taken.
                           If it has not it marks that it has, adds it to the timeline, and switches screens.
                           */
                            onClick = {
                                if (matchTimer != null) {
                                    if (orientation) {
                                        if (autoIntakeGamePieceOne == 0) {
                                            autoIntakeGamePieceOne = 1
                                            collectionObjectiveActivity.timelineAddWithStage(
                                                action_type = Constants.ActionType.AUTO_INTAKE_ONE
                                            )
                                            collectionObjectiveActivity.scoringScreen = true
                                        }
                                    } else {
                                        if (autoIntakeGamePieceFour == 0) {
                                            autoIntakeGamePieceFour = 1
                                            collectionObjectiveActivity.timelineAddWithStage(
                                                action_type = Constants.ActionType.AUTO_INTAKE_FOUR
                                            )
                                            collectionObjectiveActivity.scoringScreen = true
                                        }
                                    }
                                }
                            },

                            /*
                          It checks your alliance color and whether or not you have already intaken the piece.
                          If you have taken the piece, sets the background color to light grey.
                          Otherwise, sets it to your alliance color.
                           */
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(
                                    id =
                                    if (allianceColor == Constants.AllianceColor.RED) {
                                        if ((orientation && autoIntakeGamePieceOne == 0) || (!orientation && autoIntakeGamePieceFour == 0)) {
                                            R.color.action_red
                                        } else {
                                            R.color.light_gray
                                        }
                                    } else {
                                        if ((orientation && autoIntakeGamePieceOne == 0) || (!orientation && autoIntakeGamePieceFour == 0)) {
                                            R.color.action_blue
                                        } else {
                                            R.color.light_gray
                                        }
                                    }
                                )
                            ),

                            // Sets the size and padding of the button.
                            modifier = Modifier
                                .size(120.dp)
                                .padding(10.dp)
                        ) {

                            // Sets the text of the depending on which button # you are and if you have taken it.
                            if (orientation) {
                                if (autoIntakeGamePieceOne == 0) {
                                    Text(
                                        text = getString(R.string.one_starting_position),
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                } else {
                                    Text(
                                        text = getString(R.string.taken),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                if (autoIntakeGamePieceFour == 0) {
                                    Text(
                                        text = getString(R.string.four_starting_position),
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                } else {
                                    Text(
                                        text = getString(R.string.taken),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Pushes the buttons up when the background is red_up_game_pieces.
                        if (orientation && allianceColor == AllianceColor.RED) {
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                        // Pushes the buttons up when the background is blue_up_game_pieces.
                        if (orientation && allianceColor == AllianceColor.BLUE) {
                            Spacer(modifier = Modifier.height(75.dp))
                        }
                    }
                    // Pushes the buttons left when the background is red_down_game_pieces.
                    if (!orientation && allianceColor == AllianceColor.RED) {
                        Spacer(modifier = Modifier.width(170.dp))
                    }
                    // Pushes the buttons left when the background is blue_up_game_pieces.
                    if (orientation && allianceColor == AllianceColor.BLUE) {
                        Spacer(modifier = Modifier.width(195.dp))
                    }
                }
            }
        }
    }
}