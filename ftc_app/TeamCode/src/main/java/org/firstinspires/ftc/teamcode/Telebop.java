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
        boolean brakeToggle = false;
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
            if (gamepad1.right_bumper) {
                brakeToggle = brakeToggle ? false : true;
            }
            //robot.fieldCentricDrive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x); // Field centric????
            robot.tankDrive(gamepad1.left_stick_y, gamepad1.right_stick_y, gamepad1.left_trigger, gamepad1.right_trigger, false, brakeToggle); // Tank drive???

            if (gamepad2.right_bumper) {
                robot.intake(1);
            } else if (gamepad2.right_trigger > .3) {
                robot.intake(-1);
            } else {
                robot.intake(0);
            }

            if (gamepad2.right_stick_y > .3) {
                robot.intakeBrake.setPower(-1);
            } else if (gamepad2.right_stick_y < -.3) {
                robot.intakeBrake.setPower(1);
            } else {
                robot.intakeBrake.setPower(0);
            }

            if (gamepad2.b) {
                robot.flipUp();
            } else {
                robot.flipDown();
            }

            if (gamepad2.dpad_up) {
                robot.lift.setPower(-.5);
            } else if (gamepad2.dpad_down) {
                robot.lift.setPower(1);
            } else {
                robot.lift.setPower(0);
            }

            if (gamepad2.y) {
                robot.relLUp();
                robot.relRUp();
            } else if (gamepad2.a) {
                robot.relLDown();
                robot.relRDown();
            }

            telemetry.addData("Braking", brakeToggle);
            telemetry.update();
        }

        @Override
        public void stop() {
            robot.drive(MovementEnum.STOP, 0);
        }

}