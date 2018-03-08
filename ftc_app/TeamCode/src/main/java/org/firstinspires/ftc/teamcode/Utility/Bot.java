package org.firstinspires.ftc.teamcode.Utility;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Utility.InfiniteImprobabilityDrive.Glyph;

import static org.firstinspires.ftc.teamcode.Utility.InfiniteImprobabilityDrive.Glyph.BROWN;
import static org.firstinspires.ftc.teamcode.Utility.InfiniteImprobabilityDrive.Glyph.EMPTY;
import static org.firstinspires.ftc.teamcode.Utility.InfiniteImprobabilityDrive.Glyph.GRAY;

/**
 * Created by aburur on 8/6/17.
 */

public class Bot {

    private final double relDowner = 0;
    private final double relDown = 0;
    private final double relMid = .50;
    private final double relMidWhileUp = .52;
    private final double relUp = 1;

    //--------------------------------------------------------------------------------------------------------------------------

    public DcMotor FR, FL, BR, BL, intakeOne, intakeTwo, intakeDrop, lift;
    public Servo jewelServoBottom, flipper, releaseLeft, releaseRight, backIntakeWall, jewelServoTop;
    public Servo relicArmServo1, relicArmServo2;
    public CRServo relicArmVex2, relicArmVex1;
    public BNO055IMU imu;

    //--------------------------------------------------------------------------------------------------------------------------

    public ColorSensor intakeColorBottom, intakeColorSide;
    public DistanceSensor intakeDistanceBottom, intakeDistanceSide;
    public ModernRoboticsI2cColorSensor jewelColorBack, jewelColorForward;
    public ModernRoboticsI2cRangeSensor rangeBack, rangeLeft, rangeRight;
    private Orientation angles;

    //--------------------------------------------------------------------------------------------------------------------------
    private double temp, forward, right, clockwise, k, frontLeft, frontRight, rearLeft, rearRight, powerModifier, headingError, driveScale,
            leftPower, rightPower;
    private boolean isStrafing;
    private float heading;

    public Bot() {
    }

    public void init(HardwareMap hardwareMap) {
        imu = hardwareMap.get(BNO055IMU.class, "imu");

        //Relic Arm Initialization
        relicArmServo1 = hardwareMap.get(Servo.class, "ras1");
        relicArmServo2 = hardwareMap.get(Servo.class, "ras2");
        relicArmServo1.scaleRange(.2, .8);
        relicArmServo2.scaleRange(.2, .8);

        relicArmVex1 = hardwareMap.get(CRServo.class, "rav1");
        relicArmVex2 = hardwareMap.get(CRServo.class, "rav2");
        relicArmVex2.setDirection(CRServo.Direction.FORWARD);
        relicArmVex1.setDirection(CRServo.Direction.FORWARD);

        jewelColorBack = hardwareMap.get(ModernRoboticsI2cColorSensor.class, "cs");
        jewelColorBack.enableLed(true);
        jewelColorForward = hardwareMap.get(ModernRoboticsI2cColorSensor.class, "cs2");
        jewelColorForward.enableLed(true);

        intakeDistanceBottom = hardwareMap.get(DistanceSensor.class, "icsb");
        intakeDistanceSide = hardwareMap.get(DistanceSensor.class, "icss");

        intakeColorBottom = hardwareMap.get(ColorSensor.class, "icsb");
        intakeColorSide = hardwareMap.get(ColorSensor.class, "icss");


        rangeBack = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeb");
        rangeLeft = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangel");
        rangeRight = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "ranger");

        jewelServoBottom = hardwareMap.servo.get("jewelbot"); //servo which does servo things\
        jewelServoTop = hardwareMap.servo.get("jeweltop"); //another servo which does servo things

        FL = hardwareMap.dcMotor.get("fl");
        FR = hardwareMap.dcMotor.get("fr");
        BL = hardwareMap.dcMotor.get("bl");
        BR = hardwareMap.dcMotor.get("br");

        intakeDrop = hardwareMap.dcMotor.get("intlift");
        intakeOne = hardwareMap.dcMotor.get("rint");
        intakeTwo = hardwareMap.dcMotor.get("lint");

