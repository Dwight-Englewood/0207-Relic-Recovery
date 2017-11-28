package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Enums.MovementEnum;
import org.firstinspires.ftc.teamcode.Enums.ReleasePosition;

/**
 * Created by plotnw on 11/21/17.
 */

//This is out of date, as the normal telebop is the teleop where the changes will be made

@TeleOp(name="TelebopFieldCentric", group="Teleop")
public class TelebopFieldCentric extends OpMode
{
        Bot robot = new Bot();
        boolean brakeToggle = false;

        int countdown = 0;
        int ticks = 0;
        int wallCountdown = 0;

        ReleasePosition currentPosition = ReleasePosition.MIDDLE;
        boolean abnormalReleaseFlag = false;
        boolean i = false;

        @Override
        public void init() {
            robot.init(hardwareMap);
            robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.colorSensor.enableLed(false);
        }

        @Override
        public void init_loop() {}

        @Override
        public void start() {
            telemetry.clear();
            robot.jewelUp();
            robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
            ticks = robot.lift.getCurrentPosition();

        }

        @Override
        public void loop()

        {
            abnormalReleaseFlag = false;
            currentPosition = ReleasePosition.MIDDLE;

            if (gamepad1.left_bumper && countdown <= 0){
                i = i ? false:true;
                countdown = 50;
            }

            if (gamepad1.right_bumper && countdown <= 0) {
                brakeToggle = brakeToggle ? false : true;
                countdown = 50;
            }
            robot.fieldCentricDrive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.left_trigger, gamepad1.right_trigger, brakeToggle);

            if (gamepad2.right_bumper) {
                abnormalReleaseFlag = true;
                currentPosition = ReleasePosition.DOWN;
                robot.intake(1);
            } else if (gamepad2.right_trigger > .3) {
                abnormalReleaseFlag = true;
                currentPosition = ReleasePosition.DOWN;
                robot.intake(-1);
            } else {
                if (!abnormalReleaseFlag) {
                    currentPosition = ReleasePosition.MIDDLE;
                }
                robot.intake(0);
            }

            if (gamepad2.left_trigger > .3){
                robot.frontIntakeWallUp();
                abnormalReleaseFlag = true;
                currentPosition = ReleasePosition.DOWNER;
            } else {
                robot.frontIntakeWallDown();
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
            } else if (!gamepad2.y) {
                robot.flipDown();
            }

            if (gamepad2.dpad_down) {
                abnormalReleaseFlag = true;
                currentPosition = ReleasePosition.MIDDLEUP;
                robot.lift.setPower(-.5);
            } else if (gamepad2.dpad_up) {
                abnormalReleaseFlag = true;
                currentPosition = ReleasePosition.MIDDLEUP;
                robot.lift.setPower(1);
            } else {
                if (!abnormalReleaseFlag) {
                    currentPosition = ReleasePosition.MIDDLE;
                }
                robot.lift.setPower(0);
            }

            if (!abnormalReleaseFlag) {
                //if (robot.lift.getCurrentPosition() - ticks < 100) {
                //currentPosition = ReleasePosition.MIDDLEUP;
                //} else {
                currentPosition = ReleasePosition.MIDDLE;
                //}
            }

            if (gamepad2.y) {
                currentPosition = ReleasePosition.UP;
                robot.flipUp();
                robot.backIntakeWallDown();
                wallCountdown = 20;
            } else if (wallCountdown <= 0 && !abnormalReleaseFlag) {
                currentPosition = ReleasePosition.MIDDLE;
                robot.backIntakeWallUp();
            }

            if (gamepad2.x){
                robot.jewelServo.setPosition(.3);
            } else {
                robot.jewelUp();
            }

            countdown--;
            wallCountdown--;
            robot.releaseMove(currentPosition);

            telemetry.addData("release pos", currentPosition);
            telemetry.addData("Braking", brakeToggle);
            telemetry.update();
        }

        @Override
        public void stop() {
            robot.drive(MovementEnum.STOP, 0);
        }

}