package org.sert2521.offseason2025.subsystems.wrist

import com.revrobotics.spark.ClosedLoopSlot
import com.revrobotics.spark.SparkBase
import com.revrobotics.spark.SparkLowLevel
import com.revrobotics.spark.SparkMax
import com.revrobotics.spark.config.ClosedLoopConfig
import com.revrobotics.spark.config.SparkBaseConfig
import com.revrobotics.spark.config.SparkMaxConfig
import org.sert2521.offseason2025.ElectronicIDs.WRIST_ID
import org.sert2521.offseason2025.WristConstants.WRIST_ABS_ENCODER_MULTIPLIER
import org.sert2521.offseason2025.WristConstants.WRIST_CURRENT_LIMIT
import org.sert2521.offseason2025.WristConstants.WRIST_D
import org.sert2521.offseason2025.WristConstants.WRIST_G
import org.sert2521.offseason2025.WristConstants.WRIST_P
import kotlin.math.PI
import kotlin.math.cos

class WristIOSpark:WristIO {
    private val motor = SparkMax(WRIST_ID, SparkLowLevel.MotorType.kBrushless)
    private val config = SparkMaxConfig()
    init {
        config.idleMode(SparkBaseConfig.IdleMode.kBrake)
            .inverted(false)
            .smartCurrentLimit(WRIST_CURRENT_LIMIT)

        config.absoluteEncoder
            .inverted(false)
            .positionConversionFactor(WRIST_ABS_ENCODER_MULTIPLIER)
            .velocityConversionFactor(WRIST_ABS_ENCODER_MULTIPLIER / 60.0)

        config.encoder
            .inverted(false)
            .positionConversionFactor(WRIST_ABS_ENCODER_MULTIPLIER * (16.0 / 24.0))
            .velocityConversionFactor(WRIST_ABS_ENCODER_MULTIPLIER * (16.0 / 24.0) / 60.0)

        config.softLimit

        config.closedLoop
            .p(WRIST_P)
            .d(WRIST_D)
            .feedbackSensor(ClosedLoopConfig.FeedbackSensor.kAbsoluteEncoder)
    }

    override fun updateInputs(inputs: WristIO.WristIOInputs) {
        inputs.appliedVolts = motor.appliedOutput * motor.busVoltage
        inputs.currentAmps = motor.outputCurrent

        inputs.positionRotations = motor.absoluteEncoder.position
        inputs.velocityRotationsPerSecond = motor.absoluteEncoder.velocity
    }

    override fun setVoltage(volts: Double) {
        motor.setVoltage(volts)
    }

    override fun setReference(setpointPosition: Double) {
        motor.closedLoopController.setReference(
            setpointPosition,
            SparkBase.ControlType.kPosition,
            ClosedLoopSlot.kSlot0,
            WRIST_G * cos(setpointPosition * 2 * PI)
        )
    }

}