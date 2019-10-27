package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import java.util.List;

@TeleOp
public class DogeCVTest extends OpMode {
	private OpenCvCamera phoneCam;
	private SkystoneDetector skystoneDetector = new SkystoneDetector(telemetry);

	@Override
	public void init() {
		int cameraViewId = hardwareMap.appContext.getResources().getIdentifier(
				"cameraMonitorViewId", "id",
				hardwareMap.appContext.getPackageName());
		phoneCam = new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraViewId);
		phoneCam.openCameraDevice();
		phoneCam.setPipeline(skystoneDetector);
		phoneCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
	}

	@Override
	public void loop() {
		List<Integer> positions = skystoneDetector.skystonePositions();
		telemetry.addData("Skystone positions", positions);
		telemetry.update();
	}

	@Override
	public void stop() {
		phoneCam.stopStreaming();
	}
}
