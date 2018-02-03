package org.firstinspires.ftc.teamcode.Deprecated;

/**
 * Created by weznon on 11/3/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Utility.Bot;
import org.firstinspires.ftc.teamcode.Utility.MovementEnum;


//@TeleOp(name="OneButtonMan", group="Teleop")
@Disabled
public class OneButtonMan extends OpMode {
    Bot robot = new Bot();

    double topServo = 0;
    double botServo = 0;

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
        if (gamepad1.right_trigger > .5) {
            robot.intake(1);
        } else {
            robot.intake(0);
        }

        if (gamepad1.a) {
            robot.intakeDrop.setPower(.3);
        } else {
            robot.intakeDrop.setPower(0);
        }

        if (gamepad1.a) {
            //robot.releaseTheKraken();
            //if it goes the wrong way use the other one
        }

        if (gamepad2.dpad_up) {
            robot.FL.setPower(.5);
        } else {
            robot.FL.setPower(0);
        }

        if (gamepad2.dpad_down) {
            robot.BL.setPower(.5);
        } else {
            robot.BL.setPower(0);
        }

        if (gamepad2.y) {
            robot.FR.setPower(.5);
        } else {
            robot.FR.setPower(0);
        }

        if (gamepad2.a) {
            robot.BR.setPower(.5);
        } else {
            robot.BR.setPower(0);
        }

        if (gamepad2.x) {
            robot.intakeOne.setPower(.5);
        } else {
            robot.intakeOne.setPower(0);
        }

        if (gamepad2.b) {
            robot.intakeTwo.setPower(.5);
        } else {
            robot.intakeTwo.setPower(0);
        }

        if (gamepad1.dpad_down) {
            botServo = botServo + .01;
        } else if (gamepad1.dpad_up) {
            topServo = topServo - .01;
        }

        if (gamepad1.dpad_left) {
            topServo = topServo - .01;
        } else if (gamepad1.dpad_right) {
            topServo = topServo + .01;
        }

        //cleaner way? prolly
        if (botServo < 0) {
            botServo = 0;
        } else if (botServo > 1) {
            botServo = 1;
        }

        if (topServo < 0) {
            topServo = 0;
        } else if (topServo > 1) {
            topServo = 1;
        }

        //robot.armBotServoPos(botServo);
        //robot.armTopServoPos(topServo);
    }

    @Override
    public void stop() {
        robot.drive(MovementEnum.STOP, 0);
    }

}