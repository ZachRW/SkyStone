package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
public class Auto extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		Hardware hardware = new Hardware(this);
		telemetry.addLine("Initialization Finished");
		telemetry.update();

		waitForStart();

		hardware.wait(.2);
		telemetry.addData("Skystones", hardware.getSkystonePositions());
		telemetry.update();

		hardware.backward(1900, .5, 5);
		hardware.turnRight(1400, .5, 5);
		hardware.right(400, .5, 5);

		hardware.setPullerPosition(.7);
		hardware.wait(1.0);
		hardware.left(1000, .5, 5);
		hardware.forward(4000, .5, 5);
		hardware.setPullerPosition(0);
		hardware.backward(2000, .5, 5);
		hardware.turnLeft(300, .5, 5);
		hardware.backward(4300, .5, 5);
		hardware.right(1250, .5, 5);
		hardware.setPullerPosition(.7);
		hardware.wait(1.0);
		hardware.left(1500, .5, 5);
		hardware.forward(4500, .5, 5);
		hardware.turnLeft(1800, .5, 5);
		hardware.setPullerPosition(0);
	}
}
