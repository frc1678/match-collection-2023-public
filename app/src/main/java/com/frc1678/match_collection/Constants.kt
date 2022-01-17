// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

// Contains constant values and enum classes.
class Constants {
    companion object {
        const val NONE_VALUE: String = "NONE"
        const val NUMBER_OF_ACTIVE_SCOUTS: Int = 18
    }

    enum class ModeSelection {
        SUBJECTIVE,
        OBJECTIVE,
        NONE
    }

    enum class AllianceColor {
        RED,
        BLUE,
        NONE
    }

    enum class StartingPosition {
        ONE,
        TWO,
        THREE,
        FOUR,
        NONE
    }

    enum class ClimbLevel {
        ZERO,
        ONE,
        TWO,
        THREE,
        FOUR,
    }

    enum class ActionType {
        SCORE_BALL_HIGH_NEAR_HUB,
        SCORE_BALL_HIGH_FAR_HUB,
        SCORE_BALL_HIGH_NEAR_OTHER,
        SCORE_BALL_HIGH_FAR_OTHER,
        SCORE_BALL_HIGH_LAUNCHPAD,
        SCORE_BALL_LOW_NEAR_HUB,
        SCORE_BALL_LOW_FAR_HUB,
        INTAKE,
        START_INCAP,
        END_INCAP,
        START_CLIMB,
        END_CLIMB,
        HIGH_LOW_TOGGLE,
        CATCH_EXIT_BALL,
        SCORE_OPPONENT_BALL
    }

    enum class Stage {
        AUTO,
        TELEOP
    }

    enum class AssignmentMode {
        NONE,
        AUTOMATIC_ASSIGNMENT,
        OVERRIDE
    }
}
