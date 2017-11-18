package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;


import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Enums.ReleasePosition;
import org.firstinspires.ftc.teamcode.Enums.MovementEnum;

/**
 * Created by aborcrust'em on 8/6/17.
 */

public class Bot
{

    public DcMotor FR, FL, BR, BL, intakeOne, intakeTwo, intakeDrop, lift;

    public Servo jewelServo, flipper, releaseLeft, releaseRight;
    //Servo armNoSpringyServo;
    //Servo armTopExtendyServo;
    //Servo armBottomExtendyServo;

    public BNO055IMU imu;
    public ModernRoboticsI2cColorSensor colorSensor;

    private Orientation angles;
    private double temp, forward, right, clockwise, k, frontLeft, frontRight, rearLeft, rearRight, powerModifier, headingError, driveScale,
            leftPower, rightPower;

    public Bot() {
    }

    public void init(HardwareMap hardwareMap) {
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

        intakeDrop = hardwareMap.get(DcMotor.class, "intlift");
        intakeOne = hardwareMap.get(DcMotor.class, "rint");
        intakeTwo = hardwareMap.get(DcMotor.class, "lint");

        lift = hardwareMap.get(DcMotor.class, "lift");

        releaseRight = hardwareMap.get(Servo.class, "rel r");
        releaseLeft = hardwareMap.get(Servo.class, "rel l");
        flipper = hardwareMap.get(Servo.class, "flip");

        //setting runmode
        FL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeDrop.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeOne.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeTwo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //setting directions for drive
        FL.setDirection(DcMotorSimple.Direction.FORWARD);
        FR.setDirection(DcMotorSimple.Direction.REVERSE);
        BL.setDirection(DcMotorSimple.Direction.FORWARD);
        BR.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeDrop.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeOne.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeTwo.setDirection(DcMotorSimple.Direction.REVERSE);
        lift.setDirection(DcMotorSimple.Direction.FORWARD);

        // TODO: Test different zeropower behaviors (BRAKE, FLOAT, etc)
        intakeDrop.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeOne.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeTwo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        FL.setPower(0);
        FR.setPower(0);
        BL.setPower(0);
        BR.setPower(0);
        intakeDrop.setPower(0);
        intakeOne.setPower(0);
        intakeTwo.setPower(0);
        lift.setPower(0);

        powerModifier = 0.0055; // 180 * .0055 ~= 1
        k = .4 ;
        //armNoSpringyServo.setPosition(1);
    }

    public void tankDrive(double leftStick, double rightStick, double leftTrigger, double rightTrigger, boolean invert, boolean brake) {
        int i = invert ?  -1 : 1;

        if (brake){
            drive(MovementEnum.STOP, 0);
            setDriveZeroPowers(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {
            setDriveZeroPowers(DcMotor.ZeroPowerBehavior.FLOAT);
        }

        if (leftTrigger > .3) {
            drive(MovementEnum.LEFTSTRAFE, leftTrigger * i);
            //safeStrafe(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle, MovementEnum.LEFTSTRAFE);
            return;
        }
        if (rightTrigger > .3){
            drive(MovementEnum.RIGHTSTRAFE, rightTrigger * i);
            return;
        }
        
        leftStick *= i;
        rightStick *= i;

        FL.setPower(-leftStick);
        BL.setPower(-leftStick);
        FR.setPower(-rightStick);
        BR.setPower(-rightStick);
    }

    //TODO
    /**public void safeStrafe(double target, MovementEnum direction) {
        double FL_speed = 0;
        double FR_speed = 0;
        double RL_speed = 0;
        double RR_speed = 0;

        currentHeading = imu.;  //Current direction

        FL_speed = power + (currentHeading - target) / 25;  //Calculate speed for each side
        FR_speed = power + (currentHeading - target) / 25;
        RL_speed = power - (currentHeading - target) / 25;  //Calculate speed for each side
        RR_speed = power - (currentHeading - target) / 25;

        FL_speed = Range.clip(FL_speed, -1, 1);
        FR_speed = Range.clip(FR_speed, -1, 1);
        RL_speed = Range.clip(RL_speed, -1, 1);
        RR_speed = Range.clip(RR_speed, -1, 1);

        frontLeft.setPower(FL_speed);
        frontRight.setPower(-FR_speed);
        rearLeft.setPower(-RL_speed);
        rearRight.setPower(RR_speed);
    }*/




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

    /** Releases the arm  public void releaseTheKraken() { armNoSpringyServo.setPosition(.85); } public void releaseTheGiantSquid() { armNoSpringyServo.setPosition(.7); } /** moves the angle of hnad servo to a given position @param position  public void armTopServoPos(double position) { armTopExtendyServo.setPosition(position); } /** clamps or unclamps shit @param position  public void armBotServoPos(double position) { armBottomExtendyServo.setPosition(position); } /** let go of relic */ //TODO: public void ripTHICCBoi() { this.armBottomExtendyServo.setPosition(0); } /** Action Functions */=


    ReleasePosition currentPosition = ReleasePosition.DOWN;

    public void setLift(double power) {}
    public void intake(double power){
        if (power == 0) {
            currentPosition = ReleasePosition.MIDDLE;
        } else {
            currentPosition = ReleasePosition.DOWN;
        }
        intakeOne.setPower(power);
        intakeTwo.setPower(power);
    }

    public void setDriveMotorModes(DcMotor.RunMode mode) {
        FL.setMode(mode);
        FR.setMode(mode);
        BL.setMode(mode);
        BR.setMode(mode);
    }

    public void setDriveZeroPowers(DcMotor.ZeroPowerBehavior behavior){
        FL.setZeroPowerBehavior(behavior);
        FR.setZeroPowerBehavior(behavior);
        BL.setZeroPowerBehavior(behavior);
        BR.setZeroPowerBehavior(behavior);
    }

    /**
     * Flip:
     *  down - .05
     *  up - .55
     *  out - .75
     *
     * R release:
     *  down - .98
     *  up - .52
     *  out - .02
     *
     * L release:
     *  down - .02
     *  up - .48
     *  out - .98
     *
     * Jewel:
     *  down - 1
     *  up - .65
     *  out - .1
     */
    public void jewelUp(){jewelServo.setPosition(.65);}
    public void jewelDown(){jewelServo.setPosition(.05);}
    public void jewelOut(){jewelServo.setPosition(.1);}


    double relDown = .53;
    double relMid = .59;
    double relMidWhileUp = .63;
    double relUp = 1;

    public void relLUp() {
        releaseLeft.setPosition(1 - relUp);
    }
    public void relLDown() {
        releaseLeft.setPosition(1 - relDown);
    }
    public void relLMid() {
        releaseLeft.setPosition(1 - relMid);
    }

    public void relLMidWhileUp() {
        releaseLeft.setPosition(1 - relMidWhileUp);
    }

    public void relRUp() {
        releaseRight.setPosition(relUp);
    }
    public void relRDown() {
        releaseRight.setPosition(relDown);
    }
    public void relRMid() {
        releaseRight.setPosition(relMid);
    }
    public void relRMidWhileUp() {
        releaseRight.setPosition(relMidWhileUp);
    }

    public void flipUp(){flipper.setPosition(.55);}
    public void flipDown(){flipper.setPosition(.05);}
    public void flipOut(){flipper.setPosition(.75);}

    public void releaseMove(ReleasePosition position) {
        switch (position) {
            case DOWN:
                relRDown();
                relLDown();
                break;
            case MIDDLE:
                relRMid();
                relLMid();
                break;
            case MIDDLEUP:
                relRMidWhileUp();
                relLMidWhileUp();
                break;
            case UP:
                relRUp();
                relLUp();
                break;
        }
    }

    public void unfoldBot() {
        this.rollOut();
    }

    private void nop() {
        ;
    }

    private void rollOut() {
        this.releaseMove(ReleasePosition.UP);

        this.intakeDrop.setPower(-1);
        ElapsedTime kms = new ElapsedTime();
        kms.reset();
        while (kms.milliseconds() < 500) {
            this.nop();
        }
        this.jewelOut();
        this.releaseMove(ReleasePosition.DOWN);
    }

    public int distanceToRevs(double distance){
        final double wheelCirc = 31.9185813;

        final double gearMotorTickThing = .5 * 1120; //neverrest 40 = 1120,

        return (int)(gearMotorTickThing * (distance / wheelCirc));
    }
}
