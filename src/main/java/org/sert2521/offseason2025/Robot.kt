package org.sert2521.offseason2025

import edu.wpi.first.hal.FRCNetComm.tInstances
import edu.wpi.first.hal.FRCNetComm.tResourceType
import edu.wpi.first.hal.HAL
import edu.wpi.first.networktables.BooleanTopic
import edu.wpi.first.wpilibj.Threads
import edu.wpi.first.wpilibj.util.WPILibVersion
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj2.command.button.NetworkButton
import org.littletonrobotics.junction.LogFileUtil
import org.littletonrobotics.junction.LoggedRobot
import org.littletonrobotics.junction.Logger
import org.littletonrobotics.junction.networktables.NT4Publisher
import org.littletonrobotics.junction.wpilog.WPILOGReader
import org.littletonrobotics.junction.wpilog.WPILOGWriter
import org.sert2521.offseason2025.MetaConstants.currentRealityMode
import org.sert2521.offseason2025.subsystems.dispenser.Dispenser
import org.sert2521.offseason2025.subsystems.drivetrain.Drivetrain
import org.sert2521.offseason2025.subsystems.wrist.Wrist

/**
 * The functions in this object (which basically functions as a singleton class) are called automatically
 * corresponding to each mode, as described in the TimedRobot documentation.
 * This is written as an object rather than a class since there should only ever be a single instance, and
 * it cannot take any constructor arguments. This makes it a natural fit to be an object in Kotlin.
 *
 * If you change the name of this object or its package after creating this project, you must also update
 * the `Main.kt` file in the project. (If you use the IDE's Rename or Move refactorings when renaming the
 * object or package, it will get changed everywhere.)
 */
object Robot : LoggedRobot() {


    private var autonomousCommand: Command? = null


    init {
        // Report the use of the Kotlin Language for "FRC Usage Report" statistics.
        // Please retain this line so that Kotlin's growing use by teams is seen by FRC/WPI.
        HAL.report(tResourceType.kResourceType_Language, tInstances.kLanguage_Kotlin, 0, WPILibVersion.Version)
        HAL.report(tResourceType.kResourceType_LoggingFramework, tInstances.kFramework_AdvantageKit)
        // Access the objects so that it is initialized. This will perform all our
        // button bindings, and put our autonomous chooser on the dashboard.

        when (currentRealityMode) {
            MetaConstants.RealityMode.REAL -> {
                // Running on a real robot, log to a USB stick ("/U/logs")
                // Logger.addDataReceiver(WPILOGWriter())
                Logger.addDataReceiver(NT4Publisher())
            }

            MetaConstants.RealityMode.SIM ->
                // Running a physics simulator, log to NT
                Logger.addDataReceiver(NT4Publisher())

            MetaConstants.RealityMode.REPLAY -> {
                // Replaying a log, set up replay source
                setUseTiming(false) // Run as fast as possible
                val logPath = LogFileUtil.findReplayLog()
                Logger.setReplaySource(WPILOGReader(logPath))
                Logger.addDataReceiver(WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim")))
            }
        }

        // Initialize URCL
        //Logger.registerURCL(URCL.startExternal());

        // Start AdvantageKit logger
        Logger.start()

        VisionTargetPositions
        Drivetrain
        Input
        Wrist
        Autos
    }


    override fun robotPeriodic() {
        // Runs the Scheduler. This is responsible for polling buttons, adding
        // newly-scheduled commands, running already-scheduled commands, removing
        // finished or interrupted commands, and running subsystem periodic() methods.
        // This must be called from the robot's periodic block in order for anything in
        // the Command-based framework to work.
        CommandScheduler.getInstance().run()
    }

    override fun disabledInit() {

    }

    override fun disabledPeriodic() {

    }

    override fun autonomousInit() {
        autonomousCommand = Autos.getAutonomousCommand()
        autonomousCommand?.schedule()
    }

    override fun autonomousPeriodic() {

    }

    override fun teleopInit() {
        autonomousCommand?.cancel()
        Dispenser.resetEncoder()
    }

    /** This method is called periodically during operator control.  */
    override fun teleopPeriodic() {

    }

    override fun testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll()
    }

    override fun testPeriodic() {

    }

    override fun simulationInit() {

    }

    override fun simulationPeriodic() {

    }
}