package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous
class Auto : LinearOpMode() {
    override fun runOpMode() {
        val hardware = Hardware(this)

        hardware.initSkystoneDetector()

        telemetry.addLine("Initialization Finished")
        telemetry.update()

        waitForStart()

        telemetry.addData("Skystones", hardware.skystonePositions)
        telemetry.update()

        hardware.apply {
            setPullerPosition(0.0)

            backward(2600, 1.0, 5.0)
            right(1000, 1.0, 5.0)
            setPullerPosition(.1)
            wait(.5)
            forward(1700, 1.0, 5.0)
            turnLeft(1800, 1.0, 5.0)
            backward(3500, 1.0, 5.0)
            setPullerPosition(0.0)
            wait(.5)
            forward(5300, 1.0, 5.0)
            turnRight(1600, 1.0, 5.0)
            backward(1500, 1.0, 5.0)
            setPullerPosition(.1)
            wait(.5)
            forward(1700, 1.0, 5.0)
            turnLeft(1800, 1.0, 5.0)
            backward(5300, 1.0, 5.0)
            setPullerPosition(0.0)
            forward(1000, 1.0, 5.0)
        }
    }
}
