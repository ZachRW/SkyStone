package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class Drive : OpMode() {
    private var hardware: Hardware? = null

    private var speed = 1.0
    private var reverse = false
    private var prevX1 = false
    private var clawClosed = false
    private var prevX2 = false
    private var flickerIn = false
    private var prevA2 = false

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

                prevX1 = x
            }

            with(gamepad2) {
                setLinearSlidePower(left_stick_y.toDouble())

                if (x && !prevX2) {
                    clawClosed = !clawClosed
                }
                if (a && !prevA2) {
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

                when {
                    dpad_up -> {
                        setClawSlidePower(1.0)
                    }
                    dpad_down -> {
                        setClawSlidePower(-1.0)
                    }
                    else -> {
                        setClawSlidePower(0.0)
                    }
                }

                setSuckPower(left_trigger.toDouble(), right_trigger.toDouble())
                when {
                    left_bumper -> {
                        setSuckPower(-1.0, -1.0)
                    }
                }

                prevX2 = x
                prevA2 = a
            }
        }
    }
}
