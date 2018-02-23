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
import org.firstinspires.ftc.teamcode.Utility.MovementEnum;
import org.firstinspires.ftc.teamcode.Utility.ReleasePosition;
import org.firstinspires.ftc.teamcode.Vision.ClosableVuforiaLocalizer;

@Autonomous(name = "VuforiaRedFarSG", group = "Auton")
//@Disabled
public class VuforiaRedFarSG extends OpMode {
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
                if (timer.milliseconds() > 600) {
                    robot.jewelOuter();
                    if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
                        robot.relicArmVexControl(.5, DcMotorSimple.Direction.REVERSE);
                        timer.reset();
                        command++;
                    } else if (timer.milliseconds() > 1600) {
                        robot.relicArmVexControl(.5, DcMotorSimple.Direction.REVERSE);
                        timer.reset();
                        command++;
                    }
                }
                break;

            case 0:
                commandString = "Deactivate Vuforia";
                if (timer.milliseconds() > 500){
                    robot.relicArmVexControl(0, DcMotorSimple.Direction.FORWARD);
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
                } else if (robot.colorSensor.blue() >= 1 && !hitjewel) {
                    hitjewel = true;
                    robot.jewelKnockback();
                    timer.reset();
                } else if (robot.colorSensor.red() >= 1 && !hitjewel) {
                    hitjewel = true;
                    robot.jewelKnockforward();
                    timer.reset();
                }
                break;

            case 2:
                commandString = "Set up RUN_TO_POSITION";
                generalTarget = robot.distanceToRevs(50);
                robot.runToPosition(generalTarget);
                timer.reset();
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