package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utility.Bot;

/**
 * Created by aburur on 12/19/17.
 */

@TeleOp(name = "Sensor Test", group = "Testing")
//@Disabled
public class SensorTest extends OpMode {
    Bot robot = new Bot();

    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        robot.jewelOuterRed();
    }

    @Override
    public void loop() {
        telemetry.addData("intakeColorAlpha: ", robot.intakeColor.alpha());
        telemetry.addData("intakeColorRed", robot.intakeColor.red());
        telemetry.addData("intakeColorBlue", robot.intakeColor.blue());
        telemetry.addData("intakeColorGreen", robot.intakeColor.green());
        telemetry.addData("forward red", robot.jewelColorForward.red());
        telemetry.addData("forward blue", robot.jewelColorForward.blue());
        telemetry.addData("back red", robot.jewelColorForward.red());
        telemetry.addData("back blue", robot.jewelColorForward.blue());



        telemetry.update();
    }

    @Override
    public void stop() {

    }
}