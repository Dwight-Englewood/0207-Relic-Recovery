package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Enums.MovementEnum;

/**
 * Created by aburur on 9/10/17.
 */
@TeleOp(name="Telebop", group="Teleop")
public class Telebop extends OpMode
{
        Bot robot = new Bot();
        double servoIncrement = .05;
        double topServo = 0;
        double botServo = 0;
        @Override
        public void init() {
            robot.init(hardwareMap, telemetry);
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
            robot.fieldCentricDrive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

            if (gamepad2.x) {
                robot.intake(1);
            } else if (gamepad2.b) {
                robot.intake(-1);
            } else if (gamepad2.left_trigger < .3 && gamepad2.right_trigger < .3 && !gamepad2.left_bumper && !gamepad2.right_bumper){
                robot.intake(0);
            }

            if (gamepad2.right_trigger > .3){
                robot.intakeOne.setPower(gamepad2.right_trigger);
            }
            if (gamepad2.left_trigger > .3) {
                robot.intakeTwo.setPower(gamepad2.left_trigger);
            }
            if (gamepad2.right_bumper){
                robot.intakeOne.setPower(-.5);
            }
            if (gamepad2.left_bumper){
                robot.intakeTwo.setPower(-.5);
            }



            if (gamepad2.right_stick_y > .3){
                robot.intakeBrake.setPower(.3);
            } else if (gamepad2.right_stick_y < -.3){
                robot.intakeBrake.setPower(-.3);
            } else {
                robot.intakeBrake.setPower(0);
            }

            if (gamepad1.x) {
                robot.releaseTheKraken();
                //if it goes the wrong way use the other one
            }
            if (gamepad1.y) {
                robot.releaseTheGiantSquid();
            }

            if (gamepad1.dpad_down) {
                botServo = botServo + servoIncrement;
            } else if (gamepad2.dpad_up) {
                botServo = topServo - servoIncrement;
            }

            if (gamepad2.dpad_left) {
                topServo = topServo - servoIncrement;
            } else if (gamepad2.dpad_right) {
                topServo = topServo + servoIncrement;
            }

            Range.clip(topServo, 0, 1);
            Range.clip(botServo, 0, 1);

            robot.armBotServoPos(botServo);
            robot.armTopServoPos(topServo);
        }

        @Override
        public void stop() {
            robot.drive(MovementEnum.STOP, 0);
        }

}