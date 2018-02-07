package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;

/**
 * Created by aburur on 9/26/17.
 */


@Autonomous(name = "CalibrateGyro", group = "Testing")
//@Disabled
public class CalibrateGyro extends OpMode {

    BNO055IMU imu;
    BNO055IMU.Parameters parameters;
    boolean calib;

    public void init() {
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        parameters = new BNO055IMU.Parameters();
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        calib = true;

        telemetry.addLine("Ready.");
        telemetry.update();
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

        if (imu.isGyroCalibrated() && calib) {
            BNO055IMU.CalibrationData calibrationData = imu.readCalibrationData();
            String filename = "BNO055IMUCalibration.json";
            File file = AppUtil.getInstance().getSettingsFile(filename);
            ReadWriteFile.writeFile(file, calibrationData.serialize());
            telemetry.log().add("saved to '%s'", filename);
            calib = false;
        }
        if (!calib) {
            telemetry.addLine("done");
        }

        telemetry.addData("Status", imu.getSystemStatus().toShortString());
        telemetry.addData("Calib Status", imu.getCalibrationStatus().toString());
        telemetry.addData("Gyro Calib?", imu.isGyroCalibrated());
        telemetry.addData("heading", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
        telemetry.update();
    }

    @Override
    public void stop() {

    }
}
