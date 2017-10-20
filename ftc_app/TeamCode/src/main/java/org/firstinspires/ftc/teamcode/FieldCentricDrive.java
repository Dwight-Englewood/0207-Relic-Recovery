package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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
//TODO: Test different braking modes & modify the k parameter for turning
@TeleOp(name="FieldCentricDrive", group="Example")
@Disabled
public class FieldCentricDrive extends OpMode
{
    private double temp, forward, right, clockwise, k, frontLeft, frontRight, rearLeft, rearRight;
    private Orientation angles;
    private Bot robot = new Bot();


    @Override
    public void init() {
        robot.init(hardwareMap, telemetry);
        //Initialize the turn modifier
        k = .75;
    }

    @Override
    public void init_loop() {

        //Commented out to test the Gyro calibration OpMode
        /*if (imu.isGyroCalibrated() && cal)
        {
            telemetry.addLine("READY");
            cal = false;
        }*/
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
        angles = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
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

        // Clip power values to within acceptable ranges for the motors
        frontLeft = Range.clip(frontLeft, -1.0, 1.0);
        frontRight = Range.clip(frontRight, -1.0, 1.0);
        rearLeft = Range.clip(rearLeft, -1.0, 1.0);
        rearRight = Range.clip(rearRight, -1.0, 1.0);

        // Send power values to motors
        robot.FL.setPower(frontLeft);
        robot.BL.setPower(rearLeft);
        robot.FR.setPower(frontRight);
        robot.BR.setPower(rearRight);


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