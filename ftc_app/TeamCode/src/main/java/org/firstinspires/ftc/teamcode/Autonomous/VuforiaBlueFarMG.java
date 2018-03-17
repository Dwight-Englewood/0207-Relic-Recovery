package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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
import org.firstinspires.ftc.teamcode.Utility.MovementEnum;
import org.firstinspires.ftc.teamcode.Utility.ReleasePosition;
import org.firstinspires.ftc.teamcode.Vision.ClosableVuforiaLocalizer;

import java.util.ArrayList;


/*




 */
@Autonomous(name = "VuforiaBlueFarMG", group = "Auton")
//@Disabled
public class VuforiaBlueFarMG extends OpMode {
    Bot robot = new Bot();
    ElapsedTime timer = new ElapsedTime();
    ElapsedTime relicTimer = new ElapsedTime();

    private ClosableVuforiaLocalizer vuforia;
    private VuforiaTrackables relicTrackables;
    private VuforiaTrackable relicTemplate;
    private RelicRecoveryVuMark vuMark;

    private double power = 0, initalDistance, curDistance;
    private ArrayList<Double> powersList = new ArrayList<Double>();
    private int generalTarget = 0, counter = 0;
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

        telemetry.addLine("Ready.");
        telemetry.update();
    }

    @Override
    public void init_loop() {}

    @Override
    public void start() {
        timer.reset();
        robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        relicTrackables.activate();
        relicTimer.reset();
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
                    robot.jewelUp();
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
                generalTarget = -1 * robot.distanceToRevsNRO20(72);
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
                commandString = "Adjust heading to -90";
                if (timer.milliseconds() > 2000) {
                    robot.drive(MovementEnum.STOP);
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    timer.reset();
                    command++;
                } else {
                    robot.adjustHeading(-90, false);
                }
                break;

            case 5:
                relicTimer.reset();
                robot.relicArmVexControl(.8, DcMotorSimple.Direction.FORWARD);
                commandString = "Begin unfold";
                robot.releaseMove(ReleasePosition.DROP);
                robot.jewelOut();
                robot.intakeDrop.setPower(-1);
                timer.reset();
                command++;
                break;

            case 6:
                commandString = "Unfold";
                if (timer.milliseconds() > 800) {
                    robot.flipDown();
                    timer.reset();
                    command++;
                } else if (timer.milliseconds() > 550) {
                    robot.intakeDrop.setPower(0);
                    robot.releaseMove(ReleasePosition.MIDDLE);
                    robot.flipUp();
                    robot.jewelUp();
                }
                break;

            case 7:
                commandString = "Adjust heading to -90";
                if (timer.milliseconds() > 1000) {
                    robot.drive(MovementEnum.STOP);
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    timer.reset();
                    command++;
                } else if (timer.milliseconds() > 250){
                    robot.adjustHeading(-90, false);
                }
                break;

            case 8:
                commandString = "Choose column";
                switch (vuMark) {
                    case LEFT:
                        generalTarget = 86;
                        break;

                    case CENTER:
                        generalTarget = 66;
                        break;

                    case RIGHT:
                        generalTarget = 46;
                        break;

                    case UNKNOWN:
                        generalTarget = 66;
                        break;
                }
                try {Thread.sleep(300);} catch (Exception e) {}
                robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
                timer.reset();
                command++;
                break;

            case 9:
                commandString = "Move to column";
                /*if (counter < 5) {
                    powersList.add(robot.slowDownScale(generalTarget, initalDistance, robot.rangeLeft));
                    counter++;
                } else {
                    power = 0;
                    for (double thing: powersList) {
                        power += thing;
                    }
                    power /= powersList.size();
                    powersList.clear();
                    robot.drive(MovementEnum.RIGHTSTRAFE, power);
                    robot.strafeAdjusts(90, MovementEnum.RIGHTSTRAFE);
                    if (power == 0) {
                        robot.drive(MovementEnum.STOP, 0);
                        timer.reset();
                        command++;
                    }
                    counter = 0;
                }*/
                //if (Math.abs(robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle - 90) >= 5) {
                //    robot.adjustHeading(90, false);
                //    command = 9;
                //} else {
                curDistance = robot.rangeRight.getDistance(DistanceUnit.CM);
                if (Math.abs(generalTarget - curDistance) < 2) {
                    robot.drive(MovementEnum.STOP);
                    counter++;
                } else if (generalTarget > curDistance) {
                    //robot.drive(MovementEnum.RIGHTSTRAFE, .5);
                    robot.safeStrafe(-90,false, telemetry, .5);
                    counter = 0;
                } else {
                    robot.drive(MovementEnum.RIGHTSTRAFE, .2);
                    counter = 0;
                }

                if (counter > 10) {
                    robot.drive(MovementEnum.STOP);
                    timer.reset();
                    counter = 0;
                    command++;
                }
                //}
                break;

            case 10:
                commandString = "Adjust heading to -90";
                if (timer.milliseconds() > 500) {
                    robot.drive(MovementEnum.STOP);
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    timer.reset();
                    command++;
                } else {
                    robot.adjustHeading(-90, false);
                }
                break;

            case 11:
                commandString = "Intake wall down";
                robot.backIntakeWallDown();
                timer.reset();
                command++;
                break;

            case 12:
                commandString = "Release glyph";
                if (timer.milliseconds() > 200) {
                    robot.releaseMove(ReleasePosition.UP);
                    timer.reset();
                    robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
                    command++;
                }
                break;

            case 13:
                commandString = "Drive away";
                if (timer.milliseconds() < 250) {
                    robot.drive(MovementEnum.FORWARD, .8);
                } else {
                    robot.drive(MovementEnum.STOP);
                    timer.reset();
                    command++;
                }
                break;

            case 14:
                commandString = "Drive back";
                if (timer.milliseconds() < 250) {
                    robot.drive(MovementEnum.BACKWARD, .9);
                } else {
                    robot.drive(MovementEnum.STOP);
                    timer.reset();
                    command++;
                }
                break;

            case 15:
                commandString = "Reorient to -90";
                if (timer.milliseconds() > 750) {
                    robot.drive(MovementEnum.STOP);
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    timer.reset();
                    command++;
                } else if (timer.milliseconds() > 250){
                    robot.adjustHeading(-90, true);
                }
                break;

            case 16:
                try {Thread.sleep(300);}catch(Exception e){}
                commandString = "Set up RUN_TO_POSITION";
                generalTarget = robot.distanceToRevsNRO20(10);
                robot.releaseMove(ReleasePosition.MIDDLE);
                robot.runToPosition(generalTarget);
                timer.reset();
                command++;
                break;

            case 17:
                commandString = "RUN_TO_POSITION";
                power = robot.slowDownScale(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.FORWARD, power);
                if (power == 0) {
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
                    generalTarget = 66;
                    timer.reset();
                    command++;
                }
                break;

            case 18:
                commandString = "Move to middle column";
                /*if (counter % 5 != 0) {
                    powersList.add(robot.slowDownScale(generalTarget, initalDistance, robot.rangeLeft));
                    counter++;
                } else {
                    power = 0;
                    for (double thing: powersList) {
                        power += thing;
                    }
                    power /= powersList.size();
                    powersList.clear();
                    robot.drive(MovementEnum.RIGHTSTRAFE, power);
                    robot.strafeAdjusts(90, MovementEnum.RIGHTSTRAFE);
                    if (power == 0) {
                        robot.drive(MovementEnum.STOP, 0);
                        timer.reset();
                        command++;
                    }
                    counter = 0;
                }*/
                //if (Math.abs(robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle - 90) >= 3) {
                //    robot.adjustHeading(90, false);
                //} else {
                curDistance = robot.rangeRight.getDistance(DistanceUnit.CM);
                if (Math.abs(generalTarget - curDistance) <= 2.5) {
                    robot.drive(MovementEnum.STOP);
                    counter++;
                } else if (generalTarget > curDistance) {
                    //robot.drive(MovementEnum.RIGHTSTRAFE, .5);
                    robot.safeStrafe(-90, false, telemetry, .5);
                    counter = 0;
                } else {
                    robot.drive(MovementEnum.RIGHTSTRAFE, .2);
                    counter = 0;
                }

                if (counter > 10) {
                    robot.drive(MovementEnum.STOP);
                    timer.reset();
                    counter = 0;
                    command++;
                }
                //}
                break;

            case 19:
                commandString = "Reorient to -60";
                if (timer.milliseconds() > 1000) {
                    robot.drive(MovementEnum.STOP);
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    timer.reset();
                    command++;
                } else {
                    robot.adjustHeading(-60, false);
                }
                break;

            case 20:
                commandString = "Setup drive to glyph pit";
                if (timer.milliseconds() > 100) {
                    generalTarget = robot.distanceToRevsNRO20(100);
                    robot.intake(-1);
                    robot.releaseMove(ReleasePosition.DOWN);
                    robot.runToPosition(generalTarget);
                    timer.reset();
                    command++;
                }
                break;

            case 21:
                commandString = "Drive to glyph pit";
                power = robot.slowDownScale(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.FORWARD, power);
                if (power == 0) {
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    robot.releaseMove(ReleasePosition.MIDDLE);
                    timer.reset();
                    command++;
                }
                break;

            case 22:
                commandString = "Setup drive away from glyph pit";
                if (timer.milliseconds() > 250) {
                    generalTarget = -1 * robot.distanceToRevsNRO20(90);
                    robot.runToPosition(generalTarget);
                    timer.reset();
                    command++;
                }
                break;

            case 23:
                commandString = "Drive away from glyph pit";
                if (timer.milliseconds() > 250) {
                    timer.reset();
                    lastPosition = (lastPosition == ReleasePosition.MIDDLE ? ReleasePosition.DOWN:ReleasePosition.MIDDLE);
                    robot.releaseMove(lastPosition);
                }
                power = robot.slowDownScale(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.BACKWARD, power);
                if (power == 0) {
                    robot.releaseMove(ReleasePosition.MIDDLE);
                    robot.intake(0);
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
                    robot.releaseMove(ReleasePosition.MIDDLE);
                    timer.reset();
                    command++;
                }
                break;

            case 24:
                commandString = "Reorient to 90";
                if (timer.milliseconds() > 750) {
                    robot.drive(MovementEnum.STOP);
                    timer.reset();
                    generalTarget = 86;
                    command++;
                } else {
                    robot.adjustHeading(90, false);
                }
                break;

            case 25:
                curDistance = robot.rangeLeft.getDistance(DistanceUnit.CM);
                if (Math.abs(generalTarget - curDistance) <= 2.5) {
                    robot.drive(MovementEnum.STOP);
                    counter++;
                } else if (generalTarget > curDistance) {
                    //robot.drive(MovementEnum.RIGHTSTRAFE, .5);
                    robot.safeStrafe(-90,false, telemetry, .5);
                    counter = 0;
                } else {
                    robot.drive(MovementEnum.RIGHTSTRAFE, .2);
                    counter = 0;
                }

                if (counter > 10) {
                    robot.drive(MovementEnum.STOP);
                    timer.reset();
                    counter = 0;
                    command++;
                }
                break;

            case 26:
                robot.backIntakeWallDown();
                if (timer.milliseconds() > 250) {
                    timer.reset();
                    command++;
                }
                break;

            case 27:
                robot.releaseMove(ReleasePosition.UP);
                timer.reset();
                command++;
                break;

            case 28:
                commandString = "Drive back";
                if (timer.milliseconds() < 250) {
                    robot.drive(MovementEnum.BACKWARD, .9);
                } else {
                    robot.drive(MovementEnum.STOP);
                    timer.reset();
                    command++;
                }
                break;

            case 29:
                commandString = "Drive forward";
                if (timer.milliseconds() < 250) {
                    robot.drive(MovementEnum.FORWARD, .8);
                } else {
                    robot.drive(MovementEnum.STOP);
                    robot.releaseMove(ReleasePosition.MIDDLE);
                    timer.reset();
                    command++;
                }
                break;

            case 30:
                stop();
                command++;
                break;



        }



        telemetry.addData("Command", command);
        telemetry.addData("Column", vuMark);
        telemetry.addLine(commandString);

        telemetry.update();

    }

    @Override
    public void stop() {

    }

}