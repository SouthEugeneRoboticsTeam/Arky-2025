package org.sert2521.offseason2025.subsystems.manipulator

import edu.wpi.first.wpilibj2.command.SubsystemBase

object Manipulator : SubsystemBase() {
    private val elevatorIO = ElevatorIOSpark()
    private val wristIO = WristIOSpark()

    private val elevatorIOInputs = LoggedElevatorIOInputs()
    private val wristIOInputs = LoggedWristIOInputs()
}