package org.firstinspires.ftc.teamcode.Telebop;
/**
 * Created by weznon on 2/10/18.
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp(name = "TelebopRelic", group = "Teleop")
public class TelebopRelic extends OpMode {

    CRServo vexMotor1;
    CRServo vexMotor2;

    @Override
    public void init() {
        vexMotor1 = hardwareMap.get(CRServo.class, "rav1");
        vexMotor2 = hardwareMap.get(CRServo.class, "rav2");
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

        if (gamepad2.a) {
            vexMotor2.setPower(1);
            vexMotor1.setPower(1);
            telemetry.addData("power", 1);
        } else if (gamepad2.y) {
            vexMotor1.setPower(-1);
            vexMotor2.setPower(-1);
            telemetry.addData("power", -1);
        } else {
            vexMotor1.setPower(0);
            vexMotor2.setPower(0);
            telemetry.addData("power", 0);
        }


    }

    @Override
    public void stop() {

    }
}
