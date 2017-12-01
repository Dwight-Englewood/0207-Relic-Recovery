package org.firstinspires.ftc.teamcode;

import android.content.ContentResolver;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

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

        int countdown = 0;
        int ticks = 0;
        int wallCountdown = 0;

        ReleasePosition currentPosition = ReleasePosition.MIDDLE;
        boolean i = false;

        int releaseEncoderMax = 2000; //todo figure out real nuumber

        EnumController<ReleasePosition> controller = new EnumController<>(ReleasePosition.MIDDLE);

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
            //robot.fieldCentricDrive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x); // Field centric????
            robot.tankDrive(gamepad1.left_stick_y, gamepad1.right_stick_y, gamepad1.left_trigger, gamepad1.right_trigger, i, brakeToggle); // Tank drive???

            controller.reset();

            if (gamepad1.left_bumper && countdown <= 0){
                i = !i;
                countdown = 50;
            }

            if (gamepad1.right_bumper && countdown <= 0) {
                brakeToggle = !brakeToggle;
                countdown = 50;
            }

            if (gamepad2.right_bumper) {
                controller.addInstruction(ReleasePosition.DOWN, flag.MODIFY);
                robot.intake(1);
            } else if (gamepad2.right_trigger > .3) {
                controller.addInstruction(ReleasePosition.DOWN, flag.MODIFY);
                robot.intake(-1);
            } else {
                robot.intake(0);
            }

            if (gamepad2.left_trigger > .3){
                robot.frontIntakeWallUp();
                controller.addInstruction(ReleasePosition.DOWNER, flag.MODIFY);
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
                controller.addInstruction(ReleasePosition.MIDDLEUP, flag.MODIFY);
                robot.lift.setPower(-.5);
            } else if (gamepad2.dpad_up) {
                controller.addInstruction(ReleasePosition.MIDDLEUP, flag.MODIFY);
                robot.lift.setPower(1);
            } else {
                robot.lift.setPower(0);
            }

            controller.addInstruction(ReleasePosition.MIDDLE, flag.NORMAL);

            if (gamepad2.y) {
                controller.addInstruction(ReleasePosition.UP, flag.OVERRIDE);
                robot.flipUp();
                robot.backIntakeWallDown();
                wallCountdown = 30;
            } else if (wallCountdown <= 0) {
                controller.addInstruction(ReleasePosition.MIDDLE, flag.NORMAL);
                robot.backIntakeWallUp();
            }

            if (gamepad2.x){
                robot.jewelServo.setPosition(.3);
            } else {
                robot.jewelUp();
            }




            countdown--;
            wallCountdown--;
            currentPosition = controller.processAndGetCurrentVal();
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