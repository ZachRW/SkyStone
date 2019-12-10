package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class Drive : OpMode() {
    private var hardware: Hardware? = null
    private var clawClosed = false
    private var prevX = false
    private var flickerIn = false
    private var prevA = false
    private var leftX = 0;
    private var leftY = 0;
    private var rightX = 0;
    private var rightY = 0;

    override fun init() {
        hardware = Hardware(hardwareMap, telemetry)
        telemetry.addLine("Initialization Finished")
        telemetry.update()
        with(hardware!!) {
            setLeftPullerPosition(PullerPosition.UP)
            setRightPullerPosition(PullerPosition.UP)
            setClawPosition(0.3)
            setFlickerPosition(0.2)
        }
    }

    override fun loop() {
        with(hardware!!) {
            with(gamepad1) {
                setMecanumPower(
                    -left_stick_y.toDouble(), left_stick_x.toDouble(),
                    right_stick_x.toDouble(), 1.0
                )
            }

            with(gamepad2) {
//                when {
//                    dpad_up -> {
//                        setLinearSlidePower(-0.5)
//                    }
//                    dpad_down -> {
//                        setLinearSlidePower(0.5)
//                    }
//                    else -> {
//                        setLinearSlidePower(0.0)
//                    }
//                }
                setLinearSlidePowerLeft(left_stick_y.toDouble())
//                setLinearSlidePowerRight(right_stick_y.toDouble())

                if (x && !prevX) {
                    clawClosed = !clawClosed
                }
                if (a && !prevA) {
                    flickerIn = !flickerIn
                }

                if (clawClosed) {
                    setClawPosition(0.5)
                } else {
                    setClawPosition(0.3)
                }

                if (flickerIn) {
                    setFlickerPosition(0.8)
                } else {
                    setFlickerPosition(0.2)
                }

                //setClawSlidePower(right_stick_y.toDouble())
                when {
                    dpad_up -> {
                        setClawSlidePower(0.5)
                    }
                    dpad_down -> {
                        setClawSlidePower(-0.5)
                    }
                    else -> {
                        setClawSlidePower(0.0)
                    }
                }

                setSuckPower(left_trigger.toDouble(), right_trigger.toDouble())
                when {
                    left_bumper -> {
                        setSuckPower(-1.0,-1.0)
                    }

                }
                prevX = x
                prevA = a
            }
        }
    }
}
