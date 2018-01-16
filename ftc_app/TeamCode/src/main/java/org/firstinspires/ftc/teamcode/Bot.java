package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Utility.MovementEnum;
import org.firstinspires.ftc.teamcode.Utility.Position;
import org.firstinspires.ftc.teamcode.Utility.ReleasePosition;

/**
 * Created by aburur on 8/6/17.
 */

public class Bot {

    public DcMotor FR, FL, BR, BL, intakeOne, intakeTwo, intakeDrop, lift;
    public Servo jewelServoBottom, flipper, releaseLeft, releaseRight, backIntakeWall, jewelServoTop;
    public CRServo relicArmServo, relicArmVex, relicArmINNOUT;

    public BNO055IMU imu;
    public ModernRoboticsI2cColorSensor colorSensor, intakeColor;
    public OpticalDistanceSensor ods;
    public ModernRoboticsI2cRangeSensor rangeBack, rangeLeft, rangeRight;

    private Orientation angles;
    private double temp, forward, right, clockwise, k, frontLeft, frontRight, rearLeft, rearRight, powerModifier, headingError, driveScale,
            leftPower, rightPower;

    private boolean isStrafing;
    private float heading;

    private ReleasePosition currentPosition = ReleasePosition.DOWN;

    private ModernRoboticsI2cRangeSensor rangeSide;
    private double curSideDistance;
    private double curBackDistance;
    private Position sensorSide;

    //--------------------------------------------------------------------------------------------------------------------------

    public Bot() {}

    public void init(HardwareMap hardwareMap) {
        //BNO055IMU related initialization code
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        imu = hardwareMap.get(BNO055IMU.class, "imu");

        colorSensor = hardwareMap.get(ModernRoboticsI2cColorSensor.class, "cs");
        colorSensor.enableLed(true);
        intakeColor = hardwareMap.get(ModernRoboticsI2cColorSensor.class,"ics");
        intakeColor.enableLed(true);
        ods = hardwareMap.opticalDistanceSensor.get("iods");
        rangeBack = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeb");
        rangeLeft = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangel");
        rangeRight = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "ranger");

        jewelServoBottom = hardwareMap.servo.get("brandon"); //servo which does servo things
        jewelServoTop = hardwareMap.servo.get("hahn"); //another servo which does servo things

        relicArmServo = hardwareMap.crservo.get("relicS");
        relicArmVex = hardwareMap.crservo.get("relicVM");
        relicArmINNOUT = hardwareMap.crservo.get("relicINNOUT");

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

