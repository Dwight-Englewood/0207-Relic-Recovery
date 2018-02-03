package org.firstinspires.ftc.teamcode.Deprecated;
/**
 * Created by weznon on 11/5/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Utility.Bot;


/**
 * Teleop which tests the arm servo movements for their full range
 */
//@TeleOp(name = "ServoArmTestTeleop", group = "Teleop")
@Disabled
public class ServoArmTestTeleop extends OpMode {
    Bot robot = new Bot();

    double positionBot = .5;
    double positionTop = .5;

    boolean directionFlagBot = false;
    boolean directionFlagTop = false;

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        //robot.armBottomExtendyServo.setPosition(.5);
        //robot.armTopExtendyServo.setPosition(.5);
    }

    @Override
    public void loop() {
        if (positionBot < 0) {
            positionBot = 0;
            directionFlagBot = true;
        } else if (positionBot > 1) {
            positionBot = 1;
            directionFlagBot = false;
        }

        if (positionTop < 0) {
            positionTop = 0;
            directionFlagTop = true;
        } else if (positionTop > 1) {
            positionTop = 1;
            directionFlagTop = false;
        }

        if (directionFlagBot) {
            positionBot = positionBot + .01;
        } else {
            positionBot = positionBot - .01;
        }

        if (directionFlagTop) {
            positionTop = positionTop + .01;
        } else {
            positionTop = positionTop - .01;
        }

        //robot.armBottomExtendyServo.setPosition(positionBot);
        //robot.armTopExtendyServo.setPosition(positionTop);
        telemetry.addData("positionBot", positionBot);
        telemetry.addData("positionTop", positionTop);
    }

    @Override
    public void stop() {

    }
}
