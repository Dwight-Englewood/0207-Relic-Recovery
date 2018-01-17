package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Utility.MovementEnum;
import org.firstinspires.ftc.teamcode.Utility.ReleasePosition;
import org.firstinspires.ftc.teamcode.Vision.ClosableVuforiaLocalizer;

@Autonomous(name = "BlueAutonCloseV2", group = "Auton")
//@Disabled
public class BlueAutonCloseV2 extends OpMode {
    private Bot robot = new Bot();
    private ElapsedTime timer;
    private int command = -1;
    private ClosableVuforiaLocalizer vuforia;
    private VuforiaTrackables relicTrackables;
    private VuforiaTrackable relicTemplate;
    private RelicRecoveryVuMark vuMark;

    private double power;
    private int generalTarget;
    private boolean hitjewel = false;

    @Override
    public void init() {
        robot.init(hardwareMap);
        timer = new ElapsedTime();
        robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.releaseMove(ReleasePosition.INIT);
        robot.jewelUp();
        robot.backIntakeWallUp();

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = "AbZUuPf/////AAAAGUmS0Chan00iu7rnRhzu63+JgDtPo889M6dNtjvv+WKxiMJ8w2DgSJdM2/zEI+a759I7DlPj++D2Ryr5sEHAg4k1bGKdo3BKtkSeh8hCy78w0SIwoOACschF/ImuyP/V259ytjiFtEF6TX4teE8zYpQZiVkCQy0CmHI9Ymoa7NEvFEqfb3S4P6SicguAtQ2NSLJUX+Fdn49SEJKvpSyhwyjbrinJbak7GWqBHcp7fGh7TNFcfPFMacXg28XxlvVpQaVNgkvuqolN7wkTiR9ZMg6Fnm0zN4Xjr5lRtDHeE51Y0bZoBUbyLWSA+ts3SyDjDPPUU7GMI+Ed/ifb0csVpM12aOiNr8d+HsfF2Frnzrj2";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        vuforia = new ClosableVuforiaLocalizer(parameters);

        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);

        telemetry.addData(">", "Press Play to start");
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
                if (timer.milliseconds() > 500){
                    timer.reset();
                    robot.jewelOuter();
                    relicTrackables.deactivate();
                    vuforia.close();
                    command++;
                }
                break;

            case 1:
                if (hitjewel) {
                    robot.drive(MovementEnum.STOP);
                    robot.jewelUp();
                    timer.reset();
                    generalTarget = robot.distanceToRevs(50);
                    robot.runToPosition(generalTarget);
                    command++;
                } else if (timer.milliseconds() > 1500) {
                    robot.drive(MovementEnum.STOP);
                    robot.jewelKnockforward();
                    try {Thread.sleep(300);}catch(Exception e){}
                    robot.jewelKnockback();
                    try {Thread.sleep(300);}catch(Exception e){}
                    robot.jewelUp();
                    timer.reset();
                    generalTarget = robot.distanceToRevs(50);
                    robot.runToPosition(generalTarget);
                    command++;
                } else if (robot.colorSensor.blue() >= 1) {
                    hitjewel = true;
                    robot.jewelKnockback();
                } else if (robot.colorSensor.red() >= 1) {
                    hitjewel = true;
                    robot.jewelKnockforward();
                }
                break;

            case 2:
                power = robot.slowDownScale(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.FORWARD, power);
                if (power == 0) {
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
                    timer.reset();
                    command++;
                }
                break;

            case 3:
                if (timer.milliseconds() > 800) {
                    robot.drive(MovementEnum.STOP);
                    timer.reset();
                    generalTarget = robot.distanceToRevs(27);
                    robot.runToPosition(generalTarget);
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    command++;
                } else {
                    robot.adjustHeading(90, false, telemetry);
                }
                break;

            case 4:
                if (timer.milliseconds() > 500) {
                    robot.runToPosition(generalTarget);
                    timer.reset();
                    command++;
                }
                break;

            case 5:
                power = robot.slowDownScale(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.FORWARD, power * .4);
                if (power == 0 || timer.milliseconds() > 2000) {
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
                    timer.reset();
                    command++;
                }
                break;

            case 6:
                if (timer.milliseconds() < 2000) {
                    robot.adjustHeading(0, false, telemetry);
                } else {
                    robot.drive(MovementEnum.STOP);
                    robot.setDriveZeroPowers(DcMotor.ZeroPowerBehavior.BRAKE);
                    timer.reset();
                    command++;
                }
                break;

            case 7:
                robot.releaseMove(ReleasePosition.DROP);
                robot.jewelOut();
                robot.intakeDrop.setPower(-1);
                if (timer.milliseconds() > 1000) {
                    robot.intakeDrop.setPower(0);
                    robot.releaseMove(ReleasePosition.MIDDLE);
                    robot.flipUp();
                    robot.jewelUp();
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    timer.reset();
                    robot.setDriveZeroPowers(DcMotor.ZeroPowerBehavior.FLOAT);
                    command++;
                }
                break;

            case 8:
                if (timer.milliseconds()  > 500) {
                    timer.reset();
                    command++;
                }
                break;

            case 9:
                robot.flipDown();
                timer.reset();
                generalTarget = -1*robot.distanceToRevs(19);
                robot.runToPosition(generalTarget);
                command++;
                break;

            case 10:
                power = robot.slowDownScale(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.BACKWARD, power);
                if (power == 0 || timer.milliseconds() > 2000) {
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    timer.reset();
                    command++;
                }
                break;

            case 11:
                robot.backIntakeWallDown();
                timer.reset();
                command++;
                break;

            case 12:
                if (timer.milliseconds() > 250) {
                    robot.releaseMove(ReleasePosition.UP);
                    timer.milliseconds();
                    command++;
                }
                break;

            case 13:
                if (timer.milliseconds() > 500) {
                    generalTarget = robot.distanceToRevs(20);
                    robot.runToPosition(generalTarget);
                    timer.reset();
                    command++;
                }
                break;

            case 14:
                power = robot.slowDownScale(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.FORWARD, power);
                if (power == 0 || timer.milliseconds() > 2000) {
                    robot.drive(MovementEnum.STOP, 0);
                    robot.releaseMove(ReleasePosition.MIDDLE);
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    timer.reset();
                    command++;
                }
                break;
        }

        telemetry.addData("vuMark", vuMark);
        telemetry.addData("red", robot.colorSensor.red());
        telemetry.addData("blue", robot.colorSensor.blue());
        telemetry.addData("time", timer.seconds());
        telemetry.addData("command", command);
        telemetry.update();
    }

    @Override
    public void stop() {

    }

}