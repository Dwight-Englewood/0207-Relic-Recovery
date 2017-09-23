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
    private double temp, forward, right, clockwise, k, frontLeft, frontRight, rearLeft, rearRight;
    private BNO055IMU imu;
    private DcMotor FL, FR, BL, BR;
    private Orientation angles;
    private boolean cal;

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
        FL = hardwareMap.get(DcMotor.class, "fl");
        FL.setDirection(DcMotorSimple.Direction.REVERSE);
        FR = hardwareMap.get(DcMotor.class, "fr");
        BL = hardwareMap.get(DcMotor.class, "bl");
        BL.setDirection(DcMotorSimple.Direction.REVERSE);
        BR = hardwareMap.get(DcMotor.class, "br");

        //Initialize the turn modifier
        k = 0;

        //other
        cal = true;
    }

    @Override
    public void init_loop() {

        if (imu.isGyroCalibrated() && cal)
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

        // Clip power values to within acceptaBLe ranges for the motors
        frontLeft = Range.clip(frontLeft, -1.0, 1.0);
        frontRight = Range.clip(frontRight, -1.0, 1.0);
        rearLeft = Range.clip(rearLeft, -1.0, 1.0);
        rearRight = Range.clip(rearRight, -1.0, 1.0);

        // Send power values to motors
        FL.setPower(frontLeft);
        BL.setPower(rearLeft);
        FR.setPower(frontRight);
        BR.setPower(rearRight);


        // Send telemetry data for debugging
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