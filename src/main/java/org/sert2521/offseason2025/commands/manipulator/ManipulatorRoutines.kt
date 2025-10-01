package org.sert2521.offseason2025.commands.manipulator

import edu.wpi.first.math.Pair
import edu.wpi.first.wpilibj2.command.*
import org.sert2521.offseason2025.ElevatorConstants
import org.sert2521.offseason2025.subsystems.dispenser.Dispenser
import org.sert2521.offseason2025.subsystems.elevator.Elevator
import org.sert2521.offseason2025.subsystems.wrist.Wrist

enum class Manipulator

object ManipulatorRoutines {
    enum class ManipulatorPositions {
        STOW,
        L1,
        L2,
        L3,
        L4
    }

    fun stow(): Command {
        return Wrist.setWristCommand(ElevatorConstants.stow.wristGoalRotations)
            .alongWith(Elevator.setElevatorSafeCommand(ElevatorConstants.stow.elevatorGoalMeters))
            .alongWith(Commands.runOnce({ Dispenser.reverseOuttake = false }))
    }

    fun l1(): Command {
        return Wrist.setWristCommand(ElevatorConstants.l1.wristGoalRotations)
            .alongWith(Elevator.setElevatorSafeCommand(ElevatorConstants.l1.elevatorGoalMeters))
            .alongWith(Commands.runOnce({ Dispenser.reverseOuttake = false }))
    }

    fun l2(): Command {
        return Wrist.setWristCommand(ElevatorConstants.l2.wristGoalRotations)
            .alongWith(Elevator.setElevatorSafeCommand(ElevatorConstants.l2.elevatorGoalMeters))
            .alongWith(Commands.runOnce({ Dispenser.reverseOuttake = false }))
    }

    fun l3(): Command {
        return Wrist.setWristCommand(ElevatorConstants.l3.wristGoalRotations)
            .alongWith(Elevator.setElevatorSafeCommand(ElevatorConstants.l3.elevatorGoalMeters))
            .alongWith(Commands.runOnce({ Dispenser.reverseOuttake = false }))
    }

    fun l4(): Command {
        return Wrist.setWristCommand(ElevatorConstants.l4.wristGoalRotations)
            .alongWith(Elevator.setElevatorSafeCommand(ElevatorConstants.l4.elevatorGoalMeters))
            .alongWith(Commands.runOnce({ Dispenser.reverseOuttake = true }))
    }


    /* Buffered */
    private var bufferedLevel = ManipulatorPositions.STOW
    private val levelToCommandMap = mapOf(
        ManipulatorPositions.STOW to Commands.none(),
        ManipulatorPositions.L1 to l1(),
        ManipulatorPositions.L2 to l2(),
        ManipulatorPositions.L3 to l3(),
        ManipulatorPositions.L4 to l4()
    )

    fun changeBufferCommand(newBuffer: ManipulatorPositions):Command{
        return Commands.runOnce({ bufferedLevel = newBuffer })
    }

    fun activateBufferCommand():Command{
        return SelectCommand(levelToCommandMap) { bufferedLevel }
    }
}