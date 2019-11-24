package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvInternalCamera

@TeleOp
class DogeCVTest : OpMode() {
    private var phoneCam: OpenCvCamera? = null
    private val skystoneDetector = SkystoneDetector(telemetry)

    override fun init() {
        val cameraViewId = hardwareMap.appContext.resources.getIdentifier(
                "cameraMonitorViewId", "id",
                hardwareMap.appContext.packageName)
        phoneCam = OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraViewId)
        phoneCam!!.openCameraDevice()
        phoneCam!!.setPipeline(skystoneDetector)
        phoneCam!!.startStreaming(960, 720, OpenCvCameraRotation.UPRIGHT)
    }

    override fun loop() {
        val positions = skystoneDetector.skystonePositions()
        telemetry.addData("Skystone positions", positions)
        telemetry.update()
    }

    override fun stop() {
        phoneCam!!.stopStreaming()
    }
}
