package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Utility.Bot;
import org.firstinspires.ftc.teamcode.Utility.MovementEnum;
import org.firstinspires.ftc.teamcode.Utility.ReleasePosition;
import org.firstinspires.ftc.teamcode.Vision.ClosableVuforiaLocalizer;

@Autonomous(name = "VuforiaRedSG", group = "Auton")
//@Disabled
public class VuforiaRedSG extends OpMode {
    Bot robot = new Bot();
    ElapsedTime timer = new ElapsedTime();

    private ClosableVuforiaLocalizer vuforia;
    private VuforiaTrackables relicTrackables;
    private VuforiaTrackable relicTemplate;
    private RelicRecoveryVuMark vuMark;

    private double power = 0;
    private int generalTarget = 0;
    private boolean hitjewel = false;
    private int command = -1;
    private String commandString = "";

    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.releaseMove(ReleasePosition.INIT);
        robot.jewelUp();
        robot.backIntakeWallUp();
        robot.setDriveZeroPowers(DcMotor.ZeroPowerBehavior.BRAKE);

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
        robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
        relicTrackables.activate();
    }

    @Override
    public void loop() {
        switch (command) {
            case -1:
                commandString = "Find VuMark";
                vuMark = RelicRecoveryVuMark.from(relicTemplate);
                if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
                    robot.jewelOut();
                    timer.reset();
                    command++;
                } else if (timer.milliseconds() > 1000) {
                    robot.jewelOut();
                    timer.reset();
                    command++;
                }
                break;

            case 0:
                commandString = "Deactivate Vuforia";
                if (timer.milliseconds() > 250){
                    timer.reset();
                    robot.jewelOuter();
                    relicTrackables.deactivate();
                    vuforia.close();
                    command++;
                }
                break;

            case 1:
                commandString = "Hit Jewel";
                if (hitjewel) {
                    robot.drive(MovementEnum.STOP);
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

                } else if (robot.colorSensor.blue() >= 2) {
                    hitjewel = true;
                    robot.jewelKnockback();

                } else if (robot.colorSensor.red() >= 2) {
                    hitjewel = true;
                    robot.jewelKnockforward();
                }
                break;

            case 2:
                commandString = "Set up RUN_TO_POSITION";
                generalTarget = robot.distanceToRevs(50);
                robot.runToPosition(generalTarget);
                //Possibly turn off brake
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
                commandString = "Adjust heading to 90";
                if (timer.milliseconds() > 1000 || robot.FR.getPower() == 0) {
                    robot.drive(MovementEnum.STOP);
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    timer.reset();
                    command++;
                } else {
                    robot.adjustHeading(-90, false, telemetry);
                }
                break;

            case 5:
                commandString = "Choose column";
                switch (vuMark) {
                    case LEFT:
                        generalTarget = robot.distanceToRevs(27);
                        break;

                    case CENTER:
                        generalTarget = robot.distanceToRevs(36);
                        break;

                    case RIGHT:
                        generalTarget = robot.distanceToRevs(45);
                        break;

                    case UNKNOWN:
                        generalTarget = robot.distanceToRevs(36);
                        break;
                }
                try {Thread.sleep(300);} catch (Exception e) {}
                robot.runToPosition(generalTarget);
                timer.reset();
                command++;
                break;

            case 6:
                commandString = "Drive to column";
                power = robot.slowDownScale(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.FORWARD, power);
                if (power == 0) {
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
                    timer.reset();
                    command++;
                }
                break;

            case 7:
                commandString = "Adjust heading to 0";
                if (timer.milliseconds() > 1500 || robot.FR.getPower() == 0) {
                    robot.drive(MovementEnum.STOP);
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    timer.reset();
                    command++;
                } else {
                    robot.adjustHeading(0, false, telemetry);
                }
                break;

            case 8:
                commandString = "Begin unfold";
                robot.releaseMove(ReleasePosition.DROP);
                robot.jewelOut();
                robot.intakeDrop.setPower(-1);
                timer.reset();
                command++;
                break;

            case 9:
                commandString = "Unfold";
                if (timer.milliseconds() > 1000) {
                    robot.flipDown();
                    timer.reset();
                    command++;
                } else if (timer.milliseconds() > 750) {
                    robot.intakeDrop.setPower(0);
                    robot.releaseMove(ReleasePosition.MIDDLE);
                    robot.flipUp();
                    robot.jewelUp();
                }
                break;

            case 10:
                commandString = "Setup drive to box";
                generalTarget = -1*robot.distanceToRevs(19);
                robot.runToPosition(generalTarget);
                timer.reset();
                command++;
                break;

            case 11:
                commandString = "Drive to box";
                power = robot.slowDownScale(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.BACKWARD, power);
                if (power == 0) {
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
                    timer.reset();
                    command++;
                }
                break;

            case 12:
                commandString = "Intake wall down";
                robot.backIntakeWallDown();
                robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                timer.reset();
                command++;
                break;

            case 13:
                commandString = "Release glyph";
                if (timer.milliseconds() > 250) {
                    robot.releaseMove(ReleasePosition.UP);
                    timer.milliseconds();
                    command++;
                }
                break;

            case 14:
                commandString = "Setup drive away from box";
                if (timer.milliseconds() > 250) {
                    generalTarget = robot.distanceToRevs(15);
                    robot.runToPosition(generalTarget);
                    timer.reset();
                    command++;
                }
                break;

            case 15:
                commandString = "Drive away from box";
                power = robot.slowDownScale(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.FORWARD, power);
                if (power == 0) {
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
                    timer.reset();
                    command++;
                }
                break;


        }

        telemetry.addData("Command", command);
        telemetry.addData("Column", vuMark);
        telemetry.addData("Power", power);
        telemetry.addData("Target", generalTarget);
        telemetry.addLine(commandString);

        telemetry.update();

    }

    @Override
    public void stop() {

    }

}