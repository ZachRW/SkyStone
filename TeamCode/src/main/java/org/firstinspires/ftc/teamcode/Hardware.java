package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

public class Hardware {
	private DcMotor frontLeft, frontRight,
			backLeft, backRight;
	private DcMotor[] wheels;
	private String[] wheelLabels;

	private DcMotor leftSlide, rightSlide;

	private CRServo leftSuck, rightSuck;
	private Servo leftPuller, rightPuller;

	private LinearOpMode opMode;
	private Telemetry telemetry;

	private SkystoneDetector skystoneDetector;
	private ElapsedTime timer;

	Hardware(OpMode opMode) {
		this.telemetry = opMode.telemetry;

		skystoneDetector = new SkystoneDetector(telemetry);
		timer            = new ElapsedTime();

		HardwareMap hardwareMap = opMode.hardwareMap;
		frontLeft   = hardwareMap.dcMotor.get("fl");
		frontRight  = hardwareMap.dcMotor.get("fr");
		backLeft    = hardwareMap.dcMotor.get("bl");
		backRight   = hardwareMap.dcMotor.get("br");
		leftSlide   = hardwareMap.dcMotor.get("l slide");
		rightSlide  = hardwareMap.dcMotor.get("r slide");
		leftSuck    = hardwareMap.crservo.get("ls");
		rightSuck   = hardwareMap.crservo.get("rs");
		leftPuller  = hardwareMap.servo.get("l pull");
		rightPuller = hardwareMap.servo.get("r pull");

		wheels      = new DcMotor[]{
				frontLeft, frontRight,
				backLeft, backRight
		};
		wheelLabels = new String[]{
				"FL", "FR", "BL", "BR"
		};

		frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
		backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
		rightSuck.setDirection(DcMotorSimple.Direction.REVERSE);
		leftSlide.setDirection(DcMotorSimple.Direction.REVERSE);

		setWheelZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		setWheelMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setWheelMode(DcMotor.RunMode.RUN_USING_ENCODER);

		telemetry.addLine("Hardware Initialized");
		telemetry.update();
	}

	void slideTele() {
		telemetry.addData("Left", "Pos: %d, Tar: %d",
				leftSlide.getCurrentPosition(), leftSlide.getTargetPosition());
		telemetry.addData("Right", "Pos: %d, Tar: %d",
				rightSlide.getCurrentPosition(), rightSlide.getTargetPosition());
		telemetry.update();
	}

	void wheelTele() {
		telemetry.addData("FL", frontLeft.getCurrentPosition());
		telemetry.addData("FR", frontRight.getCurrentPosition());
		telemetry.addData("BL", backLeft.getCurrentPosition());
		telemetry.addData("BR", backRight.getCurrentPosition());
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

	void initSkystoneDetector() {
		int cameraViewId = opMode.hardwareMap.appContext.getResources().getIdentifier(
				"cameraMonitorViewId", "id",
				opMode.hardwareMap.appContext.getPackageName());
		OpenCvCamera phoneCam =
				new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraViewId);
		phoneCam.openCameraDevice();
		phoneCam.setPipeline(skystoneDetector);
		phoneCam.startStreaming(960, 720, OpenCvCameraRotation.UPRIGHT);
	}

	int getSkystonePosition() {
		return skystoneDetector.skystonePosition();
	}

	void setPullerPositions(double left, double right) {
		leftPuller.setPosition(left);
		rightPuller.setPosition(right);
	}

	void forward(int ticks, double speed, double timeoutS) {
		move(ticks, ticks, ticks, ticks, speed, timeoutS, "Forward");
	}

	void backward(int ticks, double speed, double timeoutS) {
		move(-ticks, -ticks, -ticks, -ticks, speed, timeoutS, "Backward");
	}

	void right(int ticks, double speed, double timeoutS) {
		move(-ticks, ticks, ticks, -ticks, speed, timeoutS, "Right");
	}

	void left(int ticks, double speed, double timeoutS) {
		move(ticks, -ticks, -ticks, ticks, speed, timeoutS, "Left");
	}

	void turnRight(int ticks, double speed, double timeoutS) {
		move(ticks, -ticks, ticks, -ticks, speed, timeoutS, "Right Turn");
	}

	void turnLeft(int ticks, double speed, double timeoutS) {
		move(-ticks, ticks, -ticks, ticks, speed, timeoutS, "Left Turn");
	}

	void wait(double seconds) {
		timer.reset();
		while (timer.seconds() < seconds && opMode.opModeIsActive()) {
			telemetry.addData("Waiting", "%4.2ds");
			telemetry.update();
		}
	}

	private void move(int flTicks, int frTicks, int blTicks, int brTicks,
					  double speed, double timeoutS, String action) {
		move(flTicks, frTicks, blTicks, brTicks, speed, speed, speed, speed, timeoutS, action);
	}

	void move(int flTicks, int frTicks, int blTicks, int brTicks,
					  double flSpeed, double frSpeed, double blSpeed, double brSpeed,
					  double timeoutS, String action) {
		setWheelMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

		frontLeft.setTargetPosition(flTicks);
		frontRight.setTargetPosition(frTicks);
		backLeft.setTargetPosition(blTicks);
		backRight.setTargetPosition(brTicks);

		setWheelMode(DcMotor.RunMode.RUN_TO_POSITION);

		frontLeft.setPower(flSpeed);
		frontRight.setPower(frSpeed);
		backLeft.setPower(blSpeed);
		backRight.setPower(brSpeed);

		timer.reset();
		while (wheelsBusy() && timer.seconds() < timeoutS && opMode.opModeIsActive()) {
			telemetry.addLine(action + "\n");
			telemetry.addData("Motor", "Position |  Target  | Distance");
			for (int i = 0; i < wheels.length; i++) {
				telemetry.addData(
						wheelLabels[i],
						"%8d | %8d | %8d",
						wheels[i].getCurrentPosition(),
						wheels[i].getTargetPosition(),
						wheels[i].getTargetPosition() - wheels[i].getCurrentPosition()
				);
			}
			telemetry.update();
		}
	}

	private boolean wheelsBusy() {
		for (DcMotor wheel : wheels) {
			if (Math.abs(wheel.getTargetPosition() - wheel.getCurrentPosition()) > 50) {
				return true;
			}
		}
		return false;
	}
}
