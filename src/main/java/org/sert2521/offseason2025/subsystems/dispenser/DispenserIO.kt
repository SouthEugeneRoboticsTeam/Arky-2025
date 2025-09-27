package org.sert2521.offseason2025.subsystems.dispenser

import org.team9432.annotation.Logged

interface DispenserIO {
    @Logged
    open class DispenserIOInputs {
        var appliedVolts = 0.0
        var currentAmps = 0.0
        var rampBeambreakClear = false
        var dispenserBeambreakClear = false
    }

    fun updateInputs(inputs: DispenserIOInputs)

    fun setSpeed(speed: Double)
}