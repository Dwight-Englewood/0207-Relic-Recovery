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
import org.firstinspires.ftc.teamcode.Enums.MovementEnum;

/**
 * Created by aborcrust'em on 8/6/17.
 */

public class Bot
{
    public Bot(HardwareMap hardwareMap) {

        //BNO055IMU related initialization code
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        imu = hardwareMap.get(BNO055IMU.class, "imu");

        colorSensor = hardwareMap.get(ModernRoboticsI2cColorSensor.class, "cs");

        //servo init code
        jewelServo = hardwareMap.get(Servo.class, "servo"); //servo which does servo things
        //armNoSpringyServo       = hardwareMap.get(Servo.class, "anss"); //Servo which prevents arm from springing out aka move this to extend arm
        //armBottomExtendyServo   = hardwareMap.get(Servo.class, "abes"); //Servo which controls the clamp on the arm
        //armTopExtendyServo      = hardwareMap.get(Servo.class, "ates"); //Servo which controls the angle of the hand

        //getting the motors from the hardware map
        FL = hardwareMap.get(DcMotor.class, "fl");
        FR = hardwareMap.get(DcMotor.class, "fr");
        BL = hardwareMap.get(DcMotor.class, "bl");
        BR = hardwareMap.get(DcMotor.class, "br");

        intakeBrake = hardwareMap.get(DcMotor.class, "brake");
        intakeOne = hardwareMap.get(DcMotor.class, "int1");
        intakeTwo = hardwareMap.get(DcMotor.class, "int2");

        releaseRight = hardwareMap.get(Servo.class, "rel r");
        releaseLeft = hardwareMap.get(Servo.class, "rel l");
        flipper = hardwareMap.get(Servo.class, "flip");
    }

    public DcMotor FR, FL, BR, BL, intakeOne, intakeTwo, intakeBrake;

    private Servo jewelServo, flipper, releaseLeft, releaseRight;
    //Servo armNoSpringyServo;
    //Servo armTopExtendyServo;
    //Servo armBottomExtendyServo;

    public BNO055IMU imu;
    public ModernRoboticsI2cColorSensor colorSensor;

    private Orientation angles;
    private double temp, forward, right, clockwise, k, frontLeft, frontRight, rearLeft, rearRight, powerModifier, headingError, driveScale,
            leftPower, rightPower;

    public void init(HardwareMap hardwareMap, Telemetry telemetry) {
        this.initialize(hardwareMap, telemetry);
    }

    private void initialize(HardwareMap hardwareMap, Telemetry telemetry) {

        //setting runmode
        FL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeBrake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeOne.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeTwo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //setting directions for drive
        FL.setDirection(DcMotorSimple.Direction.FORWARD);
        FR.setDirection(DcMotorSimple.Direction.REVERSE);
        BL.setDirection(DcMotorSimple.Direction.FORWARD);
        BR.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeBrake.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeOne.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeTwo.setDirection(DcMotorSimple.Direction.REVERSE);

        // TODO: Test different zeropower behaviors (BRAKE, FLOAT, etc)
        intakeBrake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeOne.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeTwo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        FL.setPower(0);
        FR.setPower(0);
        BL.setPower(0);
        BR.setPower(0);
        intakeBrake.setPower(0);
        intakeOne.setPower(0);
        intakeTwo.setPower(0);

        powerModifier = 0.0055; // 180 * .0055 ~= 1
        k = .4 ;
        //armNoSpringyServo.setPosition(1);
    }

    public void tankDrive(double leftStick, double rightStick, double leftTrigger, double rightTrigger){
        if (leftTrigger > .3) {
            drive(MovementEnum.LEFTSTRAFE, leftTrigger);
            return;
        }
        if (rightTrigger > .3){
            drive(MovementEnum.RIGHTSTRAFE, rightTrigger);
            return;
        }

        FL.setPower(-leftStick);
        BL.setPower(-leftStick);
        FR.setPower(-rightStick);
        BR.setPower(-rightStick);
    }

    //TODO: DIAGONALS
    public void drive(MovementEnum movement, double power) {
       switch (movement){
           case FORWARD:
               FL.setPower(power);
               FR.setPower(power);
               BL.setPower(power);
               BR.setPower(power);
               break;

           case BACKWARD:
               FL.setPower(-power);
               FR.setPower(-power);
               BL.setPower(-power);
               BR.setPower(-power);
               break;

           case LEFTSTRAFE:
               FL.setPower(-power);
               FR.setPower(power);
               BL.setPower(power);
               BR.setPower(-power);
               break;

           case RIGHTSTRAFE:
               FL.setPower(power);
               FR.setPower(-power);
               BL.setPower(-power);
               BR.setPower(power);
               break;

           case LEFTTURN:
               FL.setPower(-power);
               FR.setPower(power);
               BL.setPower(-power);
               BR.setPower(power);
               break;

           case RIGHTTURN:
               FL.setPower(power);
               FR.setPower(-power);
               BL.setPower(power);
               BR.setPower(-power);
               break;

           case STOP:
               FL.setPower(0);
               FR.setPower(0);
               BL.setPower(0);
               BR.setPower(0);
               break;
       }
    }

    //TODO: Test different k values. (.1,.2 are too low)
    public void fieldCentricDrive(double lStickX, double lStickY, double rStickX) {
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

    public void adjustHeading(int targetHeading) {
        headingError = targetHeading + imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        driveScale = headingError * powerModifier;

        leftPower = 0 + driveScale;
        rightPower = 0 - driveScale;

        Range.clip(leftPower, -1, 1);
        Range.clip(rightPower, -1, 1);

        FL.setPower(leftPower);
        BL.setPower(leftPower);
        FR.setPower(rightPower);
        BR.setPower(rightPower);

    }

    /** Releases the arm  public void releaseTheKraken() { armNoSpringyServo.setPosition(.85); } public void releaseTheGiantSquid() { armNoSpringyServo.setPosition(.7); } /** moves the angle of hnad servo to a given position @param position  public void armTopServoPos(double position) { armTopExtendyServo.setPosition(position); } /** clamps or unclamps shit @param position  public void armBotServoPos(double position) { armBottomExtendyServo.setPosition(position); } /** let go of relic */ //TODO: public void ripTHICCBoi() { this.armBottomExtendyServo.setPosition(0); } /** Action Functions */

    public void servoDown() {
        jewelServo.setPosition(0.4);
    }

    public void servoUp() {
        jewelServo.setPosition(1);
    }

    public void intake(double power){
        intakeOne.setPower(power);
        intakeTwo.setPower(power);
    }

    public void setDriveMotorModes(DcMotor.RunMode mode)
    {
        FL.setMode(mode);
        FR.setMode(mode);
        BL.setMode(mode);
        BR.setMode(mode);
    }

    public int distanceToRevs(double distance){
            final double wheelCirc = 31.9185813;

            final double gearMotorTickThing = .5 * 1120; //neverrest 40 = 1120,

            return (int)(gearMotorTickThing * (distance / wheelCirc));
        }
}
