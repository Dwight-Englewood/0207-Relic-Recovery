package org.firstinspires.ftc.teamcode.Autonomous;
/**
 * Created by aburur on 2/27/18.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Utility.Bot;
import org.firstinspires.ftc.teamcode.Utility.GlyphClamps;
import org.firstinspires.ftc.teamcode.Utility.MovementEnum;
import org.firstinspires.ftc.teamcode.Utility.ReleasePosition;
import org.firstinspires.ftc.teamcode.Vision.ClosableVuforiaLocalizer;


@Autonomous(name = "RedFarWorldsV2", group = "Auton")
public class RedFarWorldsV2 extends OpMode {
    Bot robot = new Bot();
    ElapsedTime timer = new ElapsedTime();
    ElapsedTime relicTimer = new ElapsedTime();
    ElapsedTime globalTime = new ElapsedTime();

    private ClosableVuforiaLocalizer vuforia;
    private VuforiaTrackables relicTrackables;
    private VuforiaTrackable relicTemplate;
    private RelicRecoveryVuMark vuMark;

    private double power = 0, curDistance;
    private int generalTarget = 0, counter = 0, targetHeading;
    private boolean hitjewel = false;
    private int command = -1;
    private String commandString = "";

    ReleasePosition lastPosition = ReleasePosition.MIDDLE;

    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.releaseMove(ReleasePosition.INIT);
        robot.jewelUp();

        robot.backIntakeWallUp();
        robot.setDriveZeroPowers(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.relicArmServo1.setPosition(1);
        robot.relicArmServo2.setPosition(1);

        robot.glyphClamps.clampBack(GlyphClamps.ClampPos.CLAMPED);
        robot.glyphClamps.clampFront(GlyphClamps.ClampPos.CLAMPED);

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = "AbZUuPf/////AAAAGUmS0Chan00iu7rnRhzu63+JgDtPo889M6dNtjvv+WKxiMJ8w2DgSJdM2/zEI+a759I7DlPj++D2Ryr5sEHAg4k1bGKdo3BKtkSeh8hCy78w0SIwoOACschF/ImuyP/V259ytjiFtEF6TX4teE8zYpQZiVkCQy0CmHI9Ymoa7NEvFEqfb3S4P6SicguAtQ2NSLJUX+Fdn49SEJKvpSyhwyjbrinJbak7GWqBHcp7fGh7TNFcfPFMacXg28XxlvVpQaVNgkvuqolN7wkTiR9ZMg6Fnm0zN4Xjr5lRtDHeE51Y0bZoBUbyLWSA+ts3SyDjDPPUU7GMI+Ed/ifb0csVpM12aOiNr8d+HsfF2Frnzrj2";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = new ClosableVuforiaLocalizer(parameters);

        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTrackables.activate();

        telemetry.addLine("Ready.");
        telemetry.update();
    }

    @Override
    public void init_loop() {
        vuMark = RelicRecoveryVuMark.from(relicTemplate);
        telemetry.addData("VuMark: ", vuMark);
    }

    @Override
    public void start() {
        timer.reset();
        robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        relicTimer.reset();
        robot.relicArmVexControl(.8, DcMotorSimple.Direction.REVERSE);
        robot.glyphClamps.clampBack(GlyphClamps.ClampPos.CLAMPED);
        robot.glyphClamps.clampFront(GlyphClamps.ClampPos.CLAMPED);
        globalTime.reset();
    }

    @Override
    public void loop() {
        if (relicTimer.milliseconds() > 1000) {
            robot.relicArmVexControl(0, DcMotorSimple.Direction.FORWARD);
        }

        switch (command) {
            case -1:
                commandString = "Find VuMark";
                vuMark = RelicRecoveryVuMark.from(relicTemplate);
                if (timer.milliseconds() > 250) {
                    robot.jewelOuterRed();
                    if (vuMark != RelicRecoveryVuMark.UNKNOWN || timer.milliseconds() > 1500) {
                        timer.reset();
                        command++;
                    }
                }
                break;

            case 0:
                commandString = "Deactivate Vuforia";
                relicTrackables.deactivate();
                vuforia.close();
                timer.reset();
                command++;
                break;

            case 1:
                commandString = "Hit Jewel";
                if (hitjewel && timer.milliseconds() > 300) {
                    robot.jewelUpTeleop();
                    timer.reset();
                    command++;
                } else if (timer.milliseconds() > 1500) {
                    robot.drive(MovementEnum.STOP);
                    robot.jewelKnockforward();
                    try {Thread.sleep(300);}catch(Exception e){}
                    robot.jewelKnockback();
                    try {Thread.sleep(300);}catch(Exception e){}
                    robot.jewelUpTeleop();
                    timer.reset();
                    command++;
                } else if ((robot.jewelColorForward.red() >= 2 || robot.jewelColorBack.blue() >= 2) && !hitjewel) {
                    hitjewel = true;
                    robot.jewelKnockback();
                    timer.reset();
                } else if ((robot.jewelColorBack.red() >= 2 || robot.jewelColorForward.blue() >= 2) && !hitjewel) {
                    hitjewel = true;
                    robot.jewelKnockforward();
                    timer.reset();
                }
                break;

            case 2:
                commandString = "Set up RUN_TO_POSITION";
                generalTarget = robot.distanceToRevsNRO20(55);
                robot.runToPosition(generalTarget);
                timer.reset();
                command++;
                break;

            case 3:
                commandString = "RUN_TO_POSITION";
                power = robot.slowDownScale(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.FORWARD, power);
                if (power == 0) {
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
                    timer.reset();
                    command++;
                }
                break;

            case 4:
                switch (vuMark) {
                    case RIGHT:
                        targetHeading = 96;
                        generalTarget = -1 * robot.distanceToRevsNRO20(46);
                        break;

                    case CENTER:
                        targetHeading = 109;
                        generalTarget = -1 * robot.distanceToRevsNRO20(50);
                        break;

                    case LEFT:
                        targetHeading = 119;
                        generalTarget = -1 * robot.distanceToRevsNRO20(58);
                        break;

                    case UNKNOWN:
                        targetHeading = 96;
                        generalTarget = -1 * robot.distanceToRevsNRO20(50);
                        break;
                }
                break;

            case 5:
                commandString = "Adjust heading to target";
                if (timer.milliseconds() >= 2000) {
                    robot.drive(MovementEnum.STOP);
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    try{Thread.sleep(300);} catch(Exception e) {}
                    robot.runToPosition(generalTarget);
                    timer.reset();
                    robot.releaseMove(ReleasePosition.UP);
                    command++;
                } else {
                    robot.adjustHeading(targetHeading, true);
                }
                break;

            case 6:
                commandString = "Drive to column";
                power = robot.slowDownScale(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.BACKWARD, power);
                if (power == 0) {
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
                    timer.reset();
                    robot.glyphClamps.clampBack(GlyphClamps.ClampPos.RELEASE);
                    robot.glyphClamps.clampFront(GlyphClamps.ClampPos.RELEASE);
                    command++;
                }
                break;

            case 7:
                commandString = "knock glyph";
                if (timer.milliseconds() <= 1000) {
                    robot.drive(MovementEnum.FORWARD, .3);
                } else if (timer.milliseconds() <= 1600) {
                    robot.glyphClamps.clampBack(GlyphClamps.ClampPos.CLAMPED);
                    robot.glyphClamps.clampFront(GlyphClamps.ClampPos.CLAMPED);
                    robot.drive(MovementEnum.BACKWARD, 1);
                } else {
                    robot.drive(MovementEnum.STOP);
                    generalTarget = robot.distanceToRevsNRO20(10);
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    try {Thread.sleep(300);} catch (Exception e) {}
                    robot.runToPosition(generalTarget);
                    command++;
                    robot.releaseMove(ReleasePosition.DROP);
                    robot.jewelOut();
                    robot.intakeDrop.setPower(-1);
                    timer.reset();
                }
                break;

            case 8:
                commandString = "Drive away";
                power = robot.slowDownScale(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.FORWARD, power);
                if (power == 0) {
                    robot.drive(MovementEnum.STOP, 0);
                }

                if (timer.milliseconds() > 800) {
                    relicTimer.reset();
                    robot.relicArmVexControl(.8, DcMotorSimple.Direction.FORWARD);
                    robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
                    timer.reset();
                    command++;
                } else if (timer.milliseconds() > 550) {
                    robot.intakeDrop.setPower(0);
                    robot.releaseMove(ReleasePosition.MIDDLE);
                    robot.jewelUpTeleop();
                }
                break;

            case 9:
                commandString = "Adjust heading to 90";
                if (timer.milliseconds() > 1500) {
                    robot.drive(MovementEnum.STOP);
                    timer.reset();
                    command++;
                } else {
                    robot.adjustHeading(90, true);
                }
                break;

            case 10:
                commandString = "Move to middle column";
                curDistance = robot.rangeLeft.getDistance(DistanceUnit.CM);
                if (Math.abs(66 - curDistance) <= 2.5) {
                    robot.drive(MovementEnum.STOP);
                    robot.releaseMove(ReleasePosition.MIDDLE);
                    counter++;
                } else if (66 > curDistance) {
                    robot.safeStrafe(90, true, telemetry, .5);
                    counter = 0;
                } else {
                    robot.drive(MovementEnum.LEFTSTRAFE, .1);
                    counter = 0;
                }

                if (counter > 10) {
                    robot.glyphClamps.clampFront(GlyphClamps.ClampPos.STANDARD);
                    robot.glyphClamps.clampBack(GlyphClamps.ClampPos.STANDARD);
                    robot.drive(MovementEnum.STOP);
                    timer.reset();
                    counter = 0;
                    command++;
                }
                break;

            case 11:
                commandString = "Reorient to 60";
                if (timer.milliseconds() > 750) {
                    robot.drive(MovementEnum.STOP);
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    timer.reset();
                    command++;
                } else {
                    robot.adjustHeading(60, false);
                }
                break;

            case 12:
                commandString = "Setup drive to glyph pit";
                if (timer.milliseconds() > 100) {
                    generalTarget = robot.distanceToRevsNRO20(115); //105 -> 115
                    robot.intake(-.8);
                    robot.releaseMove(ReleasePosition.DOWN);
                    robot.runToPosition(generalTarget);
                    timer.reset();
                    robot.backIntakeWallUp();
                    command++;
                }
                break;

            case 13:
                commandString = "Drive to glyph pit";
                power = robot.slowDownScaleFast(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.FORWARD, power);
                if (power == 0) {
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    robot.releaseMove(ReleasePosition.MIDDLE);
                    timer.reset();
                    command++;
                }
                break;

            case 14:
                commandString = "Setup drive away from glyph pit";
                if (timer.milliseconds() > 250) {
                    generalTarget = -1 * robot.distanceToRevsNRO20(107);
                    robot.runToPosition(generalTarget);
                    timer.reset();
                    command++;
                }
                break;

            case 15:
                commandString = "Drive away from glyph pit";
                if (timer.milliseconds() > 250) {
                    timer.reset();
                    lastPosition = (lastPosition == ReleasePosition.MIDDLE ? ReleasePosition.DOWN:ReleasePosition.MIDDLE);
                    robot.releaseMove(lastPosition);
                }
                power = robot.slowDownScaleFast(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.BACKWARD, power);
                if (power == 0) {
                    robot.releaseMove(ReleasePosition.MIDDLE);
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
                    robot.glyphClamps.clampBack(GlyphClamps.ClampPos.CLAMPED);
                    robot.glyphClamps.clampFront(GlyphClamps.ClampPos.CLAMPED);
                    robot.intake(0);
                    timer.reset();
                    command++;
                }
                break;

            case 16:
                commandString = "Reorient to 90";
                if (timer.milliseconds() > 750) {
                    robot.drive(MovementEnum.STOP);
                    timer.reset();
                    command++;
                } else {
                    robot.adjustHeading(90, false);
                }
                break;

            case 17:
                commandString = "Move to middle column";
                curDistance = robot.rangeLeft.getDistance(DistanceUnit.CM);
                if (Math.abs(66 - curDistance) <= 2.5) {
                    robot.drive(MovementEnum.STOP);
                    robot.releaseMove(ReleasePosition.MIDDLE);
                    counter++;
                } else if (66 > curDistance) {
                    robot.safeStrafe(90, true, telemetry, .5);
                    counter = 0;
                } else {
                    robot.drive(MovementEnum.LEFTSTRAFE, .1);
                    counter = 0;
                }

                if (counter > 10) {
                    robot.glyphClamps.clampFront(GlyphClamps.ClampPos.STANDARD);
                    robot.glyphClamps.clampBack(GlyphClamps.ClampPos.STANDARD);
                    robot.drive(MovementEnum.STOP);
                    timer.reset();
                    counter = 0;
                    command++;
                }
                break;

            case 18:
                if (timer.milliseconds() > 500) {
                    robot.glyphClamps.clampFront(GlyphClamps.ClampPos.CLAMPED);
                    robot.glyphClamps.clampBack(GlyphClamps.ClampPos.CLAMPED);
                    timer.reset();
                    command++;
                }
                break;

            case 19:
                if (timer.milliseconds() > 250) {
                    robot.releaseMove(ReleasePosition.UP);
                    timer.reset();
                    command++;
                }
                break;

            case 20:
                if (timer.milliseconds() > 250) {
                    robot.glyphClamps.clampBack(GlyphClamps.ClampPos.RELEASE);
                    robot.glyphClamps.clampFront(GlyphClamps.ClampPos.RELEASE);
                    timer.reset();
                    command++;
                }
                break;

            case 21:
                commandString = "knock glyph";
                if (timer.milliseconds() <= 1000) {
                    robot.drive(MovementEnum.FORWARD, .3);
                } else if (timer.milliseconds() <= 1600) {
                    robot.glyphClamps.clampBack(GlyphClamps.ClampPos.CLAMPED);
                    robot.glyphClamps.clampFront(GlyphClamps.ClampPos.CLAMPED);
                    robot.drive(MovementEnum.BACKWARD, 1);
                } else {
                    robot.drive(MovementEnum.STOP);
                    generalTarget = robot.distanceToRevsNRO20(10);
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    try {Thread.sleep(300);} catch (Exception e) {}
                    robot.runToPosition(generalTarget);
                    command++;
                    timer.reset();
                }
                break;

            case 22:
                commandString = "Drive away";
                power = robot.slowDownScale(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.FORWARD, power);
                if (power == 0) {
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
                    timer.reset();
                    command++;
                }
                break;

            case 23:
                commandString = "Reorient to 90";
                if (timer.milliseconds() > 750) {
                    robot.drive(MovementEnum.STOP);
                    timer.reset();
                    if (globalTime.seconds() <= 20) {
                        command = 10;
                    } else {
                        command++;
                    }
                } else {
                    robot.adjustHeading(90, false);
                }
                break;
        }

        telemetry.addData("Command", command);
        telemetry.addData("Column", vuMark);
        telemetry.addLine(commandString);

        telemetry.update();
    }

    @Override
    public void stop() {
        robot.drive(MovementEnum.STOP);
    }
}

