package org.firstinspires.ftc.teamcode.Telebop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Utility.Bot;
import org.firstinspires.ftc.teamcode.Utility.ReleasePosition;

/**
 * Created by aburur on 12/12/17.
 */
@TeleOp(name = "KIDS TANK", group = "Teleop")
@Disabled
public class Kids_TelebopTank extends OpMode
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
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        telemetry.clear();
        robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
        //ticks = robot.lift.getCurrentPosition();
    }

    @Override
    public void loop() {


        //robot.fieldCentricDrive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.left_trigger, gamepad1.right_trigger, false); // Field centric????
        robot.tankDrive(gamepad1.left_stick_y, gamepad1.right_stick_y, gamepad1.left_trigger, gamepad1.right_trigger, i, brakeToggle);


        abnormalReleaseFlag = false;
        currentPosition = ReleasePosition.MIDDLE;

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
                currentPosition = ReleasePosition.MIDDLE;
            }
            robot.lift.setPower(0);
        }

        if (!abnormalReleaseFlag) {
            currentPosition = ReleasePosition.MIDDLE;
        }

        if (gamepad2.y) {
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



        telemetry.addData("release pos", currentPosition);
        telemetry.update();
    }
}
