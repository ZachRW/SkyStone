package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple

@TeleOp
class Drive : OpMode() {
    private val hardware = Hardware(hardwareMap, telemetry)

    override fun init() {
        hardware.setLinearSlidePower(1.0)
        telemetry.addLine("Initialization Finished")
        telemetry.update()
    }

    override fun loop() {
        hardware.setMecanumPower((-gamepad1.left_stick_y).toDouble(),
                gamepad1.left_stick_x.toDouble(), gamepad1.right_stick_x.toDouble(), .5)

        if (gamepad2.dpad_up) {
            hardware.setLinearSlidePower(1.0)
        } else if (gamepad2.dpad_down) {
            hardware.setLinearSlidePower(-1.0)
        }

        hardware.setSuckPower(gamepad2.left_trigger.toDouble(), gamepad2.right_trigger.toDouble())
    }
}
