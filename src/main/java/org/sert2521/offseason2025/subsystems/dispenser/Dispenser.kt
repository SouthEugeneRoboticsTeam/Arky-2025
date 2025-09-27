package org.sert2521.offseason2025.subsystems.dispenser

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import edu.wpi.first.wpilibj2.command.SubsystemBase
import edu.wpi.first.wpilibj2.command.button.Trigger
import org.littletonrobotics.junction.Logger
import org.sert2521.offseason2025.DispenserConstants.INTAKE_SPEED_FIRST
import org.sert2521.offseason2025.DispenserConstants.INTAKE_SPEED_SECOND
import org.sert2521.offseason2025.DispenserConstants.INTAKE_SPEED_THIRD
import org.sert2521.offseason2025.DispenserConstants.OUTTAKE_L4_SPEED
import org.sert2521.offseason2025.DispenserConstants.OUTTAKE_NORMAL_SPEED
import org.sert2521.offseason2025.DispenserConstants.OUTTAKE_TIME

object Dispenser : SubsystemBase() {
    private val io = DispenserIOSpark()
    private val ioInputs = LoggedDispenserIOInputs()

    private val intakeTrigger = Trigger { getDispenserBlocked() }

    var reverseOuttake = false

    init {
        defaultCommand = run { io.setMotor(0.0) }

        intakeTrigger.onTrue(intakeCommand())
    }

    override fun periodic() {
        io.updateInputs(ioInputs)
        Logger.processInputs("Dispenser", ioInputs)
    }

    fun getBlocked(): Boolean {
        return !ioInputs.dispenserBeambreakClear || !ioInputs.rampBeambreakClear
    }

    fun getRampBlocked(): Boolean {
        return !ioInputs.rampBeambreakClear
    }

    fun getDispenserBlocked(): Boolean {
        return !ioInputs.dispenserBeambreakClear
    }

    fun intakeCommand(): Command {
        return SequentialCommandGroup(
            run { io.setMotor(INTAKE_SPEED_FIRST) }.until { !getBlocked() },
            run { io.setMotor(INTAKE_SPEED_SECOND) }.until { getBlocked() },
            run { io.setMotor(INTAKE_SPEED_THIRD) }.until { !getBlocked() }
        )
    }

    fun outtakeCommand(): Command {
        return run {
            if (reverseOuttake) {
                io.setMotor(OUTTAKE_L4_SPEED)
            } else {
                io.setMotor(OUTTAKE_NORMAL_SPEED)
            }
        }.withTimeout(OUTTAKE_TIME)
    }
}