package org.sert2521.offseason2025

import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.units.Units.Pounds
import edu.wpi.first.wpilibj.RobotBase
import org.sert2521.offseason2025.utils.ManipulatorGoalState

object DispenserConstants {
    const val INTAKE_SPEED_FIRST = 0.2
    const val INTAKE_SPEED_SECOND = 0.0
    const val INTAKE_SPEED_THIRD = 0.0

    const val OUTTAKE_NORMAL_SPEED = 0.0
    const val OUTTAKE_L4_SPEED = 0.0
    const val OUTTAKE_TIME = 1.0

}

object ElevatorConstants {
    val stow = ManipulatorGoalState(0.0, 0.0)
    val l1 = ManipulatorGoalState(0.0, 0.0)
    val l2 = ManipulatorGoalState(0.0, 0.0)
    val l3 = ManipulatorGoalState(0.0, 0.0)
    val l4 = ManipulatorGoalState(0.0, 0.0)

    const val ELEVATOR_CURRENT_LIMIT = 40

    const val ELEVATOR_P = 4.0
    const val ELEVATOR_D = 0.1

    const val ELEVATOR_S = 0.05
    const val ELEVATOR_G = 0.3
    const val ELEVATOR_V = 10.23

    val ELEVATOR_PROFILE = TrapezoidProfile.Constraints(1.0, 2.0)

    const val ELEVATOR_MOTOR_ENCODER_MULTIPLIER = 0.02328333333 / 2.0
}

object WristConstants {
    const val WRIST_CURRENT_LIMIT = 40

    const val WRIST_P = 0.0
    const val WRIST_D = 0.0

    const val WRIST_ABS_ENCODER_MULTIPLIER = (15.0 * 24.0) / (36.0 * 16.0)
}

object RampConstants {
    const val INTAKE_SPEED = 0.0
    const val RECENTER_SPEED = 0.0
}

object PhysicalConstants {
    val robotMass = Pounds.of(115.0)
}

object ControllerConstants {
    const val POWER_DEADBAND = 0.2
    const val ROTATION_DEADBAND = 0.2
}

object MetaConstants {
    enum class RealityMode {
        REAL,
        SIM,
        REPLAY
    }

    val currentRealityMode = if (RobotBase.isReal()) {
        RealityMode.REAL
    } else {
        RealityMode.SIM
    }
}

object ElectronicIDs {
    const val ELEVATOR_LEFT_ID = 13
    const val ELEVATOR_RIGHT_ID = 14

    const val RAMP_ID = 15

    const val WRIST_ID = 16

    const val DISPENSER_ID = 17

    const val DISPENSER_BEAMBREAK_ID = 0
    const val RAMP_BEAMBREAK_ID = 0
}