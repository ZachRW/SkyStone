package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class DogeCVTest extends OpMode {
	private SkystoneDetector skystoneDetector = new SkystoneDetector(telemetry);

	@Override
	public void init() {
		skystoneDetector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
		skystoneDetector.enable();
	}

	@Override
	public void loop() {
	}

	@Override
	public void stop() {
		skystoneDetector.disable();
	}
}
