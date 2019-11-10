package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

public class Hardware {
	private DcMotor frontLeft, frontRight,
			backLeft, backRight;
	private DcMotor[] wheels;

	private DcMotor leftSlide, rightSlide;

	private CRServo leftSuck, rightSuck;
	private CRServo grabber;
	private CRServo pusher;

	private LinearOpMode opMode;
	private Telemetry telemetry;

	private SkystoneDetector skystoneDetector;

	Hardware(OpMode opMode) {
		this.telemetry = opMode.telemetry;

		skystoneDetector = new SkystoneDetector(telemetry);

		HardwareMap hardwareMap = opMode.hardwareMap;
		frontLeft  = hardwareMap.dcMotor.get("fl");
		frontRight = hardwareMap.dcMotor.get("fr");
		backLeft   = hardwareMap.dcMotor.get("bl");
		backRight  = hardwareMap.dcMotor.get("br");
		leftSlide  = hardwareMap.dcMotor.get("l slide");
		rightSlide = hardwareMap.dcMotor.get("r slide");
		leftSuck   = hardwareMap.crservo.get("ls");
		rightSuck  = hardwareMap.crservo.get("rs");
		grabber    = hardwareMap.crservo.get("grab");
		pusher     = hardwareMap.crservo.get("push");

		wheels = new DcMotor[]{
				frontLeft, frontRight,
				backLeft, backRight
		};

		frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
		backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
		rightSuck.setDirection(DcMotorSimple.Direction.REVERSE);
		grabber.setDirection(DcMotorSimple.Direction.REVERSE);

		setWheelZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

//		setWheelMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//		setWheelMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
		leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		leftSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		rightSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		telemetry.addLine("Hardware Initialized");
		telemetry.update();
	}

	Hardware(LinearOpMode linearOpMode) {
		this((OpMode) linearOpMode);
		opMode = linearOpMode;
	}

	void setMecanumPower(double forwards, double strafe, double turn, double speed) {
		frontLeft.setPower((forwards - strafe + turn) * speed);
		frontRight.setPower((forwards + strafe - turn) * speed);
		backLeft.setPower((forwards + strafe + turn) * speed);
		backRight.setPower((forwards - strafe - turn) * speed);
	}

	void setLinearSlidePower(double power) {
		leftSlide.setPower(power);
		rightSlide.setPower(power);
	}

	void setSuckPower(double left, double right) {
		leftSuck.setPower(left);
		rightSuck.setPower(right);
	}

	void setPusherPower(double power) {
		pusher.setPower(power);
	}

	void setGrabberPower(double power) {
		grabber.setPower(power);
	}

	private void setWheelMode(DcMotor.RunMode runMode) {
		for (DcMotor wheel : wheels) {
			wheel.setMode(runMode);
		}
	}

	private void setWheelZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
		for (DcMotor wheel : wheels) {
			wheel.setZeroPowerBehavior(behavior);
		}
	}

	// Autonomous

	List<Integer> getSkystonePositions() {
		return skystoneDetector.skystonePositions();
	}

	private void move(int flTicks, int frTicks, int blTicks, int brTicks,
					  double speed, double timeout) {
		setWheelMode(DcMotor.RunMode.RUN_TO_POSITION);

		frontLeft.setTargetPosition(flTicks);
		frontRight.setTargetPosition(frTicks);
		backLeft.setTargetPosition(blTicks);
		backRight.setTargetPosition(brTicks);

		for (DcMotor wheel : wheels) {
			wheel.setPower(speed);
		}

		while (wheelsBusy() && opMode.opModeIsActive()) {
		}
	}

	private boolean wheelsBusy() {
		for (DcMotor wheel : wheels) {
			if (wheel.isBusy()) {
				return true;
			}
		}
		return false;
	}
}
