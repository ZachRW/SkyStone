package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.hardware.bosch.BNO055IMUImpl
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator
import com.qualcomm.robotcore.hardware.*
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import edu.spa.ftclib.internal.controller.ErrorTimeThresholdFinishingAlgorithm
import edu.spa.ftclib.internal.controller.FinishableIntegratedController
import edu.spa.ftclib.internal.controller.PIDController
import edu.spa.ftclib.internal.drivetrain.HeadingableMecanumDrivetrain
import edu.spa.ftclib.internal.sensor.IntegratingGyroscopeSensor
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.*
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.hypot

open class Hardware(hardwareMap: HardwareMap, protected val telemetry: Telemetry) {
    val controller: FinishableIntegratedController
    val drivetrain: HeadingableMecanumDrivetrain

    val leftIMU: BNO055IMUImpl
    val rightIMU: BNO055IMUImpl

    val frontLeft: DcMotor
    val frontRight: DcMotor
    val backLeft: DcMotor
    val backRight: DcMotor
    val wheels: Array<DcMotor>
    val wheelLabels: Array<String>

    private val slide: DcMotor

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
            slide = dcMotor["l slide"]
            leftSuck = crservo["l suck"]
            rightSuck = crservo["r suck"]
            clawSlide = crservo["c slide"]
            leftPuller = servo["l pull"]
            rightPuller = servo["r pull"]
            claw = servo["claw"]
            flicker = servo["flick"]
            leftIMU = get(BNO055IMUImpl::class.java, "l imu")
            rightIMU = get(BNO055IMUImpl::class.java, "r imu")
        }

        leftIMU.initialize(BNO055IMU.Parameters().apply {
            angleUnit = BNO055IMU.AngleUnit.RADIANS
            accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC
            loggingEnabled = true
            loggingTag = "Left IMU"
            accelerationIntegrationAlgorithm = JustLoggingAccelerationIntegrator()
        })

        rightIMU.initialize(BNO055IMU.Parameters().apply {
            angleUnit = BNO055IMU.AngleUnit.RADIANS
            accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC
            loggingEnabled = true
            loggingTag = "Right IMU"
            accelerationIntegrationAlgorithm = JustLoggingAccelerationIntegrator()
        })

        while (!leftIMU.isGyroCalibrated || !rightIMU.isGyroCalibrated) {
            telemetry.addLine("Calibrating Gyros")
            telemetry.update()
        }

        val pid = PIDController(0.7, 0.0, 0.0)
            .apply { maxErrorForIntegral = 0.002 }

        val averageGyro = object : IntegratingGyroscope {
            override fun getAngularVelocityAxes(): MutableSet<Axis> =
                leftIMU.angularVelocityAxes

            override fun getAngularVelocity(unit: AngleUnit?): AngularVelocity {
                val left = leftIMU.getAngularVelocity(AngleUnit.RADIANS)
                val right = rightIMU.getAngularVelocity(AngleUnit.RADIANS)

                return AngularVelocity(
                    AngleUnit.RADIANS,
                    (left.xRotationRate + right.xRotationRate) / 2,
                    (left.yRotationRate + right.yRotationRate) / 2,
                    (left.zRotationRate + right.zRotationRate) / 2,
                    (left.acquisitionTime + right.acquisitionTime) / 2
                )
            }

            override fun getAngularOrientation(
                reference: AxesReference?,
                order: AxesOrder?,
                angleUnit: AngleUnit?
            ): Orientation {
                val left = leftIMU.getAngularOrientation(
                    AxesReference.EXTRINSIC,
                    AxesOrder.ZYX,
                    AngleUnit.RADIANS
                )
                val right = rightIMU.getAngularOrientation(
                    AxesReference.EXTRINSIC,
                    AxesOrder.ZYX,
                    AngleUnit.RADIANS
                )

                return Orientation(
                    AxesReference.EXTRINSIC,
                    AxesOrder.ZYX,
                    AngleUnit.RADIANS,
                    (left.firstAngle + right.firstAngle) / 2,
                    (left.secondAngle + right.secondAngle) / 2,
                    (left.thirdAngle + right.thirdAngle) / 2,
                    (left.acquisitionTime + right.acquisitionTime) / 2
                )
            }

            override fun getAngularOrientationAxes(): MutableSet<Axis> =
                leftIMU.angularOrientationAxes
        }

        controller = FinishableIntegratedController(
            IntegratingGyroscopeSensor(averageGyro),
            pid,
            ErrorTimeThresholdFinishingAlgorithm(PI / 50, 1.0)
        )

        wheels = arrayOf(frontLeft, frontRight, backLeft, backRight)
        wheelLabels = arrayOf("FL", "FR", "BL", "BR")

        drivetrain = HeadingableMecanumDrivetrain(wheels, controller)

        slide.direction = Direction.REVERSE
        rightSuck.direction = Direction.REVERSE

        wheels.forEach {
            it.zeroPowerBehavior = ZeroPowerBehavior.BRAKE

            it.mode = RunMode.STOP_AND_RESET_ENCODER
            it.mode = RunMode.RUN_USING_ENCODER
//            it.mode = RunMode.RUN_WITHOUT_ENCODER
        }

        slide.zeroPowerBehavior = ZeroPowerBehavior.BRAKE

        telemetry.addLine("Hardware Initialized")
        telemetry.update()
    }

    internal fun setLeftPullerPosition(position: PullerPosition) {
        setLeftPullerPosition(
            when (position) {
                PullerPosition.UP -> 1.0
                PullerPosition.DOWN -> 0.42
            }
        )
    }

    internal fun setLeftPullerPosition(position: Double) {
        leftPuller.position = position
    }

    internal fun setRightPullerPosition(position: PullerPosition) {
        setRightPullerPosition(
            when (position) {
                PullerPosition.UP -> 0.0
                PullerPosition.DOWN -> 0.55
            }
        )
    }

    internal fun setRightPullerPosition(position: Double) {
        rightPuller.position = position
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

    internal fun setMecanumPower(
        forwards: Double,
        strafe: Double,
        turn: Double,
        speed: Double,
        reverse: Boolean
    ) {
        val forwards0 = if (reverse) -forwards else forwards
        val strafe0 = if (reverse) -strafe else strafe

        frontLeft.power = -(forwards0 - strafe0 + turn) * speed
        frontRight.power = (forwards0 + strafe0 - turn) * speed
        backLeft.power = -(forwards0 + strafe0 + turn) * speed
        backRight.power = (forwards0 - strafe0 - turn) * speed
    }

    fun setMecanumPowerGyro(
        forwards: Double,
        strafe: Double,
        turn: Double,
        speed: Double,
        reverse: Boolean
    ) {
        drivetrain.course = atan2(-forwards, strafe) + (PI / 2)
        if (reverse) {
            drivetrain.course += PI
            drivetrain.course %= 2 * PI
        }

        if (turn != 0.0) {
            drivetrain.rotation = -turn
            drivetrain.targetHeading = drivetrain.currentHeading % (2 * PI)
        } else {
            drivetrain.updateHeading()
        }

        drivetrain.velocity = hypot(strafe, forwards) * speed
    }

    internal fun setLinearSlidePower(power: Double) {
        slide.power = power
    }

    internal fun setSuckPower(left: Double, right: Double) {
        leftSuck.power = left
        rightSuck.power = right
    }
}

enum class PullerPosition {
    UP, DOWN
}
