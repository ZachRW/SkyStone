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

		hardware.setLinearSlidePower(1);
	}

	@Override
	public void loop() {
		hardware.setMecanumPower(-gamepad1.left_stick_y,
				gamepad1.left_stick_x, gamepad1.right_stick_x, .5);

		if (gamepad2.dpad_up) {
			hardware.incrementLinearSlideTarget(10);
		} else if (gamepad2.dpad_down) {
			hardware.incrementLinearSlideTarget(-10);
		}

		if (gamepad2.right_bumper) {
			hardware.setGrabberPower(1);
		} else if (gamepad2.left_bumper) {
			hardware.setGrabberPower(-1);
		} else {
			hardware.setGrabberPower(0);
		}

		hardware.setSuckPower(gamepad2.left_trigger, gamepad2.right_trigger);
		hardware.setPusherPower(-gamepad2.left_stick_y);

		hardware.slideTele();
	}
}
