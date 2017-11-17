package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Enums.MovementEnum;
import org.firstinspires.ftc.teamcode.Enums.ReleasePosition;

/**
 * Created by aburur on 9/10/17.
 */
@TeleOp(name="Telebop", group="Teleop")
public class Telebop extends OpMode
{
        Bot robot = new Bot();
        boolean brakeToggle = false;
        ReleasePosition currentPosition = ReleasePosition.DOWN;
        boolean abnormalReleaseFlag = false;
        //double servoIncrement = .06;
        //double topServo = 0;
        //double botServo = 0;

        @Override
        public void init() {
            robot.init(hardwareMap);
        }

        @Override
        public void init_loop() {}

        @Override
        public void start() {
            telemetry.clear();
            robot.jewelUp();
        }

        @Override
        public void loop()

        {
            abnormalReleaseFlag = false;
            if (gamepad1.right_bumper) {
                brakeToggle = brakeToggle ? false : true;
            }
            //robot.fieldCentricDrive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x); // Field centric????
            robot.tankDrive(gamepad1.left_stick_y, gamepad1.right_stick_y, gamepad1.left_trigger, gamepad1.right_trigger, false, brakeToggle); // Tank drive???

            if (gamepad2.right_bumper) {
                abnormalReleaseFlag = true;
                currentPosition = ReleasePosition.MIDDLE;
                robot.intake(1);
            } else if (gamepad2.right_trigger > .3) {
                abnormalReleaseFlag = true;
                currentPosition = ReleasePosition.MIDDLE;
                robot.intake(-1);
            } else {
                if (!abnormalReleaseFlag) {
                    currentPosition = ReleasePosition.DOWN;
                }
                robot.intake(0);
            }

            if (gamepad2.right_stick_y > .3) {
                robot.intakeDrop.setPower(-1);
            } else if (gamepad2.right_stick_y < -.3) {
                robot.intakeDrop.setPower(1);
            } else {
                robot.intakeDrop.setPower(0);
            }

            if (gamepad2.b) {
                robot.flipUp();
            } else {
                robot.flipDown();
            }

            if (gamepad2.dpad_up) {
                abnormalReleaseFlag = true;
                currentPosition = ReleasePosition.MIDDLEUP;
                robot.lift.setPower(-.5);
            } else if (gamepad2.dpad_down) {
                abnormalReleaseFlag = true;
                currentPosition = ReleasePosition.MIDDLEUP;
                robot.lift.setPower(1);
            } else {
                if (!abnormalReleaseFlag) {
                    currentPosition = ReleasePosition.DOWN;
                }
                robot.lift.setPower(0);
            }

            if (gamepad2.y) {
                currentPosition = ReleasePosition.UP;
            } else if (gamepad2.a) {
                currentPosition = ReleasePosition.DOWN;
            }

           robot.releaseMove(currentPosition);

            telemetry.addData("Braking", brakeToggle);
            telemetry.update();
        }

        @Override
        public void stop() {
            robot.drive(MovementEnum.STOP, 0);
        }

}