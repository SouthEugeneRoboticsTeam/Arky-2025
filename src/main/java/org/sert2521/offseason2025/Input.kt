package org.sert2521.offseason2025

import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.Commands.runOnce
import edu.wpi.first.wpilibj2.command.button.CommandXboxController

object Input {
    private val driverController = CommandXboxController(0)
    private val gunnerController = CommandXboxController(1)



    private var rotationOffset = Rotation2d(0.0)

    fun getJoystickX(): Double {
        return -driverController.leftX
    }

    fun getJoystickY(): Double {
        return -driverController.leftY
    }

    fun getJoystickZ(): Double {
        return -driverController.rightX
    }

    fun getRotOffset(): Rotation2d {
        return rotationOffset
    }

    fun setRumble(amount: Double) {
        driverController.setRumble(GenericHID.RumbleType.kBothRumble, amount)
    }

    fun rumbleBlip(): Command {
        return runOnce({ setRumble(0.8) }).andThen(Commands.waitSeconds(0.2))
            .andThen(runOnce({ setRumble(0.0) }))
    }
}