package org.firstinspires.ftc.teamcode.Telebop;
/**
 * Created by weznon on 2/10/18.
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name = "TelebopRelic", group = "Teleop")
public class TelebopRelic extends OpMode {

    CRServo vexMotor1;
    CRServo vexMotor2;

    Servo servo1;
    Servo servo2;

    double val1 = .5;
    double val2 = .5;

    int cooldown = 0;

    final int cooldownTime = 10;


    @Override
    public void init() {
        vexMotor1 = hardwareMap.get(CRServo.class, "rav1");
        vexMotor2 = hardwareMap.get(CRServo.class, "rav2");

        servo1 = hardwareMap.get(Servo.class, "ras1");
        servo1.scaleRange(.2, .8);
        servo2 = hardwareMap.get(Servo.class, "ras2");
        servo2.scaleRange(.2, .8);
        vexMotor2.setDirection(CRServo.Direction.FORWARD);
        vexMotor1.setDirection(CRServo.Direction.FORWARD);
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {

        if (gamepad2.left_trigger > .15 && cooldown <= 0) {
            cooldown = cooldownTime;
            val1 += .01;
        } else if (gamepad2.left_bumper && cooldown <= 0) {
            cooldown = cooldownTime;
            val1 -= .01;
        }

        if (gamepad2.right_trigger > .15 && cooldown <= 0) {
            cooldown = cooldownTime;
            val2 += .03;
        } else if (gamepad2.right_bumper && cooldown <= 0) {
            cooldown = cooldownTime;
            val2 -= .03;
        }

        if (gamepad2.a) {
            vexMotor2.setDirection(CRServo.Direction.FORWARD);
            vexMotor1.setDirection(CRServo.Direction.FORWARD);
            vexMotor1.setPower(.5);
            vexMotor2.setPower(.5);
            telemetry.addData("power", 1);
        } else if (gamepad2.y) {
            vexMotor1.setDirection(CRServo.Direction.REVERSE);
            vexMotor1.setDirection(CRServo.Direction.REVERSE);
            vexMotor1.setPower(.5);
            vexMotor2.setPower(.5);
            telemetry.addData("power", -1);
        } else {
            vexMotor1.setPower(0);
            vexMotor2.setPower(0);
            telemetry.addData("power", 0);
        }

        val1 = Range.clip(val1, 0, 1);
        val2 = Range.clip(val2, 0, 1);

        servo1.setPosition(val1);
        servo2.setPosition(val2);

        cooldown--;

        telemetry.addData("servo 1", val1);
        telemetry.addData("servo 2", val2);
        telemetry.addData("cooldown", cooldown);
        telemetry.update();
    }

    @Override
    public void stop() {

    }
}
