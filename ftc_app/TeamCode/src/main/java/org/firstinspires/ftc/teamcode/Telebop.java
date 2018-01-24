package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Utility.MovementEnum;
import org.firstinspires.ftc.teamcode.Utility.ReleasePosition;

/**
 * Created by aburur on 9/10/17.
 */
@TeleOp(name = "Telebop", group = "Teleop")
public class Telebop extends OpMode {
    Bot robot = new Bot();
    boolean brakeToggle = false;

    int countdown = 0;
    int wallCountdown = 0;

    ReleasePosition currentPosition = ReleasePosition.MIDDLE;
    boolean abnormalReleaseFlag = false;
    boolean i = false;

    double liftScaledown = .7;
    double liftScaleup = .4;

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
    }

    @Override
    public void loop() {
        if (gamepad1.right_bumper && countdown <= 0) {
            brakeToggle = !brakeToggle;
            countdown = 5;
        }

        robot.tankDrive(gamepad1.left_stick_y, gamepad1.right_stick_y, gamepad1.left_trigger, gamepad1.right_trigger, i, brakeToggle); // Tank drive???

        abnormalReleaseFlag = false;
        currentPosition = ReleasePosition.MIDDLE;

        //invert (durrently disabled)
        /*if (gamepad1.left_bumper && countdown <= 0) {
            //i = i ? false : true;
            countdown = 30;
        }*/

        //relic stuff (currently disabled)
        /*
        if (gamepad2.dpad_up) {
            robot.relicArmServo.setPower(.5);
        } else if (gamepad2.dpad_down) {
            robot.relicArmServo.setPower(-.5);
        } else {
            robot.relicArmServo.setPower(0);
        }

        if (gamepad2.dpad_right) {
            robot.relicArmINNOUT.setPower(.5);
        } else if (gamepad2.dpad_left) {
            robot.relicArmINNOUT.setPower(-.5);
        } else {
            robot.relicArmINNOUT.setPower(0);
        }

        if (gamepad1.right_trigger > .3) {
            robot.relicArmVex.setPower(.5);
        } else if (gamepad1.left_trigger > .3) {
            robot.relicArmVex.setPower(-.5);
        } else {
            robot.relicArmVex.setPower(0);
        }*/
        
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

        if (gamepad2.left_stick_y > .15) {
            abnormalReleaseFlag = true;
            currentPosition = ReleasePosition.MIDDLEUP;
            robot.lift.setPower(gamepad2.left_stick_y * liftScaleup);
        } else if (gamepad2.left_stick_y < -.15) {
            abnormalReleaseFlag = true;
            currentPosition = ReleasePosition.MIDDLEUP;
            robot.lift.setPower(gamepad2.left_stick_y * liftScaledown);
        } else {
            if (!abnormalReleaseFlag) {
                currentPosition = ReleasePosition.MIDDLE;
            }
            robot.lift.setPower(0);
        }

        if (!abnormalReleaseFlag) {
            currentPosition = ReleasePosition.MIDDLE;
        }

        if (gamepad2.y ) {
            currentPosition = ReleasePosition.UP;
            robot.flipUp();
            robot.backIntakeWallDown();
            wallCountdown = 40;
        } else if (wallCountdown <= 0 && !abnormalReleaseFlag) {
            currentPosition = ReleasePosition.MIDDLE;
            robot.backIntakeWallUp();
        }

        if (gamepad2.x) {
            robot.jewelServoBottom.setPosition(.3);
        } else {
            robot.jewelUp();
        }

        countdown--;
        wallCountdown--;
        robot.releaseMove(currentPosition);

        telemetry.addData("Braking", brakeToggle);
        telemetry.addData("Brake cooldown? ", countdown > 0 ? "Yep" : "Nope");
        telemetry.addData("Wall on cooldown? ", wallCountdown > 0 ? "Yep" : "Nope");
        telemetry.update();
    }

    @Override
    public void stop() {
        robot.drive(MovementEnum.STOP, 0);
        robot.releaseLeft.close();
        robot.releaseRight.close();
        robot.jewelServoBottom.close();
        robot.jewelServoTop.close();
        robot.flipper.close();
        robot.backIntakeWall.close();
    }

}