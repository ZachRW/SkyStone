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
            setFlickerPosition(0.5)
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

                    // get second stone
                    backward(5600, 0.7)
                    turnLeft(1550)
                    forward(2000)
                    setLeftPullerPosition(PullerPosition.DOWN)
                    wait(.5)
                    // move stone
                    backward(1650)
                    turnRight(1600, timeoutS = 1.5)
                    forward(5600, 0.9)
                    setLeftPullerPosition(PullerPosition.UP)
                    backward(1000)
                }

                1 -> {
                    // get first stone
                    forward(2750)
                    right(800)
                    setLeftPullerPosition(PullerPosition.DOWN)
                    wait(.3)
                    // move stone
                    backward(1000)
                    turnRight(1650, timeoutS = 1.5)
                    forward(4300, 0.9)
                    setLeftPullerPosition(PullerPosition.UP)
                    wait(.2)

                    // get second stone
                    backward(6250)
                    turnLeft(1500)
                    forward(1800)
                    setLeftPullerPosition(PullerPosition.DOWN)
                    wait(.5)
                    // move stone
                    backward(1650)
                    turnRight(1600, timeoutS = 1.5)
                    forward(6400, 0.9)
                    setLeftPullerPosition(PullerPosition.UP)
                    backward(1200)
                }

                0 -> {
                    // get first stone
                    forward(2750)
                    setLeftPullerPosition(PullerPosition.DOWN)
                    wait(.3)
                    // move stone
                    backward(1000)
                    turnRight(1650, timeoutS = 1.5)
                    forward(5200, 0.9)
                    setLeftPullerPosition(PullerPosition.UP)
                    wait(.2)

                    // get second stone
                    backward(6700)
                    turnLeft(1550)
                    left(500)
                    forward(1500)
                    setLeftPullerPosition(PullerPosition.DOWN)
                    wait(.5)
                    // move stone
                    backward(1650)
                    right(500)
                    turnRight(1700, timeoutS = 1.5)
                    forward(6600, 0.9)
                    setLeftPullerPosition(PullerPosition.UP)
                    backward(1200)
                }
            }
        }
    }
}
