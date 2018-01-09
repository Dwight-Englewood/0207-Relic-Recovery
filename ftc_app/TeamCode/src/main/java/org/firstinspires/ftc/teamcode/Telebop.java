package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Enums.MovementEnum;
import org.firstinspires.ftc.teamcode.Enums.ReleasePosition;

/**
 * Created by aburur on 9/10/17.
 */
@TeleOp(name = "Telebop", group = "Teleop")
public class Telebop extends OpMode {
    Bot robot = new Bot();
    boolean brakeToggle = false;

    int countdown = 0;
    int ticks = 0;
    int wallCountdown = 0;

    ReleasePosition currentPosition = ReleasePosition.MIDDLE;
    boolean abnormalReleaseFlag = false;
    boolean i = false;

    //int releaseEncoderMax = 2000; //todo figure out real nuumber

    boolean firstRun = true;
    int loopNum = 0;
    /*long time0 = 0;
    long time1 = 0;
    long time100 = 0;
    long time1000 = 0;
    long time10000 = 0;
    long time100000 = 0;*/

    double liftScaledown = .5;
    double liftScaleup = .2;

    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.colorSensor.enableLed(false);
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        telemetry.clear();
        robot.jewelUp();
        robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
        //ticks = robot.lift.getCurrentPosition();
    }

    @Override
    public void loop() {
        if (gamepad1.right_bumper && countdown <= 0) {
            brakeToggle = !brakeToggle;
            countdown = 30;
        }

        robot.tankDrive(gamepad1.left_stick_y, gamepad1.right_stick_y, gamepad1.left_trigger, gamepad1.right_trigger, i, brakeToggle); // Tank drive???

        abnormalReleaseFlag = false;
        currentPosition = ReleasePosition.MIDDLE;

        //Invert (Currently Disabled)
        /*if (gamepad1.left_bumper && countdown <= 0) {
            //i = i ? false : true;
            countdown = 50;
        }*/

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
        }
        
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

        if (gamepad2.left_trigger > .3) {
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

        loopNum = loopNum + 1;

        telemetry.addData("release pos", currentPosition);
        telemetry.addData("Braking", brakeToggle);
        telemetry.update();
    }

    @Override
    public void stop() {
        robot.drive(MovementEnum.STOP, 0);
    }

}