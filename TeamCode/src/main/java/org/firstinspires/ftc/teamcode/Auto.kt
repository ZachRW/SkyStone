package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous
class Auto : LinearOpMode() {
    override fun runOpMode() {
        val hardware = AutoHardware(this)

        hardware.initSkystoneDetector()

        telemetry.addLine("Initialization Finished")
        telemetry.update()

        waitForStart()

        hardware.apply {
            setPullerPositions(1.0, 0.0)

            val stonePosition = skystonePosition
            telemetry.addData("Skystones", stonePosition)
            telemetry.update()

            when (stonePosition) {
                0 -> {
                    //get block
                    backward(2600, 1.0, 5.0)
                    right(1050, 1.0, 5.0)
                    setPullerPositions(1.0, 0.15)
                    wait(.5)
                    //move block
                    forward(1700, 1.0, 5.0)
                    turnLeft(1700, 1.0, 5.0)
                    backward(3000, 1.0, 5.0)
                    turnRight(1600, 1.0, 5.0)
                    setPullerPositions(1.0, 0.0)
                    //move plate
                    right(3500, 1.0, 5.0)
                    backward(1000, 1.0, 5.0)
                    right(500, 1.0, 5.0)
                    setPullerPositions(.4, .2)
                    backward(300, .75, 5.0)

//                    forward(3200, .5, 3.5)
                    move(1600, 3200, 1600, 3200,
                            .5, .75, .5, .75,
                            5.0, "Custom")

                    setPullerPositions(1.0, 0.0)
                    wait(.3)
                    left(3000, .75, 5.0)
                    backward(1500, 1.0, 5.0)
                    turnLeft(1700, 1.0, 5.0)
                    //get block
                    forward(5800, 1.0, 5.0)
                    turnRight(1600, 1.0, 5.0)
                    backward(1100, 1.0, 4.0)
                    setPullerPositions(1.0, 0.15)
                    wait(.5)
                    //move block
                    forward(1200, 1.0, 5.0)
                    turnLeft(1800, 1.0, 5.0)
                    backward(5300, 1.0, 5.0)
                    setPullerPositions(1.0, 0.0)
                    forward(1000, 1.0, 5.0)
                }

                1 -> {
                }

                2 -> {
                }
            }
        }
    }
}
