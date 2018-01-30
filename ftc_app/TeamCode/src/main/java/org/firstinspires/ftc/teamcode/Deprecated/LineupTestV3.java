package org.firstinspires.ftc.teamcode.Deprecated;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Utility.ReleasePosition;

/**
 * Created by aburur on 1/16/18.
 */
@TeleOp(name = "Lineup Test V3", group = "Testing")
@Disabled
public class LineupTestV3 extends OpMode
{
    Bot robot = new Bot();
    ElapsedTime timer = new ElapsedTime();
    BNO055IMU.Parameters parameters;
    boolean calib;

    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.jewelUp();
        robot.backIntakeWallUp();
        robot.releaseMove(ReleasePosition.INIT);

        parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        calib = true;
        robot.imu.initialize(parameters);
    }

    @Override
    public void init_loop() {
        if (robot.imu.isGyroCalibrated() && calib)
            calib = false;
        if (!calib)
            telemetry.addLine("done");

        telemetry.addData("heading", robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
        telemetry.update();
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



        telemetry.addData("heading", robot.imu.getAngularOrientation().firstAngle);
        telemetry.addData("right distance", robot.rangeRight.getDistance(DistanceUnit.CM));
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

            robot.FL.setPower(Range.clip(robot.FL.getPower() + driveScale, i, j));
            robot.BL.setPower(Range.clip(robot.BL.getPower() + driveScale, i, j));
            robot.FR.setPower(Range.clip(robot.FR.getPower() - driveScale, i, j));
            robot.BR.setPower(Range.clip(robot.BR.getPower() - driveScale, i, j));
        } else {
            robot.FL.setPower(Range.clip(robot.FL.getPower() + driveScale, -1, 1));
            robot.BL.setPower(Range.clip(robot.BL.getPower() + driveScale, -1, 1));
            robot.FR.setPower(Range.clip(robot.FR.getPower() - driveScale, -1, 1));
            robot.BR.setPower(Range.clip(robot.BR.getPower() - driveScale, -1, 1));
        }

    }

}
