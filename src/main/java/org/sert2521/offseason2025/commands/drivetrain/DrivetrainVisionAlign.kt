package org.sert2521.offseason2025.commands.drivetrain

import edu.wpi.first.math.MathUtil
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.filter.Debouncer
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.wpilibj2.command.Command
import org.littletonrobotics.junction.Logger
import org.sert2521.offseason2025.ElevatorConstants
import org.sert2521.offseason2025.subsystems.drivetrain.Drivetrain
import org.sert2521.offseason2025.subsystems.drivetrain.SwerveConstants
import org.sert2521.offseason2025.subsystems.elevator.Elevator
import kotlin.math.*

class DrivetrainVisionAlign(val alignLeft: Boolean) : Command() {
    private val driveProfile = TrapezoidProfile(SwerveConstants.visionAlignProfile)
    private var currentProfileState = TrapezoidProfile.State(0.0, 0.0)

    private val drivePID = PIDController(
        SwerveConstants.VISION_ALIGN_DRIVE_P,
        SwerveConstants.VISION_ALIGN_DRIVE_I,
        SwerveConstants.VISION_ALIGN_DRIVE_D,
    )

    private val anglePID = PIDController(
        SwerveConstants.VISION_ALIGN_ROT_P,
        SwerveConstants.VISION_ALIGN_ROT_I,
        SwerveConstants.VISION_ALIGN_ROT_D
    )

    private var targetPose = Pose2d()


    private var xError = 0.0
    private var yError = 0.0

    private var angle = 0.0
    private var driveResult = 0.0
    private var angleResult = 0.0

    private var accelLimitedChassisSpeeds = ChassisSpeeds()

    private var alignDebouncer = Debouncer(0.25, Debouncer.DebounceType.kRising)

    init {
        anglePID.enableContinuousInput(PI, -PI)
        drivePID.setTolerance(0.03)
        anglePID.setTolerance(0.02)
        addRequirements(Drivetrain)
    }

    override fun initialize() {
        targetPose = Drivetrain.getNearestTargetReef(alignLeft)
        Logger.recordOutput("Target Pose", targetPose)
        xError = Drivetrain.getPose().x - targetPose.x
        yError = Drivetrain.getPose().y - targetPose.y

        currentProfileState = TrapezoidProfile.State(hypot(xError, yError), 0.0)
        driveProfile.calculate(0.02, currentProfileState, TrapezoidProfile.State(0.0, 0.0))

        drivePID.reset()
        anglePID.reset()
    }

    override fun execute() {
        currentProfileState = driveProfile.calculate(0.02, currentProfileState, TrapezoidProfile.State(0.0, 0.0))

        xError = Drivetrain.getPose().x - targetPose.x
        yError = Drivetrain.getPose().y - targetPose.y

        angle = atan2(yError, xError)


        driveResult = drivePID.calculate(hypot(xError, yError), currentProfileState.position)
        if (drivePID.atSetpoint()) {
            driveResult = 0.0
        }

        driveResult += currentProfileState.velocity * SwerveConstants.VISION_ALIGN_DRIVE_V
        angleResult = anglePID.calculate(Drivetrain.getPose().rotation.radians, targetPose.rotation.radians)

        if (anglePID.atSetpoint()) {
            angleResult = 0.0
        }

        accelLimitedChassisSpeeds =
            AccelLimiterUtil.accelLimitChassisSpeeds(
                ChassisSpeeds(driveResult * cos(angle), driveResult * sin(angle), angleResult),
                MathUtil.interpolate(
                    SwerveConstants.DRIVE_ACCEL_FAST, SwerveConstants.DRIVE_ACCEL_SLOW,
                    Elevator.getPosition() / ElevatorConstants.l4.elevatorGoalMeters
                ),
                Rotation2d()
            )

        Drivetrain.driveRobotOriented(accelLimitedChassisSpeeds)
    }

    override fun isFinished(): Boolean {
        return false
    }

    override fun end(interrupted: Boolean) {}
}
