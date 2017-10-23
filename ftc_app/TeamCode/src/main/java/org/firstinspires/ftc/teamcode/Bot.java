package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Enums.BotActions;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by aburur on 8/6/17.
 */

public class Bot
{
    /**
     * Constructor
     * Empty, since initializing the instance fields doesn't occur on object instantiation.
     * That occurs during init phase, with the .init(HardwareMap hwm, Telemetry telem)
     */
    public Bot()
    {

    }

    /**
     * Motor Declarations
     * FR, FL, BR, BL are drive train motors
     */
    DcMotor FR, FL, BR, BL;

    /**
     * Servo Declarations
     */
    Servo leftServo;
    Servo rightServo;

    /**
     * Sensor Declarations
     * BNO055IMU is the builtin gyro on the REV Module
     */
    BNO055IMU imu;
    ModernRoboticsI2cColorSensor leftColorSensor;
    ModernRoboticsI2cColorSensor rightColorSensor;

    /**
     * Variable Declarations
     */

    /**
     * Other Declarations
     * Orientation angles is used for the REv Module's gyro, to store the headings
     */
    private Orientation angles;
    private double temp, forward, right, clockwise, k, frontLeft, frontRight, rearLeft, rearRight;


    /**
     * Initialization Function
     */
    public void init(HardwareMap hardwareMap, Telemetry telemetry, boolean isAuton) {
        this.initialize(hardwareMap, telemetry, isAuton);
    }

    private void initialize(HardwareMap hardwareMap, Telemetry telemetry, boolean isAuton) {

        //BNO055IMU related initialization code
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        //imu.initialize(parameters);

        //Colorsensor init code
        if (isAuton) {
            leftColorSensor = hardwareMap.get(ModernRoboticsI2cColorSensor.class, "leftcs");
            rightColorSensor = hardwareMap.get(ModernRoboticsI2cColorSensor.class, "rightcs");

            //servo init code
            leftServo = hardwareMap.get(Servo.class, "leftservo");
            rightServo = hardwareMap.get(Servo.class, "rightservo");
        }

        //getting the motors from the hardware map
        FL = hardwareMap.get(DcMotor.class, "fl");
        FR = hardwareMap.get(DcMotor.class, "fr");
        BL = hardwareMap.get(DcMotor.class, "bl");
        BR = hardwareMap.get(DcMotor.class, "br");

        //setting runmode
        FL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //setting directions for drive
        FL.setDirection(DcMotorSimple.Direction.FORWARD);
        FR.setDirection(DcMotorSimple.Direction.REVERSE);
        BL.setDirection(DcMotorSimple.Direction.FORWARD);
        BR.setDirection(DcMotorSimple.Direction.REVERSE);

        // TODO: Test different zeropower behaviors (BRAKE, FLOAT, etc)

        FL.setPower(0);
        FR.setPower(0);
        BL.setPower(0);
        BR.setPower(0);
    }

    /**
     * Movement Functions
     */

    public void fieldCentricDrive(double lStickX, double lStickY, double rStickX)
    {
        // Get the controller values
        forward = (-1)*lStickY;
        right =  lStickX;
        clockwise = rStickX;

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

        // Clip power values to within acceptable ranges for the motors
        frontLeft = Range.clip(frontLeft, -1.0, 1.0);
        frontRight = Range.clip(frontRight, -1.0, 1.0);
        rearLeft = Range.clip(rearLeft, -1.0, 1.0);
        rearRight = Range.clip(rearRight, -1.0, 1.0);

        // Send power values to motors
        FL.setPower(frontLeft);
        BL.setPower(rearLeft);
        FR.setPower(frontRight);
        BR.setPower(rearRight);

    }

    /**
     * Action Functions
     */
    public void leftServoDown()
    {

    }

    public void leftServoUp()
    {

    }

    public void rightServoDown()
    {

    }

    public void rightServoUp()
    {

    }

    /**
     * Sensor-Related Functions
     */

    /**
     * Getter Functions
     */

    /**
     * Helper Functions
     */
}
