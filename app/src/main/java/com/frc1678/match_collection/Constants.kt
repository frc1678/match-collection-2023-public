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

    enum class ChargeLevel {
        N, // NONE
        F, // FAILED
        P, // PARKED
        D, // DOCKED
        E  // ENGAGED
    }

    enum class GamePiecePositions {
        CONE,
        CUBE,
        NONE
    }

    enum class Preloaded {
        NONE,
        CONE,
        CUBE
    }

    enum class ActionType {
        SCORE_CUBE_HIGH,
        SCORE_CUBE_MID,
        SCORE_CUBE_LOW,
        SCORE_CONE_HIGH,
        SCORE_CONE_MID,
        SCORE_CONE_LOW,
        FAIL,
        INTAKE_STATION,
        INTAKE_LOW_ROW,
        INTAKE_GROUND,
        START_INCAP,
        END_INCAP,
        CHARGE_ATTEMPT,
        TO_TELEOP
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
