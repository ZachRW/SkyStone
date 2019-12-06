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
    protected val frontLeft: DcMotor
    protected val frontRight: DcMotor
    protected val backLeft: DcMotor
    protected val backRight: DcMotor
    protected val wheels: Array<DcMotor>
    protected val wheelLabels: Array<String>

    private val leftSlide: DcMotor
    private val rightSlide: DcMotor

    private val leftSuck: CRServo
    private val rightSuck: CRServo
    private val leftPuller: Servo
    private val rightPuller: Servo


    init {
        with(hardwareMap) {
            frontLeft = dcMotor["fl"]
            frontRight = dcMotor["fr"]
            backLeft = dcMotor["bl"]
            backRight = dcMotor["br"]
            leftSlide = dcMotor["l slide"]
            rightSlide = dcMotor["r slide"]
            leftSuck = crservo["ls"]
            rightSuck = crservo["rs"]
            leftPuller = servo["l pull"]
            rightPuller = servo["r pull"]
        }

        wheels = arrayOf(frontLeft, frontRight, backLeft, backRight)
        wheelLabels = arrayOf("FL", "FR", "BL", "BR")

        wheels.forEach {
            it.direction = Direction.REVERSE
            it.zeroPowerBehavior = ZeroPowerBehavior.BRAKE

            it.mode = RunMode.STOP_AND_RESET_ENCODER
            it.mode = RunMode.RUN_USING_ENCODER
        }

        leftSlide.zeroPowerBehavior = ZeroPowerBehavior.BRAKE
        rightSlide.zeroPowerBehavior = ZeroPowerBehavior.BRAKE

        telemetry.addLine("Hardware Initialized")
        telemetry.update()
    }

    internal fun setPullerPositions(left: Double, right: Double) {
        leftPuller.position = left
        rightPuller.position = right
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

    internal fun setSuckPower(left: Double, right: Double) {
        leftSuck.power = left
        rightSuck.power = right
    }
}
