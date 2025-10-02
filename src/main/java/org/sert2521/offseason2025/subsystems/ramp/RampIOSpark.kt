package org.sert2521.offseason2025.subsystems.ramp

import com.revrobotics.spark.SparkBase
import com.revrobotics.spark.SparkLowLevel
import com.revrobotics.spark.SparkMax
import com.revrobotics.spark.config.SparkBaseConfig
import com.revrobotics.spark.config.SparkMaxConfig
import org.sert2521.offseason2025.ElectronicIDs.RAMP_ID

class RampIOSpark : RampIO {
    private val motor = SparkMax(RAMP_ID, SparkLowLevel.MotorType.kBrushless)

    init {
        val config = SparkMaxConfig()
        config.inverted(true)
            .smartCurrentLimit(30)
            .idleMode(SparkBaseConfig.IdleMode.kBrake)

        motor.configure(config, SparkBase.ResetMode.kResetSafeParameters, SparkBase.PersistMode.kPersistParameters)
    }

    override fun updateInputs(inputs: RampIO.RampIOInputs) {
        inputs.currentAmps = motor.outputCurrent
        inputs.appliedVolts = motor.appliedOutput * motor.busVoltage
    }

    override fun setSpeed(speed: Double) {
        motor.set(speed)
    }
}