package org.sert2521.offseason2025

import edu.wpi.first.math.MathUtil
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.Commands.runOnce
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import edu.wpi.first.wpilibj2.command.button.Trigger
import org.sert2521.offseason2025.commands.drivetrain.DrivetrainVisionAlign
import org.sert2521.offseason2025.commands.drivetrain.JoystickDrive
import org.sert2521.offseason2025.commands.manipulator.ManipulatorRoutines
import org.sert2521.offseason2025.commands.manipulator.ManipulatorRoutines.ManipulatorPositions
import org.sert2521.offseason2025.subsystems.dispenser.Dispenser
import org.sert2521.offseason2025.subsystems.drivetrain.Drivetrain
import org.sert2521.offseason2025.subsystems.drivetrain.SwerveConstants
import org.sert2521.offseason2025.subsystems.elevator.Elevator
import org.sert2521.offseason2025.subsystems.ramp.Ramp

object Input {
    private val driverController = CommandXboxController(0)
    val gunnerController = CommandXboxController(1)

    private val resetRotOffset = driverController.y()
    private val resetGyroRawYaw = driverController.start()
    private val resetGyroVision = driverController.back()

    private val simpleVisionAlignLeft = driverController.x()
    private val simpleVisionAlignRight = driverController.b()
    private val stopJoystickFieldOrientation = Trigger { driverController.leftTriggerAxis > 0.3 }

    private val maniStow = gunnerController.button(3)
    private val maniL1 = gunnerController.button(8)
    private val maniL2 = gunnerController.button(7)
    private val maniL3 = gunnerController.button(6)
    private val maniL4 = gunnerController.button(5)

    private val manualActivateBuffer = gunnerController.button(4)

    private val dispenserOuttake = driverController.rightBumper()
    private val dispenserOuttakeNoBack = driverController.leftBumper()
    private val dispenserReset = gunnerController.button(14)
    private val rampIntake = gunnerController.button(1)
    private val rampRecenter = gunnerController.button(2)

    private val intakeRumble = Trigger { Dispenser.getBlocked() }

    private var rotationOffset = Rotation2d(0.0)

    init {
        intakeRumble.onTrue(rumbleBlip())

        resetRotOffset.onTrue(runOnce({ rotationOffset = Drivetrain.getPose().rotation }))
        resetGyroRawYaw.onTrue(runOnce({
            Drivetrain.setPose(
                Pose2d(Drivetrain.getPose().x, Drivetrain.getPose().y, Rotation2d())
            )
        }))

        stopJoystickFieldOrientation.whileTrue(JoystickDrive(false))

        simpleVisionAlignLeft.onTrue(ManipulatorRoutines.activateBufferCommand())
        simpleVisionAlignLeft.whileTrue(DrivetrainVisionAlign(true))

        simpleVisionAlignRight.onTrue(ManipulatorRoutines.activateBufferCommand())
        simpleVisionAlignRight.whileTrue(DrivetrainVisionAlign(false))


        maniStow.onTrue(ManipulatorRoutines.stow())
        maniL1.onTrue(ManipulatorRoutines.l1())
        maniL2.onTrue(ManipulatorRoutines.changeBufferCommand(ManipulatorPositions.L2))
        maniL3.onTrue(ManipulatorRoutines.changeBufferCommand(ManipulatorPositions.L3))
        maniL4.onTrue(ManipulatorRoutines.changeBufferCommand(ManipulatorPositions.L4))

        manualActivateBuffer.onTrue(ManipulatorRoutines.activateBufferCommand())

        dispenserOuttake.whileTrue(Dispenser.outtakeCommand())
        dispenserOuttakeNoBack.whileTrue(Dispenser.outtakeCommandNoBack())
        dispenserReset.onTrue(Dispenser.recenterCommand().alongWith(Ramp.recenterCommand()))
        rampIntake.onTrue(Ramp.recenterCommand().andThen(ManipulatorRoutines.intake())
            .andThen((Ramp.intakeCommand().alongWith(Dispenser.recenterCommand().asProxy()))).until{ !rampIntake.asBoolean })
        rampRecenter.whileTrue(Ramp.recenterCommand())
    }

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

    fun getAccelLimit(): Double {
        val elevatorLimit = MathUtil.interpolate(
            SwerveConstants.DRIVE_ACCEL_FAST, SwerveConstants.DRIVE_ACCEL_SLOW,
            Elevator.getPosition() / ElevatorConstants.l4.elevatorGoalMeters
        )
        return elevatorLimit
    }

    fun getDeccelLimit(): Double {
        val elevatorLimit = MathUtil.interpolate(
            SwerveConstants.DRIVE_DECCEL_FAST, SwerveConstants.DRIVE_DECCEL_SLOW,
            Elevator.getPosition() / ElevatorConstants.l4.elevatorGoalMeters
        )
        return elevatorLimit
    }

    fun getSpeedLimit(): Double {
        val elevatorLimit = MathUtil.interpolate(
            SwerveConstants.DRIVE_SPEED_FAST, SwerveConstants.DRIVE_SPEED_SLOW,
            Elevator.getPosition() / ElevatorConstants.l4.elevatorGoalMeters
        )
        return MathUtil.interpolate(elevatorLimit, elevatorLimit / 5.0, driverController.rightTriggerAxis)
    }

    fun rumbleBlip(): Command {
        return runOnce({ setRumble(0.8) })
            .andThen(Commands.waitSeconds(0.2))
            .andThen(runOnce({ setRumble(0.0) }))
    }
}