package org.firstinspires.ftc.teamcode.Testing;
/**
 * Created by weznon on 11/15/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Utility.Bot;

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
@Disabled
public class ServoValueFinder extends OpMode {
    Bot robot = new Bot();

    /*double flipperVal = .5;
    double brandonVal = .5;
    double releaseLeftVal = .5;
    double releaseRightVal = .5;
    double frontIntakeWallVal = .5;
    double backIntakeWallVal = .5;
    double hahnVal = .5;
    double relicArmServo1 = .5;
    double relicArmServo2 = .5;
    */

    int cooldown = 0;

    final int cooldownTime = 10;

    double bottomVal = .5;
    double topVal = .5;

    double frontVal = .5;
    double backVal = .5;

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

        /*if (cooldown < 0) {
            if (gamepad1.a) {
                topVal += .01;
                cooldown = cooldownTime;
            }
            if (gamepad1.b) {
                topVal -= .01;
                cooldown = cooldownTime;
            }
            if (gamepad1.x) {
                bottomVal += .01;
                cooldown = cooldownTime;
            }
            if (gamepad1.y) {
                bottomVal -= .01;
                cooldown = cooldownTime;
            }
        } else {
            cooldown --;
        }

        topVal = Range.clip(topVal, 0, 1);
        bottomVal = Range.clip(bottomVal, 0, 1);

        robot.jewelServoBottom.setPosition(bottomVal);
        robot.jewelServoTop.setPosition(topVal);*/

        if (cooldown < 0) {
            if (gamepad1.a) {
                frontVal += .01;
                cooldown = cooldownTime;
            }
            if (gamepad1.y) {
                frontVal -= .01;
                cooldown = cooldownTime;
            }
            if (gamepad1.b) {
                backVal += .01;
                cooldown = cooldownTime;
            }
            if (gamepad1.x) {
                backVal -= .01;
                cooldown = cooldownTime;
            }
        } else {
            cooldown--;
        }

        frontVal = Range.clip(frontVal ,0, 1);
        backVal = Range.clip(backVal, 0, 1);

        robot.glyphClamps.front.setPosition(frontVal);
        robot.glyphClamps.back.setPosition(backVal);

        telemetry.addData("Front", frontVal);
        telemetry.addData("back", backVal);
    }

    @Override
    public void stop() {

    }
}
