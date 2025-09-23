package org.sert2521.offseason2025.commands.manipulator

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import org.sert2521.offseason2025.DispenserConstants
import org.sert2521.offseason2025.ElevatorConstants
import org.sert2521.offseason2025.subsystems.elevator.Elevator
import org.sert2521.offseason2025.subsystems.wrist.Wrist

enum class Manipulator

object ManipulatorRoutines {
    fun stow(): Command {
        return Wrist.setWristCommand(ElevatorConstants.stow.wristGoalRotations)
            .alongWith(Elevator.setElevatorSafeCommand(ElevatorConstants.stow.elevatorGoalMeters))
            .alongWith(Commands.runOnce({ DispenserConstants.reverseOuttake = false }) )
    }

    fun l1(): Command {
        return Wrist.setWristCommand(ElevatorConstants.l1.wristGoalRotations)
            .alongWith(Elevator.setElevatorSafeCommand(ElevatorConstants.l1.elevatorGoalMeters))
            .alongWith(Commands.runOnce({ DispenserConstants.reverseOuttake = true }) )
    }

    fun l2(): Command {
        return Wrist.setWristCommand(ElevatorConstants.l2.wristGoalRotations)
            .alongWith(Elevator.setElevatorSafeCommand(ElevatorConstants.l2.elevatorGoalMeters))
            .alongWith(Commands.runOnce({ DispenserConstants.reverseOuttake = false }) )
    }

    fun l3(): Command {
        return Wrist.setWristCommand(ElevatorConstants.l3.wristGoalRotations)
            .alongWith(Elevator.setElevatorSafeCommand(ElevatorConstants.l3.elevatorGoalMeters))
            .alongWith(Commands.runOnce({ DispenserConstants.reverseOuttake = false }) )
    }

    fun l4(): Command {
        return Wrist.setWristCommand(ElevatorConstants.l4.wristGoalRotations)
            .alongWith(Elevator.setElevatorSafeCommand(ElevatorConstants.l4.elevatorGoalMeters))
            .alongWith(Commands.runOnce({ DispenserConstants.reverseOuttake = false }) )
    }
}