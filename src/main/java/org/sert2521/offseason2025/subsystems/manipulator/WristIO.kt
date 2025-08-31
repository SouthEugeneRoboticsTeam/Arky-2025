package org.sert2521.offseason2025.subsystems.manipulator

import org.team9432.annotation.Logged

interface WristIO {
    @Logged
    open class WristIOInputs{
        var appliedVolts = 0.0
        var currentAmps = 0.0

        var positionRadians = 0.0
        var velocityRadiansPerSecond = 0.0
    }

    fun updateInputs(inputs: WristIOInputs)

    fun setVoltage(volts:Double)

    fun setReference(setpointPosition:Double)
}