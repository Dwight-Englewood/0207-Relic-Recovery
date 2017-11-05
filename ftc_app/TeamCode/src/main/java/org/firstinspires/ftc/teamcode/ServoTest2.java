package org.firstinspires.ftc.teamcode;
/**
 * Created by weznon on 11/5/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name = "ServoTest2", group = "Teleop")
public class ServoTest2 extends OpMode {
    Bot robot;
    double merp = .5;
    double merp2 = .5;
    boolean flag1 = false;
    boolean flag2 = false;
    @Override
    public void init() {
        robot.init(hardwareMap, telemetry);
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        robot.armBottomExtendyServo.setPosition(.5);
        robot.armTopExtendyServo.setPosition(.5);
    }

    @Override
    public void loop() {
        if (merp < 0) {
            merp = 0;
            flag1 = true;
        } else if (merp > 1) {
            merp = 1;
            flag1 = false;
        }

        if (merp2 < 0) {
            merp2 = 0;
            flag2 = true;
        } else if (merp2 > 1) {
            merp2 = 1;
            flag2 = false;
        }

        if (flag1) {
            merp = merp + .01;
        } else {
            merp = merp - .01;
        }

        if (flag2) {
            merp2 = merp2 + .01;
        } else {
            merp2 = merp2 - .01;
        }

        robot.armBottomExtendyServo.setPosition(merp);
        robot.armTopExtendyServo.setPosition(merp2);
        telemetry.addData("merp aka bottom", merp);
        telemetry.addData("merp2 aka top", merp2);
    }

    @Override
    public void stop() {

    }
}
