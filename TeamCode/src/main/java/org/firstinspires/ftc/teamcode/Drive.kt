package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple

@TeleOp
class Drive : OpMode() {
    private val hardware = Hardware(hardwareMap, telemetry)

    override fun init() {
        telemetry.addLine("Initialization Finished")
        telemetry.update()
        hardware.setPullerPositions(1.0 , 0.0)
    }

    override fun loop() {
        with(hardware) {
            with(gamepad1) {
                setMecanumPower(-left_stick_y.toDouble(),
                        left_stick_x.toDouble(), right_stick_x.toDouble(), 0.5)

                when {
                    x -> {
                        setPullerPositions(1.0, 0.0)
                    }
                    y -> {
                        setPullerPositions(0.5, 0.2)
                    }
                }
            }

            with(gamepad2) {
                when {
                    dpad_up -> {
                        setLinearSlidePower(-0.5)
                    }
                    dpad_down -> {
                        setLinearSlidePower(0.5)
                    }
                    else -> {
                        setLinearSlidePower(0.0)
                    }
                }

                setSuckPower(left_trigger.toDouble(), right_trigger.toDouble())
            }
        }
    }
}