        powerModifier = 0.0075; // 180 * .0055 ~= 1
        k = .6;
    }

    //--------------------------------------------------------------------------------------------------------------------------

    public void tankDrive(double leftStick, double rightStick, double leftTrigger, double rightTrigger, boolean invert, boolean brake) {
        int i = invert ? -1 : 1;

        if (brake) {
            setDriveZeroPowers(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {
            setDriveZeroPowers(DcMotor.ZeroPowerBehavior.FLOAT);
        }

        if (leftTrigger > .3) {
            drive(MovementEnum.LEFTSTRAFE, leftTrigger * i);
            return;
        }
        if (rightTrigger > .3) {
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

    //TODO: DIAGONALS
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

            case STOP:
                FL.setPower(0);
                FR.setPower(0);
                BL.setPower(0);
                BR.setPower(0);
                break;
        }
    }

    public void drive(MovementEnum movement){
        switch (movement) {
            case STOP:
                FL.setPower(0);
                FR.setPower(0);
                BL.setPower(0);
                BR.setPower(0);
                break;
        }
    }

    //TODO: Test different k values
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

        // Do Math
        temp = forward * cos - right * sin;
        right = forward * sin + right * cos;
        forward = temp;

        // Set power values using Math -- could be optimized
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

    //--------------------------------------------------------------------------------------------------------------------------

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

    //--------------------------------------------------------------------------------------------------------------------------

    public void jewelUp() {
        jewelServoBottom.setPosition(.155);
        jewelServoTop.setPosition(.73);
    }

    public void jewelOut() {
        jewelServoBottom.setPosition(.5);
        jewelServoTop.setPosition(.38);
    }

    public void jewelOuter() {
        jewelServoBottom.setPosition(0.86);
    }

    public void jewelKnockback() { jewelServoTop.setPosition(0.24); }

    public void jewelKnockforward() { jewelServoTop.setPosition(.54); }

    //--------------------------------------------------------------------------------------------------------------------------

    private final double relDowner = 0;
    private final double relDown = 0;
    private final double relMid = .50;
    private final double relMidWhileUp = .52;
    private final double relUp = 1;

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

    private void relRInit(){releaseRight.setPosition(.699);}

    private void relLInit(){releaseLeft.setPosition(.699);}

    private void relLDrop(){releaseLeft.setPosition(.73);}

    private void relRDrop(){releaseRight.setPosition(.73);}

    public void flipUp() {
        flipper.setPosition(.55);
    }

    public void flipDown() {
        flipper.setPosition(.05);
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
        if (power == 0) {
            currentPosition = ReleasePosition.MIDDLE;
        } else {
            currentPosition = ReleasePosition.DOWN;
        }
        intakeOne.setPower(power);
        intakeTwo.setPower(power);
    }

    public void backIntakeWallUp() {
        backIntakeWall.setPosition(.78);
    }

    public void backIntakeWallDown() {
        backIntakeWall.setPosition(.5);
    }

    //--------------------------------------------------------------------------------------------------------------------------

    public void runToPosition(int target) {
        this.setDriveMotorModes(DcMotor.RunMode.RUN_TO_POSITION);

        FL.setTargetPosition(target);
        FR.setTargetPosition(target);
        BL.setTargetPosition(target);
        BR.setTargetPosition(target);
    }

    public int distanceToRevs(double distance) {
        final double wheelCirc = 31.9185813;

        final double gearMotorTickThing = .5 * 1120; //neverrest 40 = 1120,

        return (int) (gearMotorTickThing * (distance / wheelCirc));
    }

    public void adjustHeading(int targetHeading, boolean slow) {

        float curHeading = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        if (Math.abs(Math.abs(targetHeading) - Math.abs(curHeading)) <= 1) {
            FL.setPower(0);
            BL.setPower(0);
            FR.setPower(0);
            BR.setPower(0);
        } else {
            headingError = targetHeading + curHeading;
            driveScale = headingError * powerModifier;

            leftPower = 0 + driveScale;
            rightPower = 0 - driveScale;

            leftPower = Range.clip(leftPower, -1, 1);
            rightPower = Range.clip(rightPower, -1, 1);

            if (slow) {
                leftPower = Range.clip(leftPower, -.2, .2);
                rightPower = Range.clip(rightPower, -.2, .2 );
            }

            FL.setPower(leftPower);
            BL.setPower(leftPower);
            FR.setPower(rightPower);
            BR.setPower(rightPower);
        }

    }

    public double slowDownScale(int tickFL, int tickFR, int tickBL, int tickBR, int targetTickFL, int targetTickFR, int targetTickBL, int targetTickBR) {
        double scale = 1;
        if (
                (Math.abs(tickFL - targetTickFL) < 50) &&
                        (Math.abs(tickFR - targetTickFR) < 50) &&
                        (Math.abs(tickBL - targetTickBL) < 50) &&
                        (Math.abs(tickBR - targetTickBR) < 50)
                ) {
            scale = 0;
            //stopped, can be changed
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
                (Math.abs(tickFL - targetTickFL) < 2400) &&
                        (Math.abs(tickFR - targetTickFR) < 2400) &&
                        (Math.abs(tickBL - targetTickBL) < 2400) &&
                        (Math.abs(tickBR - targetTickBR) < 2400)
                ) {
            scale = .5;
        } else {
            scale = .7;
        }
        return scale;
    }

    public double getDistance(Position sensorSide) {
        double distance = 0;
        switch (sensorSide) {
            case BACK:
                distance = this.rangeBack.getDistance(DistanceUnit.CM);
                break;

            case LEFT:
                distance = this.rangeLeft.getDistance(DistanceUnit.CM);
                break;

            case RIGHT:
                distance = this.rangeRight.getDistance(DistanceUnit.CM);
                break;
        }
        return distance;
    }

    public void setupAuton(Position sensorSide) {
        switch (sensorSide) {
            case LEFT:
                rangeSide = rangeLeft;
                break;

            case RIGHT:
                rangeSide = rangeRight;
                break;
        }
        this.sensorSide = sensorSide;

    }

    public void moveToDistance(Position sensorSide, int targetDistance, double power) {
        double curDistance = getDistance(sensorSide);
        if (Math.abs(curDistance - targetDistance) > 3) {
            switch (sensorSide){
                case RIGHT:
                    if (curDistance < targetDistance) {
                        drive(MovementEnum.LEFTSTRAFE, power);
                    } else {
                        drive(MovementEnum.RIGHTSTRAFE, power);
                    } break;

                case LEFT:
                    if (curDistance < targetDistance) {
                        drive(MovementEnum.RIGHTSTRAFE, power);
                    } else {
                        drive(MovementEnum.LEFTSTRAFE, power);
                    } break;

                case BACK:
                    if (curDistance < targetDistance) {
                        drive(MovementEnum.FORWARD, power);
                    } else {
                        drive(MovementEnum.BACKWARD, power);
                    } break;
            }
        } else {
            drive(MovementEnum.STOP);
        }
    }

    private final int leftDistance = 100;
    private final int rightDistance = 100;
    private final int midDistance = 100;
    private final int cryptoDistance = 12;
    private int targetDistance;

    //SETUP AUTON MUST BE CALLED FIRST
    public void lineup(Position column) {
        if (Math.abs(imu.getAngularOrientation().firstAngle) > 2) {
            adjustHeading(0, false);
        } else {
            curSideDistance = rangeSide.getDistance(DistanceUnit.CM);
            curBackDistance = rangeBack.getDistance(DistanceUnit.CM);
            switch (column) {
                case LEFT:
                    this.targetDistance = leftDistance;
                    break;

                case RIGHT:
                    this.targetDistance = rightDistance;
                    break;

                case MIDDLE:
                    this.targetDistance = midDistance;
                    break;
            }

            //TODO: Add power speed up/down
            if (Math.abs(curSideDistance - this.targetDistance) < 3) {
                moveToDistance(Position.BACK, cryptoDistance, .3);
            } else {
                moveToDistance(this.sensorSide, leftDistance, .5);
            }

        }
    }

}