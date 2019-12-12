package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry

open class Hardware(hardwareMap: HardwareMap, protected val telemetry: Telemetry) {
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
    private val clawSlide: CRServo
    private val leftPuller: Servo
    private val rightPuller: Servo
    private val claw: Servo
    private val flicker: Servo


    init {
        with(hardwareMap) {
            frontLeft = dcMotor["fl"]
            frontRight = dcMotor["fr"]
            backLeft = dcMotor["bl"]
            backRight = dcMotor["br"]
            leftSlide = dcMotor["l slide"]
            rightSlide = dcMotor["r slide"]
            leftSuck = crservo["l suck"]
            rightSuck = crservo["r suck"]
            clawSlide = crservo["c slide"]
            leftPuller = servo["l pull"]
            rightPuller = servo["r pull"]
            claw = servo["claw"]
            flicker = servo["flick"]
        }

        wheels = arrayOf(frontLeft, frontRight, backLeft, backRight)
        wheelLabels = arrayOf("FL", "FR", "BL", "BR")

        frontLeft.direction = Direction.REVERSE
        backLeft.direction = Direction.REVERSE
        leftSlide.direction = Direction.REVERSE
        rightSuck.direction = Direction.REVERSE

        wheels.forEach {
            it.zeroPowerBehavior = ZeroPowerBehavior.BRAKE

            it.mode = RunMode.STOP_AND_RESET_ENCODER
            it.mode = RunMode.RUN_USING_ENCODER
        }

        leftSlide.zeroPowerBehavior = ZeroPowerBehavior.BRAKE
        rightSlide.zeroPowerBehavior = ZeroPowerBehavior.BRAKE

        telemetry.addLine("Hardware Initialized")
        telemetry.update()
    }

    internal fun setLeftPullerPosition(position: PullerPosition) {
        when (position) {
            PullerPosition.UP -> {
                leftPuller.position = 1.0
            }

            PullerPosition.DOWN -> {
                leftPuller.position = 0.42
            }
        }
    }

    internal fun setRightPullerPosition(position: PullerPosition) {
        when (position) {
            PullerPosition.UP -> {
                rightPuller.position = 0.0
            }

            PullerPosition.DOWN -> {
                rightPuller.position = 0.55
            }
        }
    }

    internal fun setFlickerPosition(position: Double) {
        flicker.position = position
    }

    internal fun setClawPosition(position: Double) {
        claw.position = position
    }

    internal fun setClawSlidePower(power: Double) {
        clawSlide.power = power
    }

    internal fun setMecanumPower(forwards: Double, strafe: Double, turn: Double, speed: Double) {
        frontLeft.power = (forwards - strafe + turn) * speed
        frontRight.power = (forwards + strafe - turn) * speed
        backLeft.power = (forwards + strafe + turn) * speed
        backRight.power = (forwards - strafe - turn) * speed
    }

    internal fun setLinearSlidePowerRight(power: Double) {
        rightSlide.power = power

    }

    internal fun setLinearSlidePowerLeft(power: Double) {
        leftSlide.power = power
    }

    internal fun setSuckPower(left: Double, right: Double) {
        leftSuck.power = left
        rightSuck.power = right
    }
}

enum class PullerPosition {
    UP, DOWN
}
