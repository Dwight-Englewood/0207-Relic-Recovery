package org.firstinspires.ftc.teamcode.Testing;
/**
 * Created by weznon on 2/2/18.
 */

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;


@TeleOp(name = "RelicArmTest", group = "Teleop")
@Disabled
public class RelicArmTest extends OpMode {
    private CRServo motor1, motor2;
    @Override
    public void init() {
        hardwareMap.get(CRServo.class, "motor1");
        hardwareMap.get(CRServo.class, "motor2");

    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        if (gamepad1.b) {
            motor1.setPower(.5);
            motor2.setPower(.5);
        } else if (gamepad1.a) {
            motor1.setPower(-.5);
            motor2.setPower(-.5);
        } else {
            motor1.setPower(0);
            motor2.setPower(0);
        }


    }

    @Override
    public void stop() {

    }
}