        lift = hardwareMap.dcMotor.get("lift");

        releaseRight = hardwareMap.servo.get("rel r");
        releaseLeft = hardwareMap.servo.get("rel l");

        releaseRight.scaleRange(.2, .8);
        releaseLeft.scaleRange(.2, .8);

        flipper = hardwareMap.servo.get("flip");
        flipper.scaleRange(.2, .8);

        backIntakeWall = hardwareMap.servo.get("backiw");

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
        intakeOne.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intakeTwo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        FL.setPower(0);
        FR.setPower(0);
        BL.setPower(0);
        BR.setPower(0);
        intakeDrop.setPower(0);
        intakeOne.setPower(0);
        intakeTwo.setPower(0);
        lift.setPower(0);

        powerModifier = 0.02; // 180 * .0055 ~= 1
        k = .5;
    }

    public void relicArmVexControl(double power, DcMotorSimple.Direction d) {
        power = Range.clip(power, 0, 1);

        relicArmVex2.setDirection(d);
        relicArmVex1.setDirection(d);
        relicArmVex1.setPower(power);
        relicArmVex2.setPower(power);
    }

    public void tankDrive(double leftStick, double rightStick, double leftTrigger, double rightTrigger, boolean invert, boolean brake) {

        if (brake) {
            setDriveZeroPowers(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {
            setDriveZeroPowers(DcMotor.ZeroPowerBehavior.FLOAT);
        }

        if (leftTrigger > .1) {
            MovementEnum d = !invert ? MovementEnum.LEFTSTRAFE : MovementEnum.RIGHTSTRAFE;
            drive(d, leftTrigger);
            return;
        }
        if (rightTrigger > .1) {
            MovementEnum d = !invert ? MovementEnum.RIGHTSTRAFE : MovementEnum.LEFTSTRAFE;
            drive(d, rightTrigger);
            return;
        }


        leftStick *= -1;
        rightStick *= -1;

        leftPower = Range.clip((!invert ? leftStick : rightStick * -1) * (brake ? .4 : 1), -1, 1);
        rightPower = Range.clip((!invert ? rightStick : leftStick * -1) * (brake ? .4 : 1), -1, 1);

        FL.setPower(leftPower);
        BL.setPower(leftPower);
        FR.setPower(rightPower);
        BR.setPower(rightPower);
    }

    public Glyph findGlyphType() {
        /*
        int red, blue, green, alpha;
        red = this.intakeColorBottom.red();
        blue = this.intakeColorBottom.blue();
        green = this.intakeColorBottom.green();
        alpha = this.intakeColorBottom.alpha();

        double average;
        average = (red + blue + green + alpha) / (double ) 4;

        if (average > 3) {
            return Glyph.GRAY;
        } else if (average < 2.5 && average > 1) {
            return Glyph.BROWN;
        } else {
            return Glyph.EMPTY;
        }
        */
        if (this.intakeDistanceSide.getDistance(DistanceUnit.CM) < 7) {
            if (this.intakeColorSide.alpha() > 135) {
                return GRAY;
            } else {
                return BROWN;
            }
        } else {
            return EMPTY;
        }
        /*
        double distanceCM = this.intakeDistanceBottom.getDistance(DistanceUnit.CM);
        if (Double.isNaN(distanceCM)) {
            return Glyph.EMPTY;
            //only happens with open air - and thus no glyph is present
        }

        if (intakeDistanceBottom.getDistance(DistanceUnit.CM) < 6) {
            //for this distance the distance censor is unreliable - however, the color comparisons are more limited so we can use that
        }


        double compAlphaVal = -10 * distanceCM + 150;

        if (compAlphaVal < 0) {
            return EMPTY;
        }

        if (this.intakeColorBottom.alpha() > compAlphaVal) {
            return GRAY;
        } else {
            return BROWN;
        }*/

        //needs some actualy testing on the robot
    }

    public void tankDriveSafeStrafe(double leftStick, double rightStick, double leftTrigger, double rightTrigger, boolean invert, boolean brake, Telemetry telemetry) {
        int i = invert ? -1 : 1;

        if (leftTrigger < .3 && rightTrigger < .3) {
            isStrafing = false;
        }

        if (brake) {
            drive(MovementEnum.STOP, 0);
            setDriveZeroPowers(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {
            setDriveZeroPowers(DcMotor.ZeroPowerBehavior.FLOAT);
        }

        if (leftTrigger > .3) {
            if (isStrafing) {
            } else {
                heading = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
                isStrafing = true;
            }
            safeStrafe(heading, false, telemetry);
            //safeStrafe(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle, MovementEnum.LEFTSTRAFE);
            return;
        }

        if (rightTrigger > .3) {
            if (isStrafing) {
            } else {
                heading = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
                isStrafing = true;
            }
            safeStrafe(heading, true, telemetry);

        }

        leftStick *= i;
        rightStick *= i;
        if (!isStrafing) {
            FL.setPower(-leftStick);
            BL.setPower(-leftStick);
            FR.setPower(-rightStick);
            BR.setPower(-rightStick);
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------

    public void drive(MovementEnum movement, double power) {
        switch (movement) {
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

            case RIGHTDOWN:
                FL.setPower(-power);
                FR.setPower(0);
                BL.setPower(0);
                BR.setPower(-power);
                break;

            case RIGHTUP:
                FL.setPower(power);
                FR.setPower(0);
                BL.setPower(0);
                BR.setPower(power);
                break;

            case LEFTDOWN:
                FL.setPower(0);
                FR.setPower(-power);
                BL.setPower(-power);
                BR.setPower(0);
                break;

            case LEFTUP:
                FL.setPower(0);
                FR.setPower(power);
                BL.setPower(power);
                BR.setPower(0);
                break;

            case STOP:
                FL.setPower(0);
                FR.setPower(0);
                BL.setPower(0);
                BR.setPower(0);
                break;
        }
    }

    public void drive(MovementEnum movement) {
        switch (movement) {
            case STOP:
                FL.setPower(0);
                FR.setPower(0);
                BL.setPower(0);
                BR.setPower(0);
                break;

            default:
                drive(movement, .5);
                break;
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------

    public void fieldCentricDrive(double lStickX, double lStickY, double rStickX, double leftTrigger, double rightTrigger, boolean brake) {

        if (brake) {
            setDriveZeroPowers(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {
            setDriveZeroPowers(DcMotor.ZeroPowerBehavior.FLOAT);
        }

        if (leftTrigger > .3) {
            drive(MovementEnum.LEFTSTRAFE, leftTrigger);
            return;
        }
        if (rightTrigger > .3) {
            drive(MovementEnum.RIGHTSTRAFE, rightTrigger);
            return;
        }

        // Get the controller values
        forward = (-1) * lStickY;
        right = lStickX;
        clockwise = rStickX;

        // Apply the turn modifier k
        clockwise *= k;

        // Turn the output heading value to be based on counterclockwise turns
        angles = imu.getAngularOrientation();
        if (angles.firstAngle < 0) {
            angles.firstAngle += 360;
        }

        // Convert to Radians for Math.sin/cos
        double orient = Math.toRadians(angles.firstAngle);
        double sin = Math.sin(orient);
        double cos = Math.cos(orient);

        // Apply the rotation matrix
        temp = forward * cos - right * sin;
        right = forward * sin + right * cos;
        forward = temp;

        // Set power values
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

    public void safeStrafe(float targetHeading, boolean isRight, Telemetry telemetry, double powerCenter) {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        headingError = targetHeading - angles.firstAngle;
        driveScale = headingError * powerModifier;

        leftPower = powerCenter - driveScale;
        rightPower = powerCenter + driveScale;

        if (leftPower > 1)
            leftPower = 1;
        else if (leftPower < 0)
            leftPower = 0;

        if (rightPower > 1)
            rightPower = 1;
        else if (rightPower < 0)
            rightPower = 0;


        if (isRight) {
            FL.setPower(leftPower);
            FR.setPower(-rightPower);
            BL.setPower(-leftPower);
            BR.setPower(rightPower);
        } else {
            FL.setPower(-leftPower);
            FR.setPower(rightPower);
            BL.setPower(leftPower);
            BR.setPower(-rightPower);
        }
        telemetry.addData("leftPower", leftPower);
        telemetry.addData("rightPower", rightPower);
    }

    public void safeStrafe(float targetHeading, boolean isRight, Telemetry telemetry) {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        headingError = targetHeading - angles.firstAngle;
        driveScale = headingError * powerModifier;

        leftPower = .85 - driveScale;
        rightPower = .85 + driveScale;

        if (leftPower > 1)
            leftPower = 1;
        else if (leftPower < 0)
            leftPower = 0;

        if (rightPower > 1)
            rightPower = 1;
        else if (rightPower < 0)
            rightPower = 0;


        if (isRight) {
            FL.setPower(leftPower);
            FR.setPower(-rightPower);
            BL.setPower(-leftPower);
            BR.setPower(rightPower);
        } else {
            FL.setPower(-leftPower);
            FR.setPower(rightPower);
            BL.setPower(leftPower);
            BR.setPower(-rightPower);
        }
        telemetry.addData("leftPower", leftPower);
        telemetry.addData("rightPower", rightPower);
    }

    public void setDriveMotorModes(DcMotor.RunMode mode) {
        FL.setMode(mode);
        FR.setMode(mode);
        BL.setMode(mode);
        BR.setMode(mode);
    }

    public void setDriveZeroPowers(DcMotor.ZeroPowerBehavior behavior) {
        FL.setZeroPowerBehavior(behavior);
        FR.setZeroPowerBehavior(behavior);
        BL.setZeroPowerBehavior(behavior);
        BR.setZeroPowerBehavior(behavior);
    }

    public void jewelUp() {
        jewelServoBottom.setPosition(.63);
        jewelServoTop.setPosition(0);
    }

    //--------------------------------------------------------------------------------------------------------------------------

    public void jewelOut() {
        jewelServoBottom.setPosition(.36);
        jewelServoTop.setPosition(.4);
    }

    public void jewelOuterBlue() {
        jewelServoBottom.setPosition(.1);
        jewelServoTop.setPosition(.43);
    }

    public void jewelOuterRed() {
        jewelServoBottom.setPosition(.1);
        jewelServoTop.setPosition(.37);
    }

    public void jewelTeleop(){
        jewelServoBottom.setPosition(.19);
        jewelServoTop.setPosition(.4);
    }

    public void jewelKnockback() {
        jewelServoTop.setPosition(0.58);
    }

    public void jewelKnockforward() {
        jewelServoTop.setPosition(.26);
    }

    private void relLUp() {
        releaseLeft.setPosition(relUp);
    }

    private void relLDown() {
        releaseLeft.setPosition(relDown);
    }

    private void relLMid() {
        releaseLeft.setPosition(relMid);
    }

    private void relLMidWhileUp() {
        releaseLeft.setPosition(relMidWhileUp);
    }

    private void relLDowner() {
        releaseLeft.setPosition(relDowner);
    }

    private void relRUp() {
        releaseRight.setPosition(relUp);
    }

    private void relRDown() {
        releaseRight.setPosition(relDown);
    }

    private void relRMid() {
        releaseRight.setPosition(relMid);
    }

    private void relRMidWhileUp() {
        releaseRight.setPosition(relMidWhileUp);
    }

    private void relRDowner() {
        releaseRight.setPosition(relDowner);
    }

    private void relRInit() {
        releaseRight.setPosition(.72);
    }

    private void relLInit() {
        releaseLeft.setPosition(.72);
    }

    private void relLDrop() {
        releaseLeft.setPosition(.77);
    }

    private void relRDrop() {
        releaseRight.setPosition(.77);
    }

    public void flipUp() {
        flipper.setPosition(1);
    }

    public void flipDown() {
        flipper.setPosition(0);
    }

    public void releaseMove(ReleasePosition position) {
        switch (position) {
            case DOWNER:
                relRDowner();
                relLDowner();
                break;
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
            case INIT:
                relRInit();
                relLInit();
                break;
            case DROP:
                relRDrop();
                relLDrop();
                break;
        }
    }

    public void intake(double power) {
        intakeOne.setPower(power);
        intakeTwo.setPower(power);
    }

    public void backIntakeWallUp() {
        backIntakeWall.setPosition(1);
    }

    public void backIntakeWallDown() {
        backIntakeWall.setPosition(0);
    }

    //--------------------------------------------------------------------------------------------------------------------------

    public void runToPosition(int target) {
        this.setDriveMotorModes(DcMotor.RunMode.RUN_TO_POSITION);

        FL.setTargetPosition(target);
        FR.setTargetPosition(target);
        BL.setTargetPosition(target);
        BR.setTargetPosition(target);
    }

    public void runRightToPosition(int target) {
        this.setDriveMotorModes(DcMotor.RunMode.RUN_TO_POSITION);

        FL.setTargetPosition(target);
        FR.setTargetPosition(-target);
        BL.setTargetPosition(-target);
        BR.setTargetPosition(target);
    }

    public void runLeftToPosition(int target) {
        this.setDriveMotorModes(DcMotor.RunMode.RUN_TO_POSITION);

        FL.setTargetPosition(-target);
        FR.setTargetPosition(target);
        BL.setTargetPosition(target);
        BR.setTargetPosition(-target);
    }

    public int distanceToRevsNR40(double distance) {
        final double wheelCirc = 31.9185813;
        final double gearMotorTickThing = .5 * 1120; //neverrest 40 = 1120 counts per revolution

        return (int) (gearMotorTickThing * (distance / wheelCirc));
    }

    public int distanceToRevsNRO20(double distance) {
        final double wheelCirc = 31.9185813;
        final double gearMotorTickThing = .5 * 537.6; //neverrest orbital 20 = 537.6 counts per revolution

        return (int) (gearMotorTickThing * (distance / wheelCirc));
    }

    public void adjustHeading(int targetHeading, boolean slow) {
        //Initialize the turnleft boolean.
        boolean turnLeft = false;

        //Get the current heading from the imu.
        float curHeading = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;

        //If within a reasonable degree of error of the target heading, set power to zero on all motors.
        if (Math.abs(curHeading - targetHeading) <= .5) {
            drive(MovementEnum.STOP);
            return;
        }
        //Generate our proportional term
        float powFactor = Math.abs(targetHeading - curHeading) * (float) (slow ? .0055 : .02);

        //Choose how to turn based on given target
        switch (targetHeading) {
            case 0:
                turnLeft = curHeading <= 0;
                break;

            case 90:
                turnLeft = !(curHeading <= -90 || curHeading >= 90);
                break;

            case 180:
                turnLeft = !(curHeading <= 0);
                break;

            case -90:
                turnLeft = curHeading <= -90 || curHeading >= 90;
                break;

            case 45:
                turnLeft = !(curHeading <= -90 || curHeading >= 90);
                break;

        }

        //Clip the powers to within an acceptable range for the motors and apply the proportional factor.
        leftPower = Range.clip((turnLeft ? -1 : 1) * powFactor, -1, 1);
        rightPower = Range.clip((turnLeft ? 1 : -1) * powFactor, -1, 1);

        //Set power to all motors
        FL.setPower(leftPower);
        BL.setPower(leftPower);
        FR.setPower(rightPower);
        BR.setPower(rightPower);

    }

    public double slowDownScale(int tickFL, int tickFR, int tickBL, int tickBR, int targetTickFL, int targetTickFR, int targetTickBL, int targetTickBR) {
        double scale;
        if (
                (Math.abs(tickFL - targetTickFL) < 25) &&
                        (Math.abs(tickFR - targetTickFR) < 25) &&
                        (Math.abs(tickBL - targetTickBL) < 25) &&
                        (Math.abs(tickBR - targetTickBR) < 25)
                ) {
            scale = 0;
        } else if (
                (Math.abs(tickFL) < 200) &&
                        (Math.abs(tickFR) < 200) &&
                        (Math.abs(tickBL) < 200) &&
                        (Math.abs(tickBR) < 200)
                ) {
            scale = .1;
        } else if (
                (Math.abs(tickFL) < 1000) &&
                        (Math.abs(tickFR) < 1000) &&
                        (Math.abs(tickBL) < 1000) &&
                        (Math.abs(tickBR) < 1000)
                ) {
            scale = .3;
        } else if (
                (Math.abs(tickFL - targetTickFL) < 500) &&
                        (Math.abs(tickFR - targetTickFR) < 500) &&
                        (Math.abs(tickBL - targetTickBL) < 500) &&
                        (Math.abs(tickBR - targetTickBR) < 500)
                ) {
            scale = .1;
        } else if (
                (Math.abs(tickFL - targetTickFL) < 2000) &&
                        (Math.abs(tickFR - targetTickFR) < 2000) &&
                        (Math.abs(tickBL - targetTickBL) < 2000) &&
                        (Math.abs(tickBR - targetTickBR) < 2000)
                ) {
            scale = .3;
        } else if (
                (Math.abs(tickFL - targetTickFL) < 2500) &&
                        (Math.abs(tickFR - targetTickFR) < 2500) &&
                        (Math.abs(tickBL - targetTickBL) < 2500) &&
                        (Math.abs(tickBR - targetTickBR) < 2500)
                ) {
            scale = .5;
        } else if (
                (Math.abs(tickFL - targetTickFL) < 3500) &&
                        (Math.abs(tickFR - targetTickFR) < 3500) &&
                        (Math.abs(tickBL - targetTickBL) < 3500) &&
                        (Math.abs(tickBR - targetTickBR) < 3500)
                ) {
            scale = .7;
        } else {
            scale = 1;
        }
        return scale;
    }

    //NOTE: if cur distance < target, power is positive --> cur distance > target = power negative
    public double slowDownScale(int targetDistance, double startingDistance, ModernRoboticsI2cRangeSensor rangeSensor) {
        double curDistance = rangeSensor.getDistance(DistanceUnit.CM);
        int modifier = targetDistance < curDistance ? 1 : -1;
        double scale;

        if (Math.abs(targetDistance - curDistance) <= 2) {
            scale = 0;
        } else if (Math.abs(curDistance - startingDistance) <= 5) {
            scale = .1;
        } else if (Math.abs(curDistance - startingDistance) <= 10) {
            scale = .3;
        } else if (Math.abs(targetDistance - curDistance) <= 5) {
            scale = .1;
        } else if (Math.abs(targetDistance - curDistance) <= 10) {
            scale = .3;
        } else if (Math.abs(targetDistance - curDistance) <= 20) {
            scale = .5;
        } else if (Math.abs(targetDistance - curDistance) <= 35) {
            scale = .7;
        } else {
            scale = 1;
        }

        return scale * modifier;

    }

    public double getDistance(Position sensorSide) {
        double distance = 0;
        switch (sensorSide) {
            case MIDDLE:
                distance = this.rangeBack.getDistance(DistanceUnit.CM);
                break;

            case LEFT:
                distance = this.rangeLeft.getDistance(DistanceUnit.CM);
                break;

            case RIGHT:
                distance = this.rangeRight.getDistance(DistanceUnit.CM);
                break;

            default:
                distance = Double.NaN;
                break;
        }
        return distance;
    }

    public void strafeAdjusts(int targetHeading, MovementEnum direction) {
        double headingError = targetHeading + imu.getAngularOrientation().firstAngle;
        if (Math.abs(headingError) < 1) {
            FL.setPower(FR.getPower());
            FR.setPower(FR.getPower());
            BL.setPower(FR.getPower());
            BR.setPower(FR.getPower());
            return;
        }
        double driveScale = Math.abs(headingError) * .0055;
        double powbl, powbr, minbr, minbl;

        minbl = direction == MovementEnum.LEFTSTRAFE ? 0 : -1;
        minbr = direction == MovementEnum.LEFTSTRAFE ? -1 : 0;

        if (headingError < 0) {
            powbl = Range.clip(BL.getPower() + driveScale, minbl, minbl + 1);
            powbr = Range.clip(BR.getPower() - driveScale, minbr, minbr + 1);
        } else {
            powbl = Range.clip(BL.getPower() + driveScale, minbl, minbl + 1);
            powbr = Range.clip(BR.getPower() - driveScale, minbr, minbr + 1);
        }

        BL.setPower(powbl);
        BR.setPower(powbr);
    }

}