package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@TeleOp
public class DogeCVTest extends OpMode {
    private OpenCvCamera phoneCam;
    private final SkystoneDetector skystoneDetector = new SkystoneDetector(telemetry);

    @Override
    public void init() {
        int cameraViewId = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId", "id",
                hardwareMap.appContext.getPackageName());
        phoneCam = new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraViewId);
        phoneCam.openCameraDevice();
        phoneCam.setPipeline(skystoneDetector);
        phoneCam.startStreaming(960, 720, OpenCvCameraRotation.UPRIGHT);
    }

    @Override
    public void loop() {
        int position = skystoneDetector.skystonePosition();
        telemetry.addData("Skystone positions", position);
        telemetry.update();
    }

    @Override
    public void stop() {
        phoneCam.stopStreaming();
    }
}
