package org.sert2521.offseason2025.commands.manipulator

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import org.sert2521.offseason2025.ManipulatorTargets
import org.sert2521.offseason2025.subsystems.elevator.Elevator
import org.sert2521.offseason2025.subsystems.wrist.Wrist

object ManipulatorRoutines {
    fun stow(): Command {
        return Wrist.setWristCommand(ManipulatorTargets.stow.wristGoalRotations)
            .alongWith(Elevator.setElevatorCommand(ManipulatorTargets.stow.elevatorGoalMeters))
    }

    fun l1(): Command {
        return Wrist.setWristCommand(ManipulatorTargets.l1.wristGoalRotations)
            .alongWith(Elevator.setElevatorCommand(ManipulatorTargets.l1.elevatorGoalMeters))
    }

    fun l2(): Command {
        return Wrist.setWristCommand(ManipulatorTargets.l2.wristGoalRotations)
            .alongWith(Elevator.setElevatorCommand(ManipulatorTargets.l2.elevatorGoalMeters))
    }

    fun l3(): Command {
        return Wrist.setWristCommand(ManipulatorTargets.l3.wristGoalRotations)
            .alongWith(Elevator.setElevatorCommand(ManipulatorTargets.l3.elevatorGoalMeters))
    }

    fun l4(): Command {
        return Wrist.setWristCommand(ManipulatorTargets.l4.wristGoalRotations)
            .alongWith(Elevator.setElevatorCommand(ManipulatorTargets.l4.elevatorGoalMeters))
    }
}