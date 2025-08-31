package org.sert2521.offseason2025

import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.units.Units.Pounds
import kotlin.math.PI

object DispenserConstants{

}

object ElevatorConstants{
    const val ELEVATOR_CURRENT_LIMIT = 40

    const val ELEVATOR_P = 4.0
    const val ELEVATOR_D = 0.1

    const val ELEVATOR_S = 0.05
    const val ELEVATOR_G = 0.3
    const val ELEVATOR_V = 10.23

    val ELEVATOR_PROFILE = TrapezoidProfile.Constraints(1.0, 2.0)

    const val ELEVATOR_MOTOR_ENCODER_MULTIPLIER = 0.02328333333 / 2.0
}

object WristConstants{
    const val WRIST_CURRENT_LIMIT = 40

    const val WRIST_P = 0.0
    const val WRIST_D = 0.0

    const val WRIST_G = 0.0

    const val WRIST_ABS_ENCODER_MULTIPLIER = (15.0 * 24.0) / (36.0 * 16.0)
}

object RampConstants{

}

object PhysicalConstants{
    val robotMass = Pounds.of(115.0)
}

object ControllerConstants {
    const val POWER_DEADBAND = 0.2
    const val ROTATION_DEADBAND = 0.2
}

object ElectronicIDs {
    const val ELEVATOR_LEFT_ID = 13
    const val ELEVATOR_RIGHT_ID = 14

    const val WRIST_ID = 0
}