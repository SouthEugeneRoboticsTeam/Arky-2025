package org.sert2521.offseason2025.subsystems.elevator

import edu.wpi.first.math.MathUtil
import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.littletonrobotics.junction.Logger
import org.sert2521.offseason2025.ElevatorConstants.ELEVATOR_PROFILE
import org.sert2521.offseason2025.ManipulatorTargets
import org.sert2521.offseason2025.MetaConstants

object Elevator : SubsystemBase() {
    private val io = when (MetaConstants.currentRealityMode) {
        MetaConstants.RealityMode.REAL -> ElevatorIOSpark()
        else -> object : ElevatorIO {}
    }

    private val telemetry = when(MetaConstants.currentRealityMode) {
        MetaConstants.RealityMode.REAL -> ElevatorTelemetry(mechanism2dEnabled = false, mechanism3dEnabled = false)
        else -> ElevatorTelemetry(mechanism2dEnabled = true, mechanism3dEnabled = true)
    }

    private val ioInputs = LoggedElevatorIOInputs()

    private val profile = TrapezoidProfile(ELEVATOR_PROFILE)

    var goal = TrapezoidProfile.State(0.0, 0.0)
    private var currentState = TrapezoidProfile.State(0.0, 0.0)

    init {
        defaultCommand = holdElevatorCommand()
    }

    override fun periodic() {
        io.updateInputs(ioInputs)
        Logger.processInputs("Elevator", ioInputs)

        telemetry.update()
    }

    fun setVoltage(voltage: Double) {
        io.setVoltage(voltage)
    }

    fun getVelocity(): Double {
        return ioInputs.velocityMetersPerSec
    }

    fun getPosition(): Double {
        return ioInputs.positionMeters
    }

    fun stop() {
        io.setVoltage(0.0)
    }

    fun setElevatorCommand(goalMeters: Double): Command {
        /* Sets the goal of the motion profile, and then executes the calculations required
            then sets the motor setpoint to the result */
        return startRun({
            goal.position = goalMeters
            goal.velocity = 0.0
        },
            {
                currentState = profile.calculate(0.02, currentState, goal)

                io.setReference(currentState.position, currentState.velocity)

                Logger.recordOutput(
                    "Elevator/At Setpoint",
                    MathUtil.isNear(currentState.position, getPosition(), 0.05)
                )
            }
        ).until {
            profile.isFinished(0.0)
        }
    }

    fun setElevatorSafeCommand(goalMeters: Double): Command {
        return Commands.waitUntil { /*!Dispenser.getBlocked()*/false }
            .andThen(setElevatorCommand(goalMeters))
    }

    private fun holdElevatorCommand(): Command {
        // If it's at stow, then set the voltage to 0
        // Otherwise just run the profile without a new goal until another goal is set
        return run {
            if (goal.position == ManipulatorTargets.stow.elevatorGoalMeters) {
                io.setVoltage(0.0)
            } else {
                io.setReference(currentState.position, currentState.velocity)
            }

            Logger.recordOutput(
                "Elevator/At Setpoint",
                MathUtil.isNear(currentState.position, getPosition(), 0.05)
            )
        }
    }
}