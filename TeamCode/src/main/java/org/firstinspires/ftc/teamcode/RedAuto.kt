package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous
class RedAuto : LinearOpMode() {
    override fun runOpMode() {
        val hardware = AutoHardware(this).apply { initSkystoneDetector() }

        telemetry.addLine("Initialization Finished")
        telemetry.update()

        waitForStart()

        with(hardware) {
            setClawPosition(0.5)
            setFlickerPosition(1.0)
            setLeftPullerPosition(PullerPosition.UP)
            setRightPullerPosition(PullerPosition.DOWN)
            wait(1.5)

            val stonePosition = skystonePosition
            telemetry.addData("Skystones", stonePosition)
            telemetry.update()

            setRightPullerPosition(PullerPosition.UP)

            when (stonePosition) {
                2, -1 -> {
                    // get first stone
                    forward(2550)
                    right(600)
                    setRightPullerPosition(PullerPosition.DOWN)
                    forward(200)
                    wait(.25)
                    // move stone
                    backward(1700)
                    turnRight(1650, timeoutS = 1.5)
                    forward(4300, 0.7, timeoutS = 5.0)
                    setRightPullerPosition(PullerPosition.UP)
                    wait(.5)

                    backward(1200)
                    left(3000)
                }

                1 -> {
                    // get first stone
                    forward(2750)
                    right(650)
                    setLeftPullerPosition(PullerPosition.DOWN)
                    wait(.3)
                    // move stone
                    backward(1000)
                    turnRight(1600, timeoutS = 1.5)
                    forward(4300, 0.9)
                    setLeftPullerPosition(PullerPosition.UP)
                    wait(.2)

                    backward(1200)
                    left(1000)
                }

                0 -> {
                    // get first stone
                    forward(2750)
                    left(100)
                    turnLeft(100, 1.0, 1.0)
                    setLeftPullerPosition(PullerPosition.DOWN)
                    wait(.3)
                    // move stone
                    backward(1000)
                    turnRight(1450, timeoutS = 1.5)
                    forward(5200, 0.9)
                    setLeftPullerPosition(PullerPosition.UP)
                    wait(.2)

                    backward(1200)
                    left(1000)
                }
            }
        }
    }
}
