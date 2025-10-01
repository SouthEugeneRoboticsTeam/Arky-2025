package org.sert2521.offseason2025.subsystems.dispenser

import com.revrobotics.spark.SparkBase
import com.revrobotics.spark.SparkLowLevel
import com.revrobotics.spark.SparkMax
import com.revrobotics.spark.config.SparkBaseConfig
import com.revrobotics.spark.config.SparkMaxConfig
import edu.wpi.first.wpilibj.DigitalInput
import org.sert2521.offseason2025.DispenserConstants
import org.sert2521.offseason2025.DispenserConstants.DISPENSER_D
import org.sert2521.offseason2025.DispenserConstants.DISPENSER_P
import org.sert2521.offseason2025.ElectronicIDs.DISPENSER_BEAMBREAK_ID
import org.sert2521.offseason2025.ElectronicIDs.DISPENSER_ID
import org.sert2521.offseason2025.ElectronicIDs.RAMP_BEAMBREAK_ID

class DispenserIOSpark : DispenserIO {
    val motor = SparkMax(DISPENSER_ID, SparkLowLevel.MotorType.kBrushless)

    val rampBeambreak = DigitalInput(RAMP_BEAMBREAK_ID)
    val dispenserBeambreak = DigitalInput(DISPENSER_BEAMBREAK_ID)

    init {
        val config = SparkMaxConfig()

        config.idleMode(SparkBaseConfig.IdleMode.kBrake)
            .inverted(false)
            .smartCurrentLimit(40)

        config.closedLoop
            .p(DISPENSER_P)
            .d(DISPENSER_D)
    }

    override fun updateInputs(inputs: DispenserIO.DispenserIOInputs) {
        inputs.appliedVolts = motor.appliedOutput * motor.busVoltage
        inputs.currentAmps = motor.outputCurrent
        inputs.positionEncoder = motor.encoder.position
        inputs.rampBeambreakClear = rampBeambreak.get()
        inputs.dispenserBeambreakClear = dispenserBeambreak.get()
    }

    override fun setSpeed(speed: Double) {
        motor.set(0.0)
    }

    override fun resetEncoder() {
        motor.encoder.position = 0.0
    }

    override fun setPIDReference(setpoint:Double) {
        motor.closedLoopController.setReference(
            setpoint,
            SparkBase.ControlType.kPosition
        )
    }
}