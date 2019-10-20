package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Hardware {
	private DcMotor frontLeft, frontRight,
			backLeft, backRight;
	private DcMotor[] wheels;

	private DcMotor slideLeft, slideRight;

	private Telemetry telemetry;

	Hardware(HardwareMap hardwareMap, Telemetry telemetry) {
		this.telemetry = telemetry;

		frontLeft  = hardwareMap.dcMotor.get("fl");
		frontRight = hardwareMap.dcMotor.get("fr");
		backLeft   = hardwareMap.dcMotor.get("bl");
		backRight  = hardwareMap.dcMotor.get("br");
		slideLeft  = hardwareMap.dcMotor.get("sl");
		slideRight = hardwareMap.dcMotor.get("sr");

		wheels = new DcMotor[]{
				frontLeft, frontRight,
				backLeft, backRight
		};

		frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
		backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

		setWheelZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		slideLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		slideRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		setWheelMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setWheelMode(DcMotor.RunMode.RUN_USING_ENCODER);

		slideLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		slideLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		slideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		slideRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		telemetry.addLine("Hardware Initialized");
	}

	void setMecanumPower(double forwards, double strafe, double turn) {
		setMecanumPower(forwards, strafe, turn, 1);
	}

	void setMecanumPower(double forwards, double strafe, double turn, double speed) {
		frontLeft.setPower((forwards - strafe + turn) * speed);
		frontRight.setPower((forwards + strafe - turn) * speed);
		backLeft.setPower((forwards + strafe + turn) * speed);
		backRight.setPower((forwards - strafe - turn) * speed);
	}

	void setWheelMode(DcMotor.RunMode runMode) {
		for (DcMotor wheel : wheels) {
			wheel.setMode(runMode);
		}
	}

	void setLinearSlidePower(double power) {

	}

	private void setWheelZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
		for (DcMotor wheel : wheels) {
			wheel.setZeroPowerBehavior(behavior);
		}
	}
}
