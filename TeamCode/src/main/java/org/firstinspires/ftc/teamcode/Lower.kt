package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous
class Lower : LinearOpMode() {
    override fun runOpMode() {
        val hardware = AutoHardware(this)

        telemetry.addLine("Initialization Finished")
        telemetry.update()

        waitForStart()

        with(hardware) {
            setLeftPullerPosition(PullerPosition.DOWN)
            setRightPullerPosition(PullerPosition.DOWN)
            wait(1.0)
        }
    }
}
