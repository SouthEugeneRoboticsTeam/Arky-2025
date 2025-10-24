package org.sert2521.offseason2025

import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.auto.NamedCommands
import com.pathplanner.lib.config.ModuleConfig
import com.pathplanner.lib.config.RobotConfig
import com.pathplanner.lib.controllers.PPHolonomicDriveController
import com.pathplanner.lib.util.PathPlannerLogging
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.networktables.BooleanSubscriber
import edu.wpi.first.units.Units.KilogramSquareMeters
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import org.littletonrobotics.junction.Logger
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser
import org.sert2521.offseason2025.commands.manipulator.Manipulator
import org.sert2521.offseason2025.commands.manipulator.ManipulatorRoutines
import org.sert2521.offseason2025.subsystems.dispenser.Dispenser
import org.sert2521.offseason2025.subsystems.drivetrain.Drivetrain
import org.sert2521.offseason2025.subsystems.drivetrain.SwerveConstants
import org.sert2521.offseason2025.subsystems.drivetrain.SwerveConstants.DRIVE_GEAR_RATIO
import org.sert2521.offseason2025.subsystems.ramp.Ramp
import kotlin.jvm.optionals.getOrElse

object Autos {
    private var autoChooser: LoggedDashboardChooser<Command>


    val namedCommandList = mapOf<String, Command>(
        "Elevator Stow" to ManipulatorRoutines.stow().asProxy(),
        "Elevator L2" to ManipulatorRoutines.l2Instant().asProxy(),
        "Elevator L3" to ManipulatorRoutines.l3Instant().asProxy(),
        "Elevator L4" to ManipulatorRoutines.l4Instant().asProxy(),

        "Dispenser Intake" to Dispenser.intakeCommand()
            .alongWith(ManipulatorRoutines.intake()).asProxy(),
//        "Dispenser Intake" to //Ramp.intakeCommand()
//            .raceWith(Commands.waitUntil{Dispenser.getBlocked()}
//                .andThen(Commands.waitUntil{!Dispenser.getBlocked()}))
//            .withTimeout(3.0).asProxy(),
        "Dispenser Outtake" to Dispenser.outtakeCommandNoBack().asProxy().withTimeout(0.1),
        "Dispenser Outtake With Back" to Dispenser.outtakeCommand().asProxy(),

        "Wait L1 Post-Outtake" to Commands.none(),
        "Wait L2-4 Pre-Outtake" to Commands.waitSeconds(0.4),
        "Wait L2-4 Post-Outtake" to Commands.none(),
        "Wait Human Player" to Commands.waitSeconds(1.0),
        "Wait Dispenser" to Commands.waitSeconds(2.0)
            .andThen(Commands.waitUntil { !Dispenser.getBlocked() }),

        "Ramp Intake" to ManipulatorRoutines.stow()
            .andThen(ManipulatorRoutines.intake())
            .andThen(Ramp.intakeCommand()).asProxy(),
    )

    init {
        NamedCommands.registerCommands(namedCommandList)

        AutoBuilder.configure(
            Drivetrain::getPose,
            Drivetrain::setPose,
            Drivetrain::getChassisSpeeds,
            Drivetrain::driveRobotOriented,
            PPHolonomicDriveController(
                SwerveConstants.autoTranslationPID,
                SwerveConstants.autoRotationPID
            ),
            RobotConfig(
                PhysicalConstants.robotMass,
                KilogramSquareMeters.of(0.02),
                ModuleConfig(
                    SwerveConstants.WHEEL_RADIUS_METERS,
                    SwerveConstants.MAX_SPEED_MPS,
                    SwerveConstants.WHEEL_COF,
                    SwerveConstants.driveMotorGearbox.withReduction(DRIVE_GEAR_RATIO),
                    SwerveConstants.DRIVE_CURRENT_LIMIT_AUTO.toDouble(),
                    1
                ),
                *SwerveConstants.moduleTranslations
            ),
            { DriverStation.getAlliance().getOrElse { DriverStation.Alliance.Blue } == DriverStation.Alliance.Red },
            Drivetrain
        )
        //Pathfinding.setPathfinder(LocalADStarAK())
//        PathPlannerLogging.setLogActivePathCallback { activePath:List<Pose2d> ->
//            Logger.recordOutput(
//                "Odometry/Trajectory", *activePath.toTypedArray()
//            )
//        }

        PathPlannerLogging.setLogTargetPoseCallback { targetPose: Pose2d ->
            Logger.recordOutput("Odometry/TrajectorySetpoint", targetPose)
        }

        autoChooser = LoggedDashboardChooser("Auto Chooser")

        autoChooser.addDefaultOption("None", Commands.none())

        autoChooser.addOption("Leave", AutoBuilder.buildAuto("Leave"))

        autoChooser.addOption("Left 3", AutoBuilder.buildAuto("Left 3 L4"))
        autoChooser.addOption("Right 3", AutoBuilder.buildAuto("Right 3 L4"))

        autoChooser.addOption("Center", AutoBuilder.buildAuto("Center 1 L4"))
        autoChooser.addOption("Leave", AutoBuilder.buildAuto("Leave"))
    }


    fun getAutonomousCommand(): Command
    {
        return autoChooser.get()
    }
}