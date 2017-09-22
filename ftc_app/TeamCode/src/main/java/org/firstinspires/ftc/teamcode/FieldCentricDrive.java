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

@TeleOp(name="FieldCentricDrive", group="Example")
//@Disabled
public class FieldCentricDrive extends OpMode
{
    double temp, forward, right, clockwise, k, frontLeft, frontRight, rearLeft, rearRight;
    BNO055IMU imu;
    DcMotor fl, fr, bl, br;
    Orientation angles;
    boolean cal;

    @Override
    public void init() {
        // Initialize the IMU with the correct units and parameters
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        // Initialize the drivetrain motors
        fl = hardwareMap.get(DcMotor.class, "fl");
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        fr = hardwareMap.get(DcMotor.class, "fr");
        bl = hardwareMap.get(DcMotor.class, "bl");
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        br = hardwareMap.get(DcMotor.class, "br");

        //Initialize the turn modifier
        k = 0;

        //other
        cal = true;
    }

    @Override
    public void init_loop() {

        if (!imu.isGyroCalibrated() && cal)
        {
            telemetry.addLine("READY");
            cal = false;
        }
    }

    @Override
    public void start() {
        telemetry.clear();
    }

    @Override
    public void loop() {

        // Get the controller values
        forward = (-1)*gamepad1.left_stick_y;
        right =  gamepad1.left_stick_x;
        clockwise = gamepad1.right_stick_x;

        // Apply the turn modifier k
        clockwise *= k;

        // Turn the output heading value to be based on counterclockwise turns
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        if (angles.firstAngle < 0) {
            angles.firstAngle += 360;
        }

        // Convert to Radians for Math.sin/cos
        angles.firstAngle = (float)(angles.firstAngle * (Math.PI / 180));

        // Do Math
        temp = forward * Math.cos(angles.firstAngle) - right * Math.sin(angles.firstAngle);
        right = forward * Math.sin(angles.firstAngle) + right * Math.cos(angles.firstAngle);
        forward = temp;

        // Set power values using Math
        frontLeft = forward + clockwise + right;
        frontRight = forward - clockwise - right;
        rearLeft = forward + clockwise - right;
        rearRight = forward - clockwise + right;

        //Clip power values to within acceptable ranges for the motors
        frontLeft = Range.clip(frontLeft, -1, 1);
        frontRight = Range.clip(frontRight, -1, 1);
        rearLeft = Range.clip(rearLeft, -1, 1);
        rearRight = Range.clip(rearRight, -1, 1);

        // Send power values to motors
        fl.setPower(frontLeft);
        bl.setPower(rearLeft);
        fr.setPower(frontRight);
        br.setPower(rearRight);


        //Send telemetry data for debugging
        telemetry.addData("frontLeft", frontLeft);
        telemetry.addData("frontRight", frontRight);
        telemetry.addData("rearLeft", rearLeft);
        telemetry.addData("rearRight", rearRight);
        telemetry.addData("heading", angles.firstAngle);

        telemetry.update();
    }

    @Override
    public void stop() {

    }

}