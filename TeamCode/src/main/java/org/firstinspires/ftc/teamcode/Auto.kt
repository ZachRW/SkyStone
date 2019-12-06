package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous
class Auto : LinearOpMode() {
    override fun runOpMode() {
        val hardware = AutoHardware(this).apply { initSkystoneDetector() }

        telemetry.addLine("Initialization Finished")
        telemetry.update()

        waitForStart()

        with(hardware) {
            setPullerPositions(1.0, 0.0)

            val stonePosition = skystonePosition
            telemetry.addData("Skystones", stonePosition)
            telemetry.update()

            when (stonePosition) {
                0 -> {
                    //get block
                    backward(2600)
                    right(1050)
                    setPullerPositions(1.0, 0.15)
                    wait(.5)
                    //move block
                    forward(1700)
                    turnLeft(1700)
                    backward(3000)
                    turnRight(1600)
                    setPullerPositions(1.0, 0.0)
                    //move plate
                    right(3500)
                    backward(1000)
                    right(500)
                    setPullerPositions(.4, .2)
                    backward(300, .75)

//                    forward(3200, .5, 3.5)
                    move(
                        1600, 3200, 1600, 3200,
                        .5, .75, .5, .75,
                        5.0, "Custom"
                    )

                    setPullerPositions(1.0, 0.0)
                    wait(.3)
                    left(3000, .75)
                    backward(1500)
                    turnLeft(1700)
                    //get block
                    forward(5800)
                    turnRight(1600)
                    backward(1100, timeoutS = 4.0)
                    setPullerPositions(1.0, 0.15)
                    wait(.5)
                    //move block
                    forward(1200)
                    turnLeft(1800)
                    backward(5300)
                    setPullerPositions(1.0, 0.0)
                    forward(1000)
                }

                1 -> {
                }

                2 -> {
                }
            }
        }
    }
}
