package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Utility.Bot;
import org.firstinspires.ftc.teamcode.Utility.GlyphClamps;
import org.firstinspires.ftc.teamcode.Utility.MovementEnum;
import org.firstinspires.ftc.teamcode.Utility.ReleasePosition;
import org.firstinspires.ftc.teamcode.Vision.ClosableVuforiaLocalizer;

import java.util.ArrayList;

@Autonomous(name = "WorldsVuforiaBlueMG", group = "Auton")
//@Disabled
public class WorldsVuforiaBlueMG extends OpMode {
    Bot robot = new Bot();
    ElapsedTime timer = new ElapsedTime();
    ElapsedTime relicTimer = new ElapsedTime();

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

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = "AbZUuPf/////AAAAGUmS0Chan00iu7rnRhzu63+JgDtPo889M6dNtjvv+WKxiMJ8w2DgSJdM2/zEI+a759I7DlPj++D2Ryr5sEHAg4k1bGKdo3BKtkSeh8hCy78w0SIwoOACschF/ImuyP/V259ytjiFtEF6TX4teE8zYpQZiVkCQy0CmHI9Ymoa7NEvFEqfb3S4P6SicguAtQ2NSLJUX+Fdn49SEJKvpSyhwyjbrinJbak7GWqBHcp7fGh7TNFcfPFMacXg28XxlvVpQaVNgkvuqolN7wkTiR9ZMg6Fnm0zN4Xjr5lRtDHeE51Y0bZoBUbyLWSA+ts3SyDjDPPUU7GMI+Ed/ifb0csVpM12aOiNr8d+HsfF2Frnzrj2";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = new ClosableVuforiaLocalizer(parameters);

        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTrackables.activate();
        robot.glyphClamps.clampBack(GlyphClamps.ClampPos.CLAMPED);
        robot.glyphClamps.clampFront(GlyphClamps.ClampPos.CLAMPED);

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
        robot.backIntakeWallDown();
        robot.relicArmVexControl(.8, DcMotorSimple.Direction.REVERSE);

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
                    robot.jewelOuterBlue();
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
                    robot.jewelUp();
                    timer.reset();
                    command++;
                } else if ((robot.jewelColorForward.red() >= 2 || robot.jewelColorBack.blue() >=2 ) && !hitjewel) {
                    hitjewel = true;
                    robot.jewelKnockforward();
                    timer.reset();
                } else if ((robot.jewelColorBack.red() >= 2 || robot.jewelColorForward.blue() >= 2) && !hitjewel) {
                    hitjewel = true;
                    robot.jewelKnockback();
                    timer.reset();
                }
                break;

            case 2:
                commandString = "Set up RUN_TO_POSITION";
                generalTarget = -1 * robot.distanceToRevsNRO20(65);
                robot.runToPosition(generalTarget);
                timer.reset();
                command++;
                break;

            case 3:
                commandString = "RUN_TO_POSITION";
                power = robot.slowDownScale(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.BACKWARD, power);
                if (power == 0) {
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
                    timer.reset();
                    command++;
                }
                break;

            case 4:
                commandString = "Choose column";
                switch (vuMark) {
                    case LEFT:
                        generalTarget = -1 * robot.distanceToRevsNRO20(16);
                        targetHeading = -12;
                        break;

                    case CENTER:
                        generalTarget = -1 * robot.distanceToRevsNRO20(23);
                        targetHeading = -30;
                        break;

                    case RIGHT:
                        generalTarget = -1 * robot.distanceToRevsNRO20(30);
                        targetHeading = -45;
                        break;

                    case UNKNOWN:
                        generalTarget = -1 * robot.distanceToRevsNRO20(16);
                        targetHeading = -12;
                        break;
                }
                try { Thread.sleep(300); } catch (Exception e) { }
                timer.reset();
                command++;
                break;

            case 5:
                commandString = "Adjust heading to target";
                if (timer.milliseconds() > 2000) {
                    robot.drive(MovementEnum.STOP);
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    try{Thread.sleep(300);} catch(Exception e) {}
                    robot.runToPosition(generalTarget);
                    timer.reset();
                    robot.releaseMove(ReleasePosition.UP);
                    command++;
                } else {
                    robot.adjustHeading(targetHeading, false);
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
                    robot.releaseMove(ReleasePosition.INIT);
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

            case 8:
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

            case 9:
                commandString = "Adjust heading to 0";
                if (timer.milliseconds() > 1500) {
                    robot.drive(MovementEnum.STOP);
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    timer.reset();
                    command++;
                } else {
                    robot.adjustHeading(0, false);
                }
                break;

            case 10:
                relicTimer.reset();
                robot.relicArmVexControl(.8, DcMotorSimple.Direction.FORWARD);
                commandString = "Begin unfold";
                robot.releaseMove(ReleasePosition.DROP);
                robot.jewelOut();
                robot.intakeDrop.setPower(-1);
                timer.reset();
                command++;
                break;

            case 11:
                commandString = "Unfold";
                if (timer.milliseconds() > 800) {
                    timer.reset();
                    command++;
                } else if (timer.milliseconds() > 550) {
                    robot.intakeDrop.setPower(0);
                    robot.releaseMove(ReleasePosition.MIDDLE);
                    robot.jewelUp();
                }
                break;

            case 12:
                commandString = "Setup drive to glyph pit";
                generalTarget = robot.distanceToRevsNRO20(80);
                robot.intake(-.7);
                robot.backIntakeWallUp();
                robot.releaseMove(ReleasePosition.DOWN);
                robot.runToPosition(generalTarget);
                timer.reset();
                command++;
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
                if (timer.milliseconds() > 200) {
                    generalTarget = -1 * robot.distanceToRevsNRO20(75);
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
                    robot.intake(0);
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
                    robot.releaseMove(ReleasePosition.MIDDLE);
                    robot.glyphClamps.clampBack(GlyphClamps.ClampPos.CLAMPED);
                    robot.glyphClamps.clampFront(GlyphClamps.ClampPos.CLAMPED);
                    timer.reset();
                    command++;
                }
                break;

            case 16:
                commandString = "Adjust heading to target";
                if (timer.milliseconds() > 2000) {
                    robot.drive(MovementEnum.STOP);
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    try{Thread.sleep(300);} catch(Exception e) {}
                    robot.runToPosition(generalTarget);
                    timer.reset();
                    robot.releaseMove(ReleasePosition.UP);
                    command++;
                } else {
                    robot.adjustHeading(targetHeading, false);
                }
                break;

            case 17:
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

            case 18:
                if (timer.milliseconds() < 1000) {
                    robot.drive(MovementEnum.FORWARD, .4);
                } else {
                    robot.drive(MovementEnum.STOP);
                    timer.reset();
                    command++;
                }
                break;

            case 19:
                if (timer.milliseconds() > 500) {
                    robot.releaseMove(ReleasePosition.MIDDLE);
                    timer.reset();
                    command++;
                } else {
                    robot.glyphClamps.clampFront(GlyphClamps.ClampPos.CLAMPED);
                    robot.glyphClamps.clampBack(GlyphClamps.ClampPos.CLAMPED);
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