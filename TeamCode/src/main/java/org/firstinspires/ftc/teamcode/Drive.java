package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class Drive extends OpMode {
	private Hardware hardware;

	@Override
	public void init() {
		hardware = new Hardware(this);

		telemetry.addLine("Initialization Finished");
		telemetry.update();
	}

	@Override
	public void loop() {
		hardware.setPullerPosition(0);

		hardware.setMecanumPower(-gamepad1.left_stick_y,
				gamepad1.left_stick_x, gamepad1.right_stick_x, .5);

		if (gamepad2.dpad_up) {
			hardware.setLinearSlidePower(-.5);
		} else if (gamepad2.dpad_down) {
			hardware.setLinearSlidePower(.5);
		}
		else{
			hardware.setLinearSlidePower(0);
		}

		hardware.setSuckPower(gamepad2.left_trigger, gamepad2.right_trigger);

		hardware.wheelTele();
	}
}
