package org.firstinspires.ftc.teamcode.Testing;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.JewelDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Utility.Bot;

/**
 * Created by aburur on 3/19/18.
 */

@Autonomous(name="Jewel Detector Test", group="Testing")
public class JewelDetectorTest extends OpMode
{
    Bot robot = new Bot();
    private ElapsedTime runtime = new ElapsedTime();
    private JewelDetector jewelDetector;

    private int red_blue = 0, blue_red = 0, unknown = 0;


    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.setDriveZeroPowers(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.jewelOut();

        jewelDetector = new JewelDetector();
        jewelDetector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());

        //Jewel Detector Settings
        jewelDetector.areaWeight = 0.02;
        jewelDetector.detectionMode = JewelDetector.JewelDetectionMode.MAX_AREA; // PERFECT_AREA
        //jewelDetector.perfectArea = 6500; <- Needed for PERFECT_AREA
        jewelDetector.debugContours = true;
        jewelDetector.maxDiffrence = 15;
        jewelDetector.ratioWeight = 15;
        jewelDetector.minArea = 700;

        jewelDetector.enable();
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        robot.jewelOuterBlue();
        runtime.reset();
    }

    @Override
    public void loop() {

        switch (jewelDetector.getCurrentOrder()) {
            case BLUE_RED:
                blue_red++;
                break;
            case RED_BLUE:
                red_blue++;
                break;
            case UNKNOWN:
                unknown++;
                break;
        }

        if (runtime.milliseconds() > 1000) {
            if (blue_red > red_blue && blue_red > unknown) {
                robot.jewelKnockforward();
            } else if (red_blue > blue_red && red_blue > unknown) {
                robot.jewelKnockback();
            } else {
                robot.jewelKnockforward();
                try {Thread.sleep(300);} catch (InterruptedException e) {}
                robot.jewelKnockback();
            }
        } else {
            robot.jewelOuterBlue();
        }

        telemetry.addData("blue_red", blue_red);
        telemetry.addData("red_blue", red_blue);
        telemetry.addData("unknown", unknown);

        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Current Order", "Jewel Order: " + jewelDetector.getCurrentOrder().toString()); // Current Result
        telemetry.addData("Last Order", "Jewel Order: " + jewelDetector.getLastOrder().toString()); // Last Known Result

    }

    @Override
    public void stop() {
        jewelDetector.disable();
    }

}
