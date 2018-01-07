
package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Enums.MovementEnum;
import org.firstinspires.ftc.teamcode.Enums.ReleasePosition;

@Autonomous(name = "BlueAutonClose", group = "Auton")
//@Disabled
public class BlueAutonClose extends OpMode {
    Bot robot = new Bot();
    ElapsedTime timer;
    int command = 0;
    //VuforiaLocalizer vuforia;
    //VuforiaTrackables relicTrackables;
    //VuforiaTrackable relicTemplate;
    //RelicRecoveryVuMark vuMark;

    double power;
    int generalTarget;
    boolean hitjewel = false;

    @Override
    public void init() {
        robot.init(hardwareMap);
        timer = new ElapsedTime();
        robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.releaseMove(ReleasePosition.INIT);
        robot.jewelUp();
        robot.backIntakeWallUp();

        //VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        //parameters.vuforiaLicenseKey = "AbZUuPf/////AAAAGUmS0Chan00iu7rnRhzu63+JgDtPo889M6dNtjvv+WKxiMJ8w2DgSJdM2/zEI+a759I7DlPj++D2Ryr5sEHAg4k1bGKdo3BKtkSeh8hCy78w0SIwoOACschF/ImuyP/V259ytjiFtEF6TX4teE8zYpQZiVkCQy0CmHI9Ymoa7NEvFEqfb3S4P6SicguAtQ2NSLJUX+Fdn49SEJKvpSyhwyjbrinJbak7GWqBHcp7fGh7TNFcfPFMacXg28XxlvVpQaVNgkvuqolN7wkTiR9ZMg6Fnm0zN4Xjr5lRtDHeE51Y0bZoBUbyLWSA+ts3SyDjDPPUU7GMI+Ed/ifb0csVpM12aOiNr8d+HsfF2Frnzrj2";
        //parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        //vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        //relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        //relicTemplate = relicTrackables.get(0);

        telemetry.addData(">", "Press Play to start");
        telemetry.update();
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        timer.reset();
        robot.jewelOut();
        robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
        //relicTrackables.activate();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        switch (command) {
            /*case 0:
                vuMark = RelicRecoveryVuMark.from(relicTemplate);
                if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
                    command++;
                    telemetry.addData("VuMark", "%s visible", vuMark);
                } else {
                    telemetry.addData("VuMark", "not visible");
                }
                break;*/

            case 0:
                if (timer.milliseconds() > 750){
                    timer.reset();
                    robot.jewelOuter();
                    command++;
                }
                break;

            case 1:
                if (timer.milliseconds() > 2000 && hitjewel) {
                    robot.drive(MovementEnum.STOP);
                    robot.jewelUp();
                    timer.reset();
                    generalTarget = robot.distanceToRevs(50);
                    robot.runToPosition(generalTarget);
                    command++;
                } else if (timer.milliseconds() > 2000) {
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
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    timer.reset();
                    command++;
                }
                break;

            case 3:
                if (timer.milliseconds() > 1000) {
                    command++;
                }
                break;

            case 4:
                timer.reset();
                generalTarget = robot.distanceToRevs(27);
                robot.runToPosition(generalTarget);
                command++;
                break;

            case 5:
                if (timer.milliseconds() > 800) {
                    robot.drive(MovementEnum.STOP);
                    robot.setDriveZeroPowers(DcMotor.ZeroPowerBehavior.BRAKE);
                    timer.reset();
                    command++;
                } else {
                    robot.adjustHeading(90, false);
                }
                break;

            case 6:
                power = robot.slowDownScale(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.FORWARD, power * .4);
                if (power == 0 || timer.milliseconds() > 2000) {
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
                    timer.reset();
                    command++;
                }
                break;

            case 7:
                if (timer.milliseconds() < 4000) {
                    robot.adjustHeading(0, false);
                } else {
                    robot.drive(MovementEnum.STOP);
                    timer.reset();
                    command++;
                }

                break;

            case 8:
                robot.relRDrop();
                robot.relLDrop();
                robot.jewelOut();
                robot.intakeDrop.setPower(-1);
                if (timer.milliseconds() > 1000) {
                    robot.intakeDrop.setPower(0);
                    robot.releaseMove(ReleasePosition.MIDDLE);
                    robot.flipUp();
                    robot.jewelUp();
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    timer.reset();
                    command++;
                }
                break;

            case 9:
                if (timer.milliseconds()  > 750) {
                    command++;
                }
                 //   robot.intake(-1);
                //} else {
                    //robot.intake(0);
                    //robot.flipUp();
                    //timer.reset();
                //}
                break;

            case 10:
                robot.flipDown();
                timer.reset();
                generalTarget = -1*robot.distanceToRevs(19);
                robot.runToPosition(generalTarget);
                command++;
                    //robot.releaseMove(ReleasePosition.UP);
                    //robot.drive(MovementEnum.BACKWARD, .1);
                    //robot.drive(MovementEnum.FORWARD, .1);
                    //timer.reset();
                    //command++;
                break;

            case 11:
                //if (timer.milliseconds() > 500) {
                //    robot.drive(MovementEnum.STOP);
                //    timer.reset();
                //    command++;
                //}
                power = robot.slowDownScale(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.BACKWARD, power);
                if (power == 0 || timer.milliseconds() > 2000) {
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
                    timer.reset();
                    command++;
                }
                break;

            case 12:
                if (timer.milliseconds() < 1000) {
                    robot.adjustHeading(0, false);
                    robot.backIntakeWallDown();
                } else {
                    robot.drive(MovementEnum.STOP);
                    timer.reset();
                    command++;
                }
                break;

            case 13:
                robot.releaseMove(ReleasePosition.UP);
                command++;
                break;

            case 14:
                if (timer.milliseconds() > 1000) {
                    robot.releaseMove(ReleasePosition.MIDDLE);
                    timer.reset();
                    generalTarget = robot.distanceToRevs(10);
                    robot.runToPosition(generalTarget);
                    command++;
                }
                break;

            case 15:
                power = robot.slowDownScale(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.FORWARD, power);
                if (power == 0 || timer.milliseconds() > 2000) {
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
                    timer.reset();
                    command++;
                }
                break;
        }

        telemetry.addData("red", robot.colorSensor.red());
        telemetry.addData("blue", robot.colorSensor.blue());
        telemetry.addData("time", timer.seconds());
        telemetry.addData("command", command);
        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }

}