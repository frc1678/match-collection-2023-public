// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

// Contains constant values and enum classes.
class Constants {
    companion object {
        const val NONE_VALUE: String = "NONE"
        const val NUMBER_OF_ACTIVE_SCOUTS: Int = 18
        const val COMPRESSED_QR_TAG = "QR"
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
        ZERO,
        ONE,
        TWO,
        THREE,
        FOUR,
        NONE
    }

    enum class Preloaded {
        NONE,
        CONE,
        CUBE
    }

    enum class ClimbLevel {
        NONE,
        LOW,
        MID,
        HIGH,
        TRAVERSAL,
    }

    enum class ActionType {
        SCORE_BALL_HIGH,
        SCORE_BALL_LOW,
        INTAKE,
        START_INCAP,
        END_INCAP,
        CLIMB_ATTEMPT,
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
