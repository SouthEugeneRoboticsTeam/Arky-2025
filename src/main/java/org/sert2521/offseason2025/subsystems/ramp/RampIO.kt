package org.sert2521.offseason2025.subsystems.ramp

import org.team9432.annotation.Logged

interface RampIO {
    @Logged
    open class RampIOInputs {
        var appliedVolts = 0.0
        var currentAmps = 0.0
    }

    fun updateInputs(inputs: RampIOInputs)

    fun setSpeed(speed:Double)
}