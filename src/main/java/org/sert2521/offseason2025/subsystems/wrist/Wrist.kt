package org.sert2521.offseason2025.subsystems.wrist

import edu.wpi.first.math.MathUtil
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.littletonrobotics.junction.Logger
import org.sert2521.offseason2025.ElevatorConstants

object Wrist : SubsystemBase() {
    private val io = WristIOSpark()
    private val ioInputs = LoggedWristIOInputs()

    init {
        // YES I KNOW IT'S ELEVATOR CONSTANTS
        // IT'S CLOSE ENOUGH
        defaultCommand = setWristCommand(ElevatorConstants.stow.wristGoalRotations)
    }

    override fun periodic() {
        io.updateInputs(ioInputs)
        Logger.processInputs("Wrist", ioInputs)
    }

    fun setWristCommand(position: Double): Command {
        return runOnce {
            io.setReference(position)
        }.andThen(Commands.waitUntil { MathUtil.isNear(position, ioInputs.positionRotations, 0.05) })
    }
}