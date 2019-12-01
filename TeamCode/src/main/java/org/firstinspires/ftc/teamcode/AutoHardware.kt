package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvInternalCamera
import org.openftc.easyopencv.OpenCvInternalCamera.CameraDirection
import kotlin.math.abs

class AutoHardware(private val linearOpMode: LinearOpMode)
    : Hardware(linearOpMode.hardwareMap, linearOpMode.telemetry) {
    private val timer = ElapsedTime()
    private val skystoneDetector = SkystoneDetector(telemetry)


    internal val skystonePositions: List<Int>?
        get() = skystoneDetector.skystonePositions()

    internal fun initSkystoneDetector() {
        val cameraViewId = linearOpMode.hardwareMap.appContext.resources.getIdentifier(
                "cameraMonitorViewId", "id",
                linearOpMode.hardwareMap.appContext.packageName
        )

        OpenCvInternalCamera(CameraDirection.BACK, cameraViewId).apply {
            openCameraDevice()
            setPipeline(skystoneDetector)
            startStreaming(960, 720, OpenCvCameraRotation.UPRIGHT)
        }
    }

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
        wheels.forEach { it.mode = RunMode.STOP_AND_RESET_ENCODER }

        frontLeft.targetPosition = flTicks
        frontRight.targetPosition = frTicks
        backLeft.targetPosition = blTicks
        backRight.targetPosition = brTicks

        wheels.forEach {
            it.power = speed
            it.mode = RunMode.RUN_TO_POSITION
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

    private fun wheelsBusy(): Boolean =
            wheels.any { abs(it.targetPosition - it.currentPosition) > 50 }
}