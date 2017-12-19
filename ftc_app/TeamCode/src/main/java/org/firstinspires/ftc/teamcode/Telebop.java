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
    long time0 = 0;
    long time1 = 0;
    long time100 = 0;
    long time1000 = 0;
    long time10000 = 0;
    long time100000 = 0;

    double liftScale = .3;

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

        if (firstRun) {
            time0 = System.nanoTime();
            firstRun = false;
        }

        if (loopNum == 1) {
            time1 = System.nanoTime();
        } else if (loopNum == 100) {
            time100 = System.nanoTime();
        } else if (loopNum == 1000) {
            time1000 = System.nanoTime();
        } else if (loopNum == 10000) {
            time10000 = System.nanoTime();
        } else if (loopNum == 100000) {
            time100000 = System.nanoTime();
        }
        //robot.fieldCentricDrive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x); // Field centric????
        robot.tankDrive(gamepad1.left_stick_y, gamepad1.right_stick_y, gamepad1.left_trigger, gamepad1.right_trigger, i, brakeToggle); // Tank drive???

        abnormalReleaseFlag = false;
        currentPosition = ReleasePosition.MIDDLE;

        if (gamepad1.left_bumper && countdown <= 0) {
            //i = i ? false : true;
            countdown = 50;
        }

        if (gamepad1.right_bumper && countdown <= 0) {
            brakeToggle = brakeToggle ? false : true;
            countdown = 30;
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
            robot.lift.setPower(gamepad2.left_stick_y * liftScale);
        } else if (gamepad2.left_stick_y < -.15) {
            abnormalReleaseFlag = true;
            currentPosition = ReleasePosition.MIDDLEUP;
            robot.lift.setPower(gamepad2.left_stick_y * liftScale);
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
        telemetry.addData("time0", time0);
        telemetry.addData("time1", time1);
        telemetry.addData("time100", time100);
        telemetry.addData("time1000", time1000);
        telemetry.addData("time10000", time10000);
        telemetry.addData("time100000", time100000);
        telemetry.addData("time1Avg", time1 - time0);
        telemetry.addData("time100Avg", (time100 - time0) / 100);
        telemetry.addData("time1000Avg", (time1000 - time0) / 1000);
        telemetry.addData("time10000Avg", (time10000 - time0) / 10000);
        telemetry.addData("time100000Avg", (time100000 - time0) / 100000);

        telemetry.update();
    }

    @Override
    public void stop() {
        robot.drive(MovementEnum.STOP, 0);
    }

}