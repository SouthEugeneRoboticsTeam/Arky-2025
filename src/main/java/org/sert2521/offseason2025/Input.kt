package org.sert2521.offseason2025

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
import org.sert2521.offseason2025.subsystems.drivetrain.Drivetrain
import org.sert2521.offseason2025.subsystems.ramp.Ramp

object Input {
    private val driverController = CommandXboxController(0)
    private val gunnerController = CommandXboxController(1)

    private val resetRotOffset = driverController.y()
    private val resetGyroRawYaw = driverController.start()
    private val resetGyroVision = driverController.back()

    private val simpleVisionAlignLeft = driverController.x()
    private val simpleVisionAlignRight = driverController.b()
    private val stopJoystickFieldOrientation = Trigger { driverController.leftTriggerAxis > 0.3 }

    private val maniStow = gunnerController.button(4)
    private val maniL1 = gunnerController.button(8)
    private val maniL2 = gunnerController.button(7)
    private val maniL3 = gunnerController.button(6)
    private val maniL4 = gunnerController.button(5)

    private val dispenserOuttake = driverController.rightBumper()
    private val dispenserReset = gunnerController.button(14)
    private val rampIntake = gunnerController.button(13)

    // TODO: Change when dispenser
    private val intakeRumble = Trigger { false }

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
        simpleVisionAlignLeft.whileTrue(DrivetrainVisionAlign(true))
        simpleVisionAlignRight.whileTrue(DrivetrainVisionAlign(false))

        maniStow.onTrue(ManipulatorRoutines.stow())
        maniL1.onTrue(ManipulatorRoutines.l1())
        maniL2.onTrue(ManipulatorRoutines.l2())
        maniL3.onTrue(ManipulatorRoutines.l3())
        maniL4.onTrue(ManipulatorRoutines.l4())

        //dispenserOuttake.whileTrue(Dispenser.outtakeCommand())
        //dispenserReset.onTrue(Dispenser.recenterCommand().alongWith(Ramp.recenterCommand()))
        rampIntake.whileTrue(Ramp.intakeCommand())

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

    fun rumbleBlip(): Command {
        return runOnce({ setRumble(0.8) }).andThen(Commands.waitSeconds(0.2))
            .andThen(runOnce({ setRumble(0.0) }))
    }
}