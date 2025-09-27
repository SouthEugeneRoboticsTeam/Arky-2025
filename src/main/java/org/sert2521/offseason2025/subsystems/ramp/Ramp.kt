package org.sert2521.offseason2025.subsystems.ramp

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.littletonrobotics.junction.Logger
import org.sert2521.offseason2025.RampConstants.INTAKE_SPEED
import org.sert2521.offseason2025.RampConstants.RECENTER_SPEED
import org.sert2521.offseason2025.subsystems.dispenser.Dispenser

object Ramp : SubsystemBase() {
    private val io = RampIOSpark()
    private val ioInputs = LoggedRampIOInputs()

    init {
        defaultCommand = idleCommand()
    }

    override fun periodic() {
        io.updateInputs(ioInputs)
        Logger.processInputs("Ramp", ioInputs)
    }

    fun idleCommand(): Command {
        return run {
            if (Dispenser.getBlocked()) {
                io.setSpeed(INTAKE_SPEED)
            } else {
                io.setSpeed(0.0)
            }
        }
    }

    fun intakeCommand(): Command {
        return run {
            io.setSpeed(INTAKE_SPEED)
        }
    }

    // TODO: Change
    fun recenterCommand(): Command {
        return SequentialCommandGroup(
            run { io.setSpeed(RECENTER_SPEED) }.until { !Dispenser.getBlocked() },
            intakeCommand().withTimeout(2.0)
        )
    }
}