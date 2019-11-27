package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
public class Auto extends LinearOpMode {
	@Override
	public void runOpMode() {
		Hardware hardware = new Hardware(this);
		telemetry.addLine("Initialization Finished");
		telemetry.update();

		waitForStart();

		hardware.setPullerPosition(0);
		telemetry.addData("Skystones", hardware.getSkystonePositions());
		telemetry.update();

		hardware.backward(2600, 1, 5);
		hardware.right(1000, 1, 5);
		hardware.setPullerPosition(.1);
		hardware.wait(.5);
		hardware.forward(1700, 1, 5);
		hardware.turnLeft(1800, 1, 5);
		hardware.backward(3500, 1, 5);
		hardware.setPullerPosition(0);
		hardware.wait(.5);
		hardware.forward(5300, 1, 5);
		hardware.turnRight(1600, 1, 5);
		hardware.backward(1500, 1, 5);
		hardware.setPullerPosition(.1);
		hardware.wait(.5);
		hardware.forward(1700, 1, 5);
		hardware.turnLeft(1800, 1, 5);
		hardware.backward(5300, 1, 5);
		hardware.setPullerPosition(0);
		hardware.forward(1000, 1, 5);

	}
}
