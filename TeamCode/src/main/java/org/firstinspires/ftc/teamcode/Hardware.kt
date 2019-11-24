package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

import org.firstinspires.ftc.robotcore.external.Telemetry

open class Hardware internal constructor(hardwareMap: HardwareMap, val telemetry: Telemetry) {
    val frontLeft: DcMotor
    val frontRight: DcMotor
    val backLeft: DcMotor
    val backRight: DcMotor
    val wheels: Array<DcMotor>
    val wheelLabels: Array<String>

    private val leftSlide: DcMotor
    private val rightSlide: DcMotor

    private val leftSuck: CRServo
    private val rightSuck: CRServo
    private val puller: Servo


    init {
        hardwareMap.apply {
            frontLeft = dcMotor["fl"]
            frontRight = dcMotor["fr"]
            backLeft = dcMotor["bl"]
            backRight = dcMotor["br"]
            leftSlide = dcMotor["l slide"]
            rightSlide = dcMotor["r slide"]
            leftSuck = crservo["ls"]
            rightSuck = crservo["rs"]
            puller = servo["pull"]
        }

        wheels = arrayOf(frontLeft, frontRight, backLeft, backRight)
        wheelLabels = arrayOf("FL", "FR", "BL", "BR")

        puller.position = 0.0

        frontLeft.direction = DcMotorSimple.Direction.REVERSE
        backLeft.direction = DcMotorSimple.Direction.REVERSE
        rightSuck.direction = DcMotorSimple.Direction.REVERSE
        leftSlide.direction = DcMotorSimple.Direction.REVERSE

        setWheelZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE)
        leftSlide.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rightSlide.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        setWheelMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)
        setWheelMode(DcMotor.RunMode.RUN_USING_ENCODER)

        leftSlide.targetPosition = 0
        rightSlide.targetPosition = 0

        leftSlide.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        leftSlide.mode = DcMotor.RunMode.RUN_TO_POSITION
        rightSlide.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        rightSlide.mode = DcMotor.RunMode.RUN_TO_POSITION

        telemetry.addLine("Hardware Initialized")
        telemetry.update()
    }

    internal fun slideTele() {
        telemetry.addData("Left", "Pos: %d, Tar: %d",
                leftSlide.currentPosition, leftSlide.targetPosition)
        telemetry.addData("Right", "Pos: %d, Tar: %d",
                rightSlide.currentPosition, rightSlide.targetPosition)
        telemetry.update()
    }

    internal fun wheelTele() {
        telemetry.addData("FL", frontLeft.currentPosition)
        telemetry.addData("FR", frontRight.currentPosition)
        telemetry.addData("BL", backLeft.currentPosition)
        telemetry.addData("BR", backRight.currentPosition)
        telemetry.update()
    }

    internal fun setMecanumPower(forwards: Double, strafe: Double, turn: Double, speed: Double) {
        frontLeft.power = (forwards - strafe + turn) * speed
        frontRight.power = (forwards + strafe - turn) * speed
        backLeft.power = (forwards + strafe + turn) * speed
        backRight.power = (forwards - strafe - turn) * speed
    }

    internal fun setLinearSlidePower(power: Double) {
        leftSlide.power = power
        rightSlide.power = power
    }

    internal fun incrementLinearSlideTarget(ticks: Int) {
        var target = leftSlide.targetPosition + ticks
        if (target < -100) {
            target = -100
        }

        leftSlide.targetPosition = target
        rightSlide.targetPosition = target
    }

    internal fun setSuckPower(left: Double, right: Double) {
        leftSuck.power = left
        rightSuck.power = right
    }

    fun setWheelMode(runMode: DcMotor.RunMode) {
        for (wheel in wheels) {
            wheel.mode = runMode
        }
    }

    private fun setWheelZeroPowerBehavior(behavior: DcMotor.ZeroPowerBehavior) {
        for (wheel in wheels) {
            wheel.zeroPowerBehavior = behavior
        }
    }
}
