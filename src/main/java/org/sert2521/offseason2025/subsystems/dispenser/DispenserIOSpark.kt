package org.sert2521.offseason2025.subsystems.dispenser

import com.revrobotics.spark.SparkLowLevel
import com.revrobotics.spark.SparkMax
import com.revrobotics.spark.config.SparkBaseConfig
import com.revrobotics.spark.config.SparkMaxConfig
import edu.wpi.first.wpilibj.DigitalInput
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
    }

    override fun updateInputs(inputs: DispenserIO.DispenserIOInputs) {
        inputs.appliedVolts = motor.appliedOutput * motor.busVoltage
        inputs.currentAmps = motor.outputCurrent
        inputs.rampBeambreakClear = rampBeambreak.get()
        inputs.dispenserBeambreakClear = dispenserBeambreak.get()
    }

    override fun setMotor(speed: Double) {
        motor.set(0.0)
    }
}