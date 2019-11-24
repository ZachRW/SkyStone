package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.ElapsedTime

class AutoHardware internal constructor(private val linearOpMode: LinearOpMode)
    : Hardware(linearOpMode.hardwareMap, linearOpMode.telemetry) {
    private val timer = ElapsedTime()
    private val skystoneDetector = SkystoneDetector(telemetry)


    internal val skystonePositions: List<Int>?
        get() = skystoneDetector.skystonePositions()

    internal fun forward(ticks: Int, speed: Double, timeoutS: Double) {
        move(ticks, ticks, ticks, ticks, speed, timeoutS, "Forward")
    }

    internal fun backward(ticks: Int, speed: Double, timeoutS: Double) {
        move(-ticks, -ticks, -ticks, -ticks, speed, timeoutS, "Backward")
    }

    internal fun right(ticks: Int, speed: Double, timeoutS: Double) {
        move(-ticks, ticks, ticks, -ticks, speed, timeoutS, "Right")
    }

    internal fun left(ticks: Int, speed: Double, timeoutS: Double) {
        move(ticks, -ticks, -ticks, ticks, speed, timeoutS, "Left")
    }

    internal fun turnRight(ticks: Int, speed: Double, timeoutS: Double) {
        move(ticks, -ticks, ticks, -ticks, speed, timeoutS, "Right Turn")
    }

    internal fun turnLeft(ticks: Int, speed: Double, timeoutS: Double) {
        move(-ticks, ticks, -ticks, ticks, speed, timeoutS, "Left Turn")
    }

    internal fun wait(seconds: Double) {
        timer.reset()
        while (timer.seconds() < seconds && linearOpMode.opModeIsActive()) {
            telemetry.addData("Waiting", "%4.2ds")
            telemetry.update()
        }
    }

    private fun move(flTicks: Int, frTicks: Int, blTicks: Int, brTicks: Int,
                     speed: Double, timeoutS: Double, action: String) {
        setWheelMode(DcMotor.RunMode.RUN_TO_POSITION)

        frontLeft.targetPosition = flTicks
        frontRight.targetPosition = frTicks
        backLeft.targetPosition = blTicks
        backRight.targetPosition = brTicks

        for (wheel in wheels) {
            wheel.power = speed
        }

        timer.reset()
        while (wheelsBusy() && timer.seconds() < timeoutS && linearOpMode.opModeIsActive()) {
            telemetry.addLine(action + "\n")
            telemetry.addData("Motor", "Position |  Target  | Distance")
            for (i in wheels.indices) {
                telemetry.addData(
                        wheelLabels[i],
                        "%8d | %8d | %8d",
                        wheels[i].currentPosition,
                        wheels[i].targetPosition,
                        wheels[i].targetPosition - wheels[i].currentPosition
                )
            }
            telemetry.update()
        }
    }

    private fun wheelsBusy(): Boolean {
        for (wheel in wheels) {
            if (wheel.isBusy) {
                return true
            }
        }
        return false
    }
}