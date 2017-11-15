package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by aburur on 9/26/17.
 */


@Autonomous(name="CalibrateGyro", group="Testing")
@Disabled
public class CalibrateGyro extends OpMode {

    BNO055IMU imu;
    BNO055IMU.Parameters parameters;
    boolean calib;

    public void init() {
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        calib = true;
    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {
        telemetry.clear();
        imu.initialize(parameters);
    }

    @Override
    public void loop() {
        if (imu.isGyroCalibrated() && calib)
        {
            telemetry.addLine("Calibrated");
            calib = false;
        }
    }

    @Override
    public void stop() {

    }
}
