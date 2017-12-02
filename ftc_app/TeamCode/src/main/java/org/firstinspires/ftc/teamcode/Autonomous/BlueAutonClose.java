
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

@Autonomous(name = "BlueAutonClose", group = "Auton")
//@Disabled
public class BlueAutonClose extends OpMode {
    Bot robot = new Bot();
    ElapsedTime timer;
    int command = 0;
    VuforiaLocalizer vuforia;
    VuforiaTrackables relicTrackables;
    VuforiaTrackable relicTemplate;
    RelicRecoveryVuMark vuMark;
    int targetFL;
    int targetFR;
    int targetBL;
    int targetBR;

    @Override
    public void init() {
        robot.init(hardwareMap);
        timer = new ElapsedTime();
        robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = "AbZUuPf/////AAAAGUmS0Chan00iu7rnRhzu63+JgDtPo889M6dNtjvv+WKxiMJ8w2DgSJdM2/zEI+a759I7DlPj++D2Ryr5sEHAg4k1bGKdo3BKtkSeh8hCy78w0SIwoOACschF/ImuyP/V259ytjiFtEF6TX4teE8zYpQZiVkCQy0CmHI9Ymoa7NEvFEqfb3S4P6SicguAtQ2NSLJUX+Fdn49SEJKvpSyhwyjbrinJbak7GWqBHcp7fGh7TNFcfPFMacXg28XxlvVpQaVNgkvuqolN7wkTiR9ZMg6Fnm0zN4Xjr5lRtDHeE51Y0bZoBUbyLWSA+ts3SyDjDPPUU7GMI+Ed/ifb0csVpM12aOiNr8d+HsfF2Frnzrj2";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);

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
        robot.semiUnfoldBot();
        timer.reset();
        robot.jewelOut();
        robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
        relicTrackables.activate();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        switch (command) {
            case 0:
                vuMark = RelicRecoveryVuMark.from(relicTemplate);
                if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
                    command++;
                    telemetry.addData("VuMark", "%s visible", vuMark);
                } else {
                    telemetry.addData("VuMark", "not visible");
                }
                break;

            case 1:
                if (timer.milliseconds() > 2000) {
                    robot.drive(MovementEnum.STOP, 0);
                    robot.jewelUp();
                    timer.reset();
                    command++;
                } else if (robot.colorSensor.blue() >= 2) {
                    robot.adjustHeading(105);
                } else if (robot.colorSensor.red() >= 2) {
                    robot.adjustHeading(75);
                }
                break;

            case 2:
                if (timer.milliseconds() > 1000) {
                    timer.reset();
                    robot.drive(MovementEnum.STOP, 0);
                    command++;
                }
                robot.adjustHeading(90);
                break;

            case 3:
                robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                try {Thread.sleep(1000);} catch (InterruptedException e){}
                robot.setDriveMotorModes(DcMotor.RunMode.RUN_TO_POSITION);
                targetFR = targetFL = targetBR = targetBL = robot.distanceToRevs(75);
                robot.setDriveTargets(targetFL, targetFR, targetBL, targetBR);
                command++;
                break;

            case 4:
                double power = robot.slowDownScale(
                        robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(),
                        robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(),
                        targetFL, targetFR,
                        targetBL, targetBR);

                if (power == 0) {
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
                    timer.reset();
                    command++;
                } else {
                    robot.drive(MovementEnum.FORWARD, power);
                }
                break;

            case 5:
                if (timer.milliseconds() > 2000){
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    timer.reset();
                    command++;
                }
                robot.adjustHeading(180);
                break;


        }

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