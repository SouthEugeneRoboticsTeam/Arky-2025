package org.sert2521.offseason2025.commands.manipulator

import edu.wpi.first.wpilibj2.command.*
import org.sert2521.offseason2025.ElevatorConstants
import org.sert2521.offseason2025.subsystems.dispenser.Dispenser
import org.sert2521.offseason2025.subsystems.drivetrain.Drivetrain
import org.sert2521.offseason2025.subsystems.elevator.Elevator
import org.sert2521.offseason2025.subsystems.wrist.Wrist

object ManipulatorRoutines {
    enum class ManipulatorPositions {
        STOW,
        L1,
        L2,
        L3,
        L4
    }

    fun intake(): Command {
        return Elevator.setElevatorSafeCommand(ElevatorConstants.intake.elevatorGoalMeters)
            .alongWith(Commands.runOnce({ Dispenser.reverseOuttake = false }))
            .andThen(Wrist.setWristCommand(ElevatorConstants.intake.wristGoalRotations))
    }

    fun l1(): Command {
        return Elevator.setElevatorSafeCommand(ElevatorConstants.l1.elevatorGoalMeters)
            .alongWith(Commands.runOnce({ Dispenser.reverseOuttake = false }))
            .andThen(Wrist.setWristCommand(ElevatorConstants.l1.wristGoalRotations))    }

    fun l2Safe(): Command {
        return Drivetrain.driveBackCommand().withTimeout(0.2)
            .andThen(Elevator.setElevatorSafeCommand(ElevatorConstants.l2.elevatorGoalMeters)
                .alongWith(Commands.runOnce({ Dispenser.reverseOuttake = true }))
                .andThen(Wrist.setWristCommand(ElevatorConstants.l2.wristGoalRotations)))
            .andThen(Drivetrain.driveForwardCommand().withTimeout(0.2))
    }

    fun l3Safe(): Command {
        return Drivetrain.driveBackCommand().withTimeout(0.2)
            .andThen(Elevator.setElevatorSafeCommand(ElevatorConstants.l3.elevatorGoalMeters)
                .alongWith(Commands.runOnce({ Dispenser.reverseOuttake = true }))
                .andThen(Wrist.setWristCommand(ElevatorConstants.l3.wristGoalRotations)))
            .andThen(Drivetrain.driveForwardCommand().withTimeout(0.2))
    }

    fun l4Safe(): Command {
        return Drivetrain.driveBackCommand().withTimeout(0.2)
            .andThen(Elevator.setElevatorSafeCommand(ElevatorConstants.l4.elevatorGoalMeters)
            .alongWith(Commands.runOnce({ Dispenser.reverseOuttake = true }))
            .andThen(Wrist.setWristCommand(ElevatorConstants.l4.wristGoalRotations)))
            .andThen(Drivetrain.driveForwardCommand().withTimeout(0.2))
    }

    fun stow(): Command {
        return Wrist.setWristCommand(ElevatorConstants.l4Out.wristGoalRotations)
            .alongWith(Commands.runOnce({ Dispenser.reverseOuttake = true }))
            .andThen(Elevator.setElevatorSafeCommand(ElevatorConstants.stow.elevatorGoalMeters))
            .andThen(Wrist.setWristCommand(ElevatorConstants.stow.wristGoalRotations))
    }

    fun afterOuttake(): Command {
        return Drivetrain.driveBackCommand().withTimeout(0.4).asProxy()
            .andThen(this.stow())

    }


    /* Buffered */
    fun l2Instant(): Command {
        return Elevator.setElevatorSafeCommand(ElevatorConstants.l2.elevatorGoalMeters)
                .alongWith(Commands.runOnce({ Dispenser.reverseOuttake = true }))
                .andThen(Wrist.setWristCommand(ElevatorConstants.l2.wristGoalRotations))
    }

    fun l3Instant(): Command {
        return Elevator.setElevatorSafeCommand(ElevatorConstants.l3.elevatorGoalMeters)
                .alongWith(Commands.runOnce({ Dispenser.reverseOuttake = true }))
                .andThen(Wrist.setWristCommand(ElevatorConstants.l3.wristGoalRotations))
    }

    fun l4Instant(): Command {
        return Elevator.setElevatorSafeCommand(ElevatorConstants.l4.elevatorGoalMeters)
                .alongWith(Commands.runOnce({ Dispenser.reverseOuttake = true }))
                .andThen(Wrist.setWristCommand(ElevatorConstants.l4.wristGoalRotations))
    }

    private var bufferedLevel = ManipulatorPositions.STOW

    fun changeBufferCommand(newBuffer: ManipulatorPositions):Command{
        return Commands.runOnce({ bufferedLevel = newBuffer })
    }

    fun activateBufferCommand():Command{
        return SelectCommand(
            mapOf(
                ManipulatorPositions.STOW to Commands.none(),
                ManipulatorPositions.L1 to l1(),
                ManipulatorPositions.L2 to l2Instant(),
                ManipulatorPositions.L3 to l3Instant(),
                ManipulatorPositions.L4 to l4Instant()
            )
        ) { bufferedLevel }
    }
}