package org.sert2521.offseason2025.subsystems.dispenser

import edu.wpi.first.math.MathUtil
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import edu.wpi.first.wpilibj2.command.SubsystemBase
import edu.wpi.first.wpilibj2.command.WaitUntilCommand
import edu.wpi.first.wpilibj2.command.button.Trigger
import org.littletonrobotics.junction.Logger
import org.sert2521.offseason2025.DispenserConstants.INTAKE_SPEED_FIRST
import org.sert2521.offseason2025.DispenserConstants.INTAKE_SPEED_SECOND
import org.sert2521.offseason2025.DispenserConstants.INTAKE_SPEED_THIRD
import org.sert2521.offseason2025.DispenserConstants.OUTTAKE_L4_SPEED
import org.sert2521.offseason2025.DispenserConstants.OUTTAKE_NORMAL_SPEED
import org.sert2521.offseason2025.DispenserConstants.OUTTAKE_TIME
import org.sert2521.offseason2025.commands.manipulator.ManipulatorRoutines
import org.sert2521.offseason2025.DispenserConstants.PID_TO_BACK_DISTANCE

object Dispenser : SubsystemBase() {
    private val io = DispenserIOSpark()
    private val ioInputs = LoggedDispenserIOInputs()

    private val intakeTrigger = Trigger { getRampBlocked() }

    var reverseOuttake = false

    init {
        defaultCommand = run { io.setPIDReference(0.0) }

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
            run { io.setSpeed(INTAKE_SPEED_FIRST) }.until { !getBlocked() },
            run { io.setSpeed(INTAKE_SPEED_SECOND) }.until { getBlocked() },
            run { io.setSpeed(INTAKE_SPEED_THIRD) }.until { !getBlocked() }
        ).finallyDo {
            value -> ManipulatorRoutines.stow().schedule()
            io.resetEncoder()
        }
    }

    fun outtakeCommand(): Command {
        return run {
            if (reverseOuttake) {
                io.setSpeed(OUTTAKE_L4_SPEED)
            } else {
                io.setSpeed(OUTTAKE_NORMAL_SPEED)
            }
        }.withTimeout(OUTTAKE_TIME)
            .finallyDo {
                value -> ManipulatorRoutines.afterOuttake().schedule()
                io.resetEncoder()
            }
    }

    fun outtakeCommandNoBack(): Command {
        return run {
            if (reverseOuttake) {
                io.setSpeed(OUTTAKE_L4_SPEED)
            } else {
                io.setSpeed(OUTTAKE_NORMAL_SPEED)
            }
        }.withTimeout(OUTTAKE_TIME)
    }

    fun recenterCommand(): Command {
        return SequentialCommandGroup(
            run { io.setSpeed(INTAKE_SPEED_SECOND) }.until { getBlocked() },
            run { io.setSpeed(INTAKE_SPEED_THIRD) }.until { !getBlocked() }
        ).finallyDo {
            value ->
            if (!value){
                ManipulatorRoutines.stow().schedule()
                io.resetEncoder()
            }
        }
    }

    fun pidBackward(): Command {
        return runOnce {
            io.resetEncoder()
            io.setPIDReference(PID_TO_BACK_DISTANCE)
        }.andThen(WaitUntilCommand{ MathUtil.isNear(ioInputs.positionEncoder, PID_TO_BACK_DISTANCE, 0.1)})
    }
}