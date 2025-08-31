package org.sert2521.offseason2025

import edu.wpi.first.apriltag.AprilTagFieldLayout
import edu.wpi.first.apriltag.AprilTagFields
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Translation2d
import org.sert2521.offseason2025.utils.ManipulatorGoal
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object ManipulatorTargets {
    val stow = ManipulatorGoal(0.0, 0.0)
    val l1 = ManipulatorGoal(0.0, 0.0)
    val l2 = ManipulatorGoal(0.0, 0.0)
    val l3 = ManipulatorGoal(0.0, 0.0)
    val l4 = ManipulatorGoal(0.0, 0.0)
}

object VisionTargetPositions {
    private const val CX = 4.49
    private const val CY = 4.03
    private const val HD = PI / 3
    private const val RADIUS = 1.25
    private const val SHIFT = 0.16

    val layout = AprilTagFieldLayout.loadField(AprilTagFields.k2025ReefscapeWelded)

    val reefPositionsLeft = mutableListOf(
        //Left I think
        Pose2d(
            CX - cos(6 * HD) * RADIUS - sin(6 * HD) * SHIFT,
            CY - sin(6 * HD) * RADIUS + cos(6 * HD) * SHIFT, Rotation2d(6 * HD)
        ),
        Pose2d(
            CX - cos(5 * HD) * RADIUS - sin(5 * HD) * SHIFT,
            CY - sin(5 * HD) * RADIUS + cos(5 * HD) * SHIFT, Rotation2d(5 * HD)
        ),
        Pose2d(
            CX - cos(4 * HD) * RADIUS - sin(4 * HD) * SHIFT,
            CY - sin(4 * HD) * RADIUS + cos(4 * HD) * SHIFT, Rotation2d(4 * HD)
        ),
        Pose2d(
            CX - cos(3 * HD) * RADIUS - sin(3 * HD) * SHIFT,
            CY - sin(3 * HD) * RADIUS + cos(3 * HD) * SHIFT, Rotation2d(3 * HD)
        ),
        Pose2d(
            CX - cos(2 * HD) * RADIUS - sin(2 * HD) * SHIFT,
            CY - sin(2 * HD) * RADIUS + cos(2 * HD) * SHIFT, Rotation2d(2 * HD)
        ),
        Pose2d(
            CX - cos(1 * HD) * RADIUS - sin(1 * HD) * SHIFT,
            CY - sin(1 * HD) * RADIUS + cos(1 * HD) * SHIFT, Rotation2d(1 * HD)
        ),

        Pose2d(
            CX - cos(6 * HD) * RADIUS - sin(6 * HD) * SHIFT,
            CY - sin(6 * HD) * RADIUS + cos(6 * HD) * SHIFT, Rotation2d(6 * HD)
        ).flip(),
        Pose2d(
            CX - cos(5 * HD) * RADIUS - sin(5 * HD) * SHIFT,
            CY - sin(5 * HD) * RADIUS + cos(5 * HD) * SHIFT, Rotation2d(5 * HD)
        ).flip(),
        Pose2d(
            CX - cos(4 * HD) * RADIUS - sin(4 * HD) * SHIFT,
            CY - sin(4 * HD) * RADIUS + cos(4 * HD) * SHIFT, Rotation2d(4 * HD)
        ).flip(),
        Pose2d(
            CX - cos(3 * HD) * RADIUS - sin(3 * HD) * SHIFT,
            CY - sin(3 * HD) * RADIUS + cos(3 * HD) * SHIFT, Rotation2d(3 * HD)
        ).flip(),
        Pose2d(
            CX - cos(2 * HD) * RADIUS - sin(2 * HD) * SHIFT,
            CY - sin(2 * HD) * RADIUS + cos(2 * HD) * SHIFT, Rotation2d(2 * HD)
        ).flip(),
        Pose2d(
            CX - cos(1 * HD) * RADIUS - sin(1 * HD) * SHIFT,
            CY - sin(1 * HD) * RADIUS + cos(1 * HD) * SHIFT, Rotation2d(1 * HD)
        ).flip(),
    )

    val reefPositionsRight = mutableListOf(
        //right I think
        Pose2d(
            CX - cos(1 * HD) * RADIUS + sin(1 * HD) * SHIFT,
            CY - sin(1 * HD) * RADIUS - cos(1 * HD) * SHIFT, Rotation2d(1 * HD)
        ),
        Pose2d(
            CX - cos(2 * HD) * RADIUS + sin(2 * HD) * SHIFT,
            CY - sin(2 * HD) * RADIUS - cos(2 * HD) * SHIFT, Rotation2d(2 * HD)
        ),
        Pose2d(
            CX - cos(3 * HD) * RADIUS + sin(3 * HD) * SHIFT,
            CY - sin(3 * HD) * RADIUS - cos(3 * HD) * SHIFT, Rotation2d(3 * HD)
        ),
        Pose2d(
            CX - cos(4 * HD) * RADIUS + sin(4 * HD) * SHIFT,
            CY - sin(4 * HD) * RADIUS - cos(4 * HD) * SHIFT, Rotation2d(4 * HD)
        ),
        Pose2d(
            CX - cos(5 * HD) * RADIUS + sin(5 * HD) * SHIFT,
            CY - sin(5 * HD) * RADIUS - cos(5 * HD) * SHIFT, Rotation2d(5 * HD)
        ),
        Pose2d(
            CX - cos(6 * HD) * RADIUS + sin(6 * HD) * SHIFT,
            CY - sin(6 * HD) * RADIUS - cos(6 * HD) * SHIFT, Rotation2d(6 * HD)
        ),

        Pose2d(
            CX - cos(1 * HD) * RADIUS + sin(1 * HD) * SHIFT,
            CY - sin(1 * HD) * RADIUS - cos(1 * HD) * SHIFT, Rotation2d(1 * HD)
        ).flip(),
        Pose2d(
            CX - cos(2 * HD) * RADIUS + sin(2 * HD) * SHIFT,
            CY - sin(2 * HD) * RADIUS - cos(2 * HD) * SHIFT, Rotation2d(2 * HD)
        ).flip(),
        Pose2d(
            CX - cos(3 * HD) * RADIUS + sin(3 * HD) * SHIFT,
            CY - sin(3 * HD) * RADIUS - cos(3 * HD) * SHIFT, Rotation2d(3 * HD)
        ).flip(),
        Pose2d(
            CX - cos(4 * HD) * RADIUS + sin(4 * HD) * SHIFT,
            CY - sin(4 * HD) * RADIUS - cos(4 * HD) * SHIFT, Rotation2d(4 * HD)
        ).flip(),
        Pose2d(
            CX - cos(5 * HD) * RADIUS + sin(5 * HD) * SHIFT,
            CY - sin(5 * HD) * RADIUS - cos(5 * HD) * SHIFT, Rotation2d(5 * HD)
        ).flip(),
        Pose2d(
            CX - cos(6 * HD) * RADIUS + sin(6 * HD) * SHIFT,
            CY - sin(6 * HD) * RADIUS - cos(6 * HD) * SHIFT, Rotation2d(6 * HD)
        ).flip(),
    )

    /** Flips this [Pose2d] to the opposite side of a rotated field. */
    fun Pose2d.flip() = Pose2d(translation.flip(), rotation.flip())

    /** Flips this [Translation2d] to the opposite side of a rotated field. */
    fun Translation2d.flip() = Translation2d(layout.fieldLength - x, layout.fieldWidth - y)

    /** Flips this [Rotation2d] to the opposite side of a rotated field. */
    fun Rotation2d.flip(): Rotation2d = this.rotateBy(Rotation2d.k180deg)
}