package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;
import com.sun.tools.javac.util.Name;

import org.firstinspires.ftc.teamcode.Bot;

/**
 * Created by plotnw on 10/8/17.
 */
@TeleOp(name="Tank Drive Teleop", group="Teleop")
@Disabled
public class TankDriveTeleop extends OpMode {
    Bot robot = new Bot();
    private DcMotor left, right;
    private BNO055IMU imu;

    private double leftPower, rightPower;
    @Override
    public void init() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        // Initialize the drivetrain motors
        right = hardwareMap.get(DcMotor.class, "right");
        left = hardwareMap.get(DcMotor.class, "left");
        right.setDirection(DcMotorSimple.Direction.REVERSE);
        left.setDirection(DcMotorSimple.Direction.REVERSE);

        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    @Override
    public void init_loop() {

    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        leftPower = Range.clip(gamepad1.left_stick_y, -1.0, 1.0);
        rightPower = Range.clip(gamepad1.right_stick_y, -1.0, 1.0);

        left.setPower(leftPower);
        right.setPower(rightPower);
    }

    @Override
    public void stop() {
        left.setPower(0);
        right.setPower(0);
    }
}
