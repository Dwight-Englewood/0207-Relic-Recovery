package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Utility.MovementEnum;
import org.firstinspires.ftc.teamcode.Utility.ReleasePosition;

/**
 * Created by aburur on 1/16/18.
 */
@TeleOp(name = "Lineup Test V2", group = "Testing")
@Disabled
public class LineupTestV2 extends OpMode
{
    Bot robot = new Bot();
    boolean aligned = false;
    int command = 0;
    ElapsedTime timer = new ElapsedTime();
    boolean thing = true;
    double currentDistance;

    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.jewelUp();
        robot.backIntakeWallUp();
        robot.releaseMove(ReleasePosition.INIT);
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        timer.reset();
        robot.jewelUp();
        robot.backIntakeWallUp();
        robot.releaseMove(ReleasePosition.INIT);
    }

    @Override
    public void loop() {

        if (gamepad1.a) {
            command = 0;
            timer.reset();
            aligned = false;
        }

        if (aligned) {
            robot.backIntakeWallDown();
            robot.releaseMove(ReleasePosition.UP);
            robot.drive(MovementEnum.STOP);
        } else {
            robot.backIntakeWallUp();
            robot.releaseMove(ReleasePosition.INIT);
        }

        switch (command) {
            case 0:
                if (robot.FR.getPower() == 0) {
                    command++;
                    timer.reset();
                } else {
                    robot.adjustHeading(0, true, telemetry);
                }
                break;

            case 1:
                currentDistance = robot.rangeRight.getDistance(DistanceUnit.CM);
                if (Math.abs(currentDistance - 119) < 2) {
                    robot.drive(MovementEnum.STOP);
                    timer.reset();
                    thing = true;
                    command++;
                } else if (currentDistance < 119) {
                    if (thing) {
                        robot.drive(MovementEnum.LEFTSTRAFE, .3);
                        thing = false;
                    }
                    adjustPower(0, false);
                } else {
                    if (!thing) {
                        robot.drive(MovementEnum.RIGHTSTRAFE, .1);
                        thing = true;
                    }
                    adjustPower(0, false);
                }
                break;

            case 2:
                if (robot.FR.getPower() == 0) {
                    robot.jewelOut();
                    command++;
                    timer.reset();
                } else {
                    robot.adjustHeading(0, true, telemetry);
                }
                break;

            case 3:
                currentDistance = robot.rangeBack.getDistance(DistanceUnit.CM);
                double power = robot.slowDownScale(37, currentDistance, timer);
                if (power == 0) {
                    robot.drive(MovementEnum.STOP);
                    robot.jewelUp();
                    timer.reset();
                    command++;
                } else if (currentDistance < 37) {
                    robot.drive(MovementEnum.FORWARD, power);
                    timer.reset();

                } else {
                    robot.drive(MovementEnum.BACKWARD, power);
                    timer.reset();
                }
                /*if (timer.milliseconds() > 500) {
                    currentDistance = robot.rangeBack.getDistance(DistanceUnit.CM);
                    if (Math.abs(currentDistance - 37) < 2) {
                        robot.drive(MovementEnum.STOP);
                        timer.reset();
                        thing = true;
                        command++;
                    } else if (currentDistance < 37) {
                        if (thing) {
                            robot.drive(MovementEnum.FORWARD, .1);
                            thing = false;
                        }
                        adjustPower(0, true);
                    } else {
                        if (!thing) {
                            robot.drive(MovementEnum.BACKWARD, .1);
                            thing = true;
                        }
                        adjustPower(0, true);
                    }
                }*/
                break;

            case 4:
                if (robot.FR.getPower() == 0) {
                    command++;
                    timer.reset();
                    aligned = true;
                } else {
                    robot.adjustHeading(0, true, telemetry);
                }
                break;

        }

        telemetry.addData("Range back", robot.rangeBack.getDistance(DistanceUnit.CM));
        telemetry.addData("Range Right", robot.rangeRight.getDistance(DistanceUnit.CM));
        telemetry.addData("heading", robot.imu.getAngularOrientation().firstAngle);
        telemetry.addData("Current Distance", currentDistance);
        telemetry.addData("Command: ", command);
        telemetry.addData("Aligned: ", aligned);
        telemetry.update();
    }


    @Override
    public void stop() {

    }

    public void adjustPower(int targetHeading, boolean goingStraight) {
        double headingError = targetHeading - robot.imu.getAngularOrientation().firstAngle;
        double driveScale = headingError * .01;

        if (goingStraight) {
            int i = robot.FL.getPower() < 0 ? -1 : 0;
            int j = robot.FL.getPower() < 0 ? 0 : 1;

            robot.FL.setPower(Range.clip(robot.FL.getPower() - driveScale, i, j));
            robot.BL.setPower(Range.clip(robot.BL.getPower() - driveScale, i, j));
            robot.FR.setPower(Range.clip(robot.FR.getPower() + driveScale, i, j));
            robot.BR.setPower(Range.clip(robot.BR.getPower() + driveScale, i, j));
        } else {
            robot.FL.setPower(Range.clip(robot.FL.getPower() + driveScale, -1, 1));
            robot.BL.setPower(Range.clip(robot.BL.getPower() + driveScale, -1, 1));
            robot.FR.setPower(Range.clip(robot.FR.getPower() - driveScale, -1, 1));
            robot.BR.setPower(Range.clip(robot.BR.getPower() - driveScale, -1, 1));
        }

    }

}
