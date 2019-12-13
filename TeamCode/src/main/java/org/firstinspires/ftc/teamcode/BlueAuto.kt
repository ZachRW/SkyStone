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
                    forward(2750)
                    left(800)
                    setRightPullerPosition(PullerPosition.DOWN)
                    wait(.25)
                    // move stone
                    backward(1700)
                    turnLeft(1650)
                    forward(3500, 0.9)
                    setRightPullerPosition(PullerPosition.UP)
                    wait(.5)

                    // get second stone
                    backward(5850)
                    turnRight(1600)
                    forward(850)
                    setRightPullerPosition(PullerPosition.DOWN)
                    wait(.5)
                    // move stone
                    backward(1650)
                    turnLeft(1600)
                    forward(5600, 0.9)
                    setRightPullerPosition(PullerPosition.UP)
                    backward(1000)
                }

                1 -> {
                    // get first stone
                    forward(2750)
                    setRightPullerPosition(PullerPosition.DOWN)
                    wait(.3)
                    // move stone
                    backward(1000)
                    turnLeft(1650)
                    forward(4000, 0.9)
                    setRightPullerPosition(PullerPosition.UP)
                    wait(.2)

                    // get second stone
                    backward(100)
                    turnRight(120, 1.0, 1.0)
                    backward(6200)
                    turnRight(1300)
                    forward(1200)
                    setRightPullerPosition(PullerPosition.DOWN)
                    wait(.5)
                    // move stone
                    backward(1650)
                    turnLeft(1600)
                    forward(6400, 0.9)
                    setRightPullerPosition(PullerPosition.UP)
                    backward(1200)
                }

                2 -> {
                    // get first stone
                    forward(2550, 0.7)
                    right(850)
                    forward(250)
                    setRightPullerPosition(PullerPosition.DOWN)
                    wait(.3)
                    // move stone
                    backward(1000)
                    turnLeft(1650)
                    forward(4800, 0.7)
                    setRightPullerPosition(PullerPosition.UP)
                    wait(.2)

                    // get second stone
                    backward(100)
                    turnRight(160, 1.0, 1.5)
                    backward(6100, 1.0)
                    turnRight(1500)
                    right(750, timeoutS = 3.0)
                    forward(1300, 0.7)
                    setRightPullerPosition(PullerPosition.DOWN)
                    wait(.5)
                    // move stone
                    backward(1650)
                    left(900)
                    turnLeft(1850)
                    forward(6500, 1.0)
                    setRightPullerPosition(PullerPosition.UP)
                    backward(900)
                }
            }
        }
    }
}
