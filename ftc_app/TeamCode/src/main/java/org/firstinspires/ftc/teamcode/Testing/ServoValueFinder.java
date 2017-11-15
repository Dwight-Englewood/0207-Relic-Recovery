package org.firstinspires.ftc.teamcode.Testing;
/**
 * Created by weznon on 11/15/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bot;


@TeleOp(name = "ServoValueFinder", group = "Teleop")
public class ServoValueFinder extends OpMode {
    Bot robot = new Bot(hardwareMap, telemetry);

    double flipperVal = .5;
    double juulVal = .5;
    double releaseLeftVal = .5;
    double releaseRightVal = .5;

    int cooldown = 0;
    @Override
    public void init() {
        robot.init();
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        telemetry.clear();

    }

    @Override
    public void loop() {
        if (cooldown == 0) {
            if (gamepad1.dpad_right) {
                flipperVal = flipperVal + .01;
                cooldown = 10000;
            }
            if (gamepad1.dpad_left) {
                flipperVal = flipperVal - .01;
                cooldown = 10000;
            }

            if (gamepad1.dpad_up) {
                juulVal = juulVal + .01;
                cooldown = 10000;
            }
            if (gamepad1.dpad_down) {
                juulVal = juulVal - .01;
                cooldown = 10000;
            }

            if (gamepad1.b) {
                releaseLeftVal = releaseLeftVal + .01;
                cooldown = 10000;
            }
            if (gamepad1.x) {
                releaseLeftVal = releaseLeftVal - .01;
                cooldown = 10000;
            }

            if (gamepad1.y) {
                releaseRightVal = releaseRightVal + .01;
                cooldown = 10000;
            }
            if (gamepad1.a) {
                releaseRightVal = releaseRightVal - .01;
                cooldown = 10000;
            }
        } else {
            cooldown = cooldown - 1;
        }

        //Setting servo values
        robot.flipper.setPosition(Range.clip(flipperVal, 0, 1.0));
        robot.jewelServo.setPosition(Range.clip(juulVal, 0, 1.0));
        robot.releaseLeft.setPosition(Range.clip(releaseLeftVal, 0, 1.0));
        robot.releaseRight.setPosition(Range.clip(releaseRightVal, 0, 1.0));

        //Telemetry
        telemetry.addData("flipperVal", flipperVal);
        telemetry.addData("juulVal", juulVal);
        telemetry.addData("releaseLeftVal", releaseLeftVal);
        telemetry.addData("releaseRightVal", releaseRightVal);

        telemetry.addData("cooldown", cooldown);
    }

    @Override
    public void stop() {

    }
}
