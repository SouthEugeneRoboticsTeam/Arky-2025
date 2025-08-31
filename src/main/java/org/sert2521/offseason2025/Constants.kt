package org.sert2521.offseason2025

import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.units.Units.Pounds
import org.sert2521.offseason2025.utils.ManipulatorGoal

object DispenserConstants{

}

object ManipulatorConstants{
    const val ELEVATOR_CURRENT_LIMIT = 40

    const val ELEVATOR_P = 4.0
    const val ELEVATOR_D = 0.1

    const val ELEVATOR_S = 0.05
    const val ELEVATOR_G = 0.3
    const val ELEVATOR_V = 10.23

    val ELEVATOR_PROFILE = TrapezoidProfile.Constraints(1.0, 2.0)

    const val ELEVATOR_MOTOR_ENCODER_MULTIPLIER = 0.02328333333 / 2.0
}

object RampConstants{

}

object PhysicalConstants{
    val robotMass = Pounds.of(115.0)
}

object ElectronicIDs {
    const val ELEVATOR_LEFT_ID = 13
    const val ELEVATOR_RIGHT_ID = 14
}