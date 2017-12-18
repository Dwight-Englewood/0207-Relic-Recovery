package org.firstinspires.ftc.teamcode.Testing;
/**
 * Created by weznon on 11/15/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Bot;

/**
 * Flip:
 * down - .05
 * up - .55
 * out - .75
 * <p>
 * R release:
 * down - .98
 * up - .52
 * out - .02
 * <p>
 * L release:
 * down - .02
 * up - .48
 * out - .98
 * <p>
 * Jewel:
 * down - 1
 * up - .65
 * out - .1
 */

@TeleOp(name = "ServoValueFinder", group = "Teleop")
//@Disabled
public class ServoValueFinder extends OpMode {
    Bot robot = new Bot();

    double flipperVal = .5;
    double brandonVal = .5;
    double releaseLeftVal = .5;
    double releaseRightVal = .5;
    double frontIntakeWallVal = .5;
    double backIntakeWallVal = .5;
    double hahnVal = .5;


    int cooldown = 0;

    int cooldownTime = 50;

    @Override
    public void init() {
        robot.init(hardwareMap);
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
            if (gamepad2.dpad_right) {
                flipperVal = flipperVal + .01;
                cooldown = cooldownTime;
            }
            if (gamepad2.dpad_left) {
                flipperVal = flipperVal - .01;
                cooldown = cooldownTime;
            }

            if (gamepad1.dpad_up) {
                brandonVal = brandonVal + .01;
                cooldown = cooldownTime;
            }
            if (gamepad1.dpad_down) {
                brandonVal = brandonVal - .01;
                cooldown = cooldownTime;
            }
            if (gamepad1.dpad_left) {
                hahnVal = hahnVal - .01;
                cooldown = cooldownTime;
            }
            if (gamepad1.dpad_right) {
                hahnVal = hahnVal + .01;
                cooldown = cooldownTime;
            }
            if (gamepad1.b) {
                releaseLeftVal = releaseLeftVal + .01;
                releaseRightVal = releaseRightVal - .01;
                cooldown = cooldownTime;
            }
            if (gamepad1.x) {
                releaseLeftVal = releaseLeftVal - .01;
                releaseRightVal = releaseRightVal + .01;
                cooldown = cooldownTime;
            }

            if (gamepad1.a) {
                frontIntakeWallVal = frontIntakeWallVal + .01;
                cooldown = cooldownTime;
            }

            if (gamepad1.y) {
                frontIntakeWallVal = frontIntakeWallVal - .01;
                cooldown = cooldownTime;
            }

            if (gamepad2.a) {
                backIntakeWallVal = backIntakeWallVal + .01;
                cooldown = cooldownTime;
            }

            if (gamepad2.y) {
                backIntakeWallVal = backIntakeWallVal - .01;
                cooldown = cooldownTime;
            }
        } else {
            cooldown -= 1;
        }

        flipperVal = Range.clip(flipperVal, 0, 1.0);
        brandonVal = Range.clip(brandonVal, 0, 1.0);
        hahnVal = Range.clip(hahnVal, 0, 1.0);
        releaseLeftVal = Range.clip(releaseLeftVal, 0, 1.0);
        releaseRightVal = Range.clip(releaseRightVal, 0, 1.0);
        frontIntakeWallVal = Range.clip(frontIntakeWallVal, 0, 1.0);
        backIntakeWallVal = Range.clip(backIntakeWallVal, 0, 1.0);

        //Setting servo values
        robot.flipper.setPosition(flipperVal);
        robot.jewelServoBottom.setPosition(brandonVal);
        robot.releaseLeft.setPosition(releaseLeftVal);
        robot.releaseRight.setPosition(releaseRightVal);
        robot.frontIntakeWall.setPosition(frontIntakeWallVal);
        robot.backIntakeWall.setPosition(backIntakeWallVal);
        robot.jewelServoTop.setPosition(hahnVal);

        //Telemetry
        telemetry.addData("flipperVal", flipperVal);
        telemetry.addData("juulVal", brandonVal);
        telemetry.addData("releaseLeftVal", releaseLeftVal);
        telemetry.addData("releaseRightVal", releaseRightVal);
        telemetry.addData("frontIntakeWallVal", frontIntakeWallVal);
        telemetry.addData("backIntakeWallVal", backIntakeWallVal);

        telemetry.addData("cooldown", cooldown);
        telemetry.addData("cooldownTime", cooldownTime);
        telemetry.update();

    }

    @Override
    public void stop() {

    }
}
