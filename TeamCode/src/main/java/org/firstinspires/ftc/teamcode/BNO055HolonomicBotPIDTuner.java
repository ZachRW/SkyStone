package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import edu.spa.ftclib.internal.controller.PIDController;
import edu.spa.ftclib.util.PIDTuner;

/**
 * Created by Gabriel on 2018-01-07.
 */

//@Disabled
@TeleOp(name = "BNO055 Holonomic Bot PID Tuner", group = "sample")

public class BNO055HolonomicBotPIDTuner extends OpMode {
	private PIDTuner tuner;
	private Hardware robot;

	/**
	 * User defined init method
	 * <p>
	 * This method will be called once when the INIT button is pressed.
	 */
	@Override
	public void init() {
		robot = new Hardware(hardwareMap, telemetry);
		tuner = new PIDTuner(robot.getDrivetrain(), (PIDController) robot.getController().algorithm, gamepad1, telemetry);
	}

	/**
	 * User defined loop method
	 * <p>
	 * This method will be called repeatedly in a loop while this op mode is running
	 */
	@Override
	public void loop() {
		robot.setMecanumPowerGyro(
				-gamepad1.left_stick_y,
				gamepad1.left_stick_x,
				gamepad1.right_stick_x,
				0.5,
				false
		);
		tuner.update();
	}
}
