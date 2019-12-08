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
                    // get block
                    backward(2750)
                    left(920)
                    setLeftPullerPosition(PullerPosition.DOWN)
                    wait(.25)
                    // move block
                    forward(1700)
                    turnRight(1650)
                    backward(3500, .9)
                    setLeftPullerPosition(PullerPosition.UP)
                    wait(.5)
                    forward(5850)
                    turnLeft(1600)
                    backward(850)
                    setLeftPullerPosition(PullerPosition.DOWN)
                    wait(.5)
                    forward(1650)
                    turnRight(1600)
                    backward(5600, .9)
                    setLeftPullerPosition(PullerPosition.UP)
                    forward(1000)
                }

                1 -> {
                    // get first stone
                    backward(2750)
                    setLeftPullerPosition(PullerPosition.DOWN)
                    left(250)
                    wait(.3)
                    // move stone
                    forward(1000)
                    turnRight(1650)
                    backward(3700, .9)
                    setLeftPullerPosition(PullerPosition.UP)
                    wait(.2)
                    // get second stone
                    forward(100)
                    turnLeft(120, 1.0, 1.0)
                    forward(6200)
                    turnLeft(1300)
                    backward(1200)
                    setLeftPullerPosition(PullerPosition.DOWN)
                    wait(.5)
                    // move stone
                    forward(1650)
                    turnRight(1600)
                    backward(6400, .9)
                    setLeftPullerPosition(PullerPosition.UP)
                    forward(1200)
                }

                0 -> {
                    // get first stone
                    backward(2750)
                    right(500)
                    setLeftPullerPosition(PullerPosition.DOWN)
                    wait(.3)
                    // move stone
                    forward(1000)
                    turnRight(1650)
                    backward(3600, .9)
                }
            }
        }
    }
}
