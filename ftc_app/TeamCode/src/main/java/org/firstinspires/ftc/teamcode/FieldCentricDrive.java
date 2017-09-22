package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by aburur on 9/20/17.
 */

@TeleOp(name="FieldCentricDrive", group="Testing")
//@Disabled
public class FieldCentricDrive extends OpMode
{
    double temp, forward, right, clockwise, k, frontLeft, frontRight, rearLeft, rearRight;
    BNO055IMU imu;

    DcMotor fl, fr, bl, br;

    Orientation angles;

    @Override
    public void init() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        bl = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");
        br.setDirection(DcMotorSimple.Direction.REVERSE);



    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {
        k = 0;
    }

    @Override
    public void loop() {
        forward = (-1)*gamepad1.left_stick_y;
        right =  gamepad1.left_stick_x;
        clockwise = gamepad1.right_stick_x;

        clockwise *= k;

        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        if (angles.firstAngle < 0) {
            angles.firstAngle += 360;
        }
        angles.firstAngle = (float)(angles.firstAngle * (Math.PI / 180));

        temp = forward * Math.cos(angles.firstAngle) - right * Math.sin(angles.firstAngle);
        right = forward*Math.sin(angles.firstAngle) + right*Math.cos(angles.firstAngle);
        forward = temp;

        frontLeft = forward + clockwise + right;
        frontRight = forward - clockwise - right;
        rearLeft = forward + clockwise - right;
        rearRight = forward - clockwise + right;

        frontLeft = Range.clip(frontLeft, -1, 1);
        frontRight = Range.clip(frontRight, -1, 1);
        rearLeft = Range.clip(rearLeft, -1, 1);
        rearRight = Range.clip(rearRight, -1, 1);

        fl.setPower(frontLeft);
        bl.setPower(rearLeft);
        fr.setPower(frontRight);
        br.setPower(rearRight);

        telemetry.addData( "frontLeft", frontLeft);
        telemetry.addData( "frontRight", frontRight);
        telemetry.addData( "rearLeft", rearLeft);
        telemetry.addData( "rearRight", rearRight);
        telemetry.addData("heading", angles.firstAngle);

        telemetry.update();


    }

    @Override
    public void stop() {

    }

}