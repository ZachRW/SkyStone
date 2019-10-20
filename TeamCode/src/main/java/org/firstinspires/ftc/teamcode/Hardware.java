package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Hardware {
	private DcMotor frontLeft, frontRight,
			backLeft, backRight;
	private DcMotor[] wheels;

	private DcMotor leftSlide, rightSlide;
	private DcMotor pusher;

	private CRServo leftSuck, rightSuck;
	private CRServo grabber;

	private Telemetry telemetry;

	Hardware(HardwareMap hardwareMap, Telemetry telemetry) {
		this.telemetry = telemetry;

		frontLeft  = hardwareMap.dcMotor.get("fl");
		frontRight = hardwareMap.dcMotor.get("fr");
		backLeft   = hardwareMap.dcMotor.get("bl");
		backRight  = hardwareMap.dcMotor.get("br");
		leftSlide  = hardwareMap.dcMotor.get("lsl");
		rightSlide = hardwareMap.dcMotor.get("rsl");
		pusher     = hardwareMap.dcMotor.get("push");
		leftSuck   = hardwareMap.crservo.get("lsu");
		rightSuck  = hardwareMap.crservo.get("rsu");
		grabber    = hardwareMap.crservo.get("grab");

		wheels = new DcMotor[]{
				frontLeft, frontRight,
				backLeft, backRight
		};

		frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
		backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
		pusher.setDirection(DcMotorSimple.Direction.REVERSE);
		leftSuck.setDirection(DcMotorSimple.Direction.REVERSE);
		grabber.setDirection(DcMotorSimple.Direction.REVERSE);

		setWheelZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		setWheelMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setWheelMode(DcMotor.RunMode.RUN_USING_ENCODER);

		leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		leftSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		rightSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		telemetry.addLine("Hardware Initialized");
	}

	void setMecanumPower(float forwards, float strafe, float turn) {
		setMecanumPower(forwards, strafe, turn, 1);
	}

	void setMecanumPower(float forwards, float strafe, float turn, float speed) {
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

	void setLinearSlidePower(float power) {
		leftSlide.setPower(power);
		rightSlide.setPower(power);
	}

	void setSuckPower(float left, float right) {
		leftSuck.setPower(left);
		rightSuck.setPower(right);
	}

	void setPusherPower(float power) {
		pusher.setPower(power);
	}

	void setGrabberPower(float power) {
		grabber.setPower(power);
	}

	private void setWheelZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
		for (DcMotor wheel : wheels) {
			wheel.setZeroPowerBehavior(behavior);
		}
	}
}
