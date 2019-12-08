package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous
class BlueAuto : LinearOpMode() {
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
                0, -1 -> {
                    // get first stone
                    backward(2750)
                    right(920)
                    setRightPullerPosition(PullerPosition.DOWN)
                    wait(.25)
                    // move stone
                    forward(1700)
                    turnLeft(1650)
                    backward(3500, .9)
                    setRightPullerPosition(PullerPosition.UP)
                    wait(.5)

                    // get second stone
                    forward(5850)
                    turnRight(1600)
                    backward(850)
                    setRightPullerPosition(PullerPosition.DOWN)
                    wait(.5)
                    // move stone
                    forward(1650)
                    turnLeft(1600)
                    backward(5600, .9)
                    setRightPullerPosition(PullerPosition.UP)
                    forward(1000)
                }

                1 -> {
                    // get first stone
                    backward(2750)
                    setRightPullerPosition(PullerPosition.DOWN)
                    right(250)
                    wait(.3)
                    // move stone
                    forward(1000)
                    turnLeft(1650)
                    backward(3700, .9)
                    setRightPullerPosition(PullerPosition.UP)
                    wait(.2)

                    // get second stone
                    forward(100)
                    turnRight(120, 1.0, 1.0)
                    forward(6200)
                    turnRight(1300)
                    backward(1200)
                    setRightPullerPosition(PullerPosition.DOWN)
                    wait(.5)
                    // move stone
                    forward(1650)
                    turnLeft(1600)
                    backward(6400, .9)
                    setRightPullerPosition(PullerPosition.UP)
                    forward(1200)
                }

                2 -> {
                    // get first stone
                    backward(2550, 0.7)
                    left(850)
                    backward(250)
                    setRightPullerPosition(PullerPosition.DOWN)
                    wait(.3)
                    // move stone
                    forward(1000)
                    turnLeft(1650)
                    backward(4500, 1.0)
                    setRightPullerPosition(PullerPosition.UP)
                    wait(.2)

                    // get second stone
                    forward(100)
                    turnRight(160, 1.0, 1.5)
                    forward(6000, 1.0)
                    turnRight(1500)
                    left(800, timeoutS = 3.0)
                    backward(800, 0.7)
                    setRightPullerPosition(PullerPosition.DOWN)
                    wait(.5)
                    // move stone
                    forward(1650)
                    right(900)
                    turnLeft(1850)
                    backward(6800, 1.0)
                    setRightPullerPosition(PullerPosition.UP)
                    forward(1200)
                }
            }
        }
    }
}
