package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class Drive : OpMode() {
    private lateinit var hardware: Hardware

    private var speed = 1.0
    private var reverse = false
    private var prevX1 = false
    private var leftPullerDown = false
    private var prevLB1 = false
    private var rightPullerDown = false
    private var prevRB1 = false

    private var clawClosed = false
    private var prevX2 = false
    private var flickerIn = false
    private var prevA2 = false

    override fun init() {
        hardware = Hardware(hardwareMap, telemetry)
        telemetry.addLine("Initialization Finished")
        telemetry.update()
    }

    override fun loop() {
        with(hardware) {
            with(gamepad1) {
                setMecanumPower(
                    -left_stick_y.toDouble(),
                    left_stick_x.toDouble(),
                    right_stick_x.toDouble(),
                    speed,
                    reverse
                )

                when {
                    dpad_up ->
                        speed = 1.0

                    dpad_left || dpad_right ->
                        speed = 0.5

                    dpad_down ->
                        speed = 0.2
                }

                if (x && !prevX1) {
                    reverse = !reverse
                }
                if (left_bumper && !prevLB1) {
                    leftPullerDown = !leftPullerDown
                }
                if (right_bumper && !prevRB1) {
                    rightPullerDown = !rightPullerDown
                }

                setLeftPullerPosition(if (leftPullerDown) 0.3 else 1.0)
                setRightPullerPosition(if (rightPullerDown) 0.67 else 0.0)

                prevX1 = x
                prevLB1 = left_bumper
                prevRB1 = right_bumper
            }

            with(gamepad2) {
                setLinearSlidePower(left_stick_y.toDouble())

                if (x && !prevX2) {
                    clawClosed = !clawClosed
                }
                if (a && !prevA2) {
                    flickerIn = !flickerIn
                }

                setClawPosition(if (clawClosed) 0.5 else 0.3)
                setFlickerPosition(if (flickerIn) 1.0 else 0.0)

                setClawSlidePower(
                    when {
                        dpad_up -> 1.0
                        dpad_down -> -1.0
                        else -> 0.0
                    }
                )

                setSuckPower(left_trigger.toDouble(), right_trigger.toDouble())
                if (left_bumper) {
                    setSuckPower(-1.0, -1.0)
                }

                prevX2 = x
                prevA2 = a
            }
        }
    }
}
