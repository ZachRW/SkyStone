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
            setPullerPositions(1.0, 0.3)
            wait(1.5)

            val stonePosition = skystonePosition
            telemetry.addData("Skystones", stonePosition)
            telemetry.update()

            setPullerPositions(1.0, 0.0)

            when (stonePosition) {
                0 -> {
                    // get block
                    backward(2750)
                    right(920)
                    setPullerPositions(1.0, 0.3)
                    wait(.25)
                    // move block
                    forward(1700)
                    turnLeft(1650)
                    backward(3500, .9)
                    setPullerPositions(1.0, 0.0)
                    wait(.5)
                    forward(5850)
                    turnRight(1600)
                    backward(850)
                    setPullerPositions(1.0, 0.3)
                    wait(.5)
                    forward(1650)
                    turnLeft(1600)
                    backward(5600, .9)
                    setPullerPositions(1.0, 0.0)
                    forward(1000)
                }

                1 -> {
                    // get block
                    backward(2750)
                    setPullerPositions(1.0, 0.3)
                    right(250)
                    wait(.3)
                    // move block
                    forward(1000)
                    turnLeft(1650)
                    backward(3700, .9)
                    setPullerPositions(1.0, 0.0)
                    wait(.2)
                    forward(100)
                    turnRight(120, 1.0, 1.0)
                    forward(6200)
                    turnRight(1300)
                    backward(1200)
                    setPullerPositions(1.0, 0.3)
                    wait(.5)
                    forward(1650)
                    turnLeft(1600)
                    backward(6400, .9)
                    setPullerPositions(1.0, 0.0)
                    forward(1200)
                }

                2 -> {
                    // get block
                    backward(2750)
                    left(500)
                    setPullerPositions(1.0, 0.3)
                    wait(.3)
                    // move block
                    forward(1000)
                    turnLeft(1650)
                    backward(3600, .9)
                }
            }
        }
    }
}
