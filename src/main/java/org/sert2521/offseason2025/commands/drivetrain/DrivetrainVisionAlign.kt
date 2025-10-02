package org.sert2521.offseason2025.commands.drivetrain

import edu.wpi.first.math.MathUtil
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.controller.ProfiledPIDController
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
import kotlin.div
import kotlin.math.*

class DrivetrainVisionAlign(val alignLeft: Boolean) : Command() {
    private val drivePID = ProfiledPIDController(
        SwerveConstants.VISION_ALIGN_DRIVE_P,
        SwerveConstants.VISION_ALIGN_DRIVE_I,
        SwerveConstants.VISION_ALIGN_DRIVE_D,
        SwerveConstants.visionAlignProfile
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
        val test = cos(Drivetrain.getPose().rotation.radians) * hypot(
            Drivetrain.getChassisSpeeds().vxMetersPerSecond,
            Drivetrain.getChassisSpeeds().vyMetersPerSecond
        )
        drivePID.reset(hypot(xError, yError))
        anglePID.reset()

    }

    override fun execute() {
        xError = Drivetrain.getPose().x - targetPose.x
        yError = Drivetrain.getPose().y - targetPose.y

        angle = atan2(yError, xError)

        driveResult = drivePID.calculate(hypot(xError, yError), 0.0)
        if (drivePID.atGoal()) {
            driveResult = 0.0
        }

        driveResult += drivePID.setpoint.velocity * SwerveConstants.VISION_ALIGN_DRIVE_V
        angleResult = anglePID.calculate(Drivetrain.getPose().rotation.radians, targetPose.rotation.radians)

        if (anglePID.atSetpoint()) {
            angleResult = 0.0
        }

        Logger.recordOutput("Input Chassis Speeds", ChassisSpeeds(driveResult * cos(angle), driveResult * sin(angle), angleResult))
        accelLimitedChassisSpeeds =
            AccelLimiterUtil.accelLimitChassisSpeeds(
                ChassisSpeeds(driveResult * cos(angle), driveResult * sin(angle), angleResult),
                MathUtil.interpolate(
                    SwerveConstants.DRIVE_ACCEL_FAST, SwerveConstants.DRIVE_ACCEL_SLOW,
                    Elevator.getPosition() / ElevatorConstants.l4.elevatorGoalMeters
                ),
                Rotation2d()
            )

        Logger.recordOutput("Output Chassis Speeds", accelLimitedChassisSpeeds)
        Drivetrain.driveRobotOriented(accelLimitedChassisSpeeds)
    }

    override fun isFinished(): Boolean {
        return false
    }

    override fun end(interrupted: Boolean) {}
}
