package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Timer;

/**
 * Created by aburur on 9/14/17.
 */

    @Autonomous(name = "TestdsfBotAuton", group = "Auton")
    //@Disabled
    public class TestBotAuton extends LinearOpMode {

        BNO055IMU imu;
        DcMotor fl, fr, bl, br;

        @Override
        public void runOpMode() {

            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.calibrationDataFile = "BNO055IMUCalibration.json";
            imu = hardwareMap.get(BNO055IMU.class, "imu");
            imu.initialize(parameters);

            //Motors
            fl = hardwareMap.get(DcMotor.class, "fl");
            fr = hardwareMap.get(DcMotor.class, "fr");
            fr.setDirection(DcMotorSimple.Direction.REVERSE);
            bl = hardwareMap.get(DcMotor.class, "bl");
            br = hardwareMap.get(DcMotor.class, "br");
            br.setDirection(DcMotorSimple.Direction.REVERSE);



            // wait for the start button to be pressed.
            waitForStart();

            while (opModeIsActive()) {

                Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

                telemetry.addData("imu orientation", angles.firstAngle);
                telemetry.update();
            }

                telemetry.update();
            }

}
