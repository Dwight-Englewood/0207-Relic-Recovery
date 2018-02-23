package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
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

    }

    @Override
    public void loop() {
        robot.jewelOut();
        telemetry.addData("intakeColorAlpha: ", robot.intakeColor.alpha());
        telemetry.addData("intakeColorRed", robot.intakeColor.red());
        telemetry.addData("intakeColorBlue", robot.intakeColor.blue());
        telemetry.addData("intakeColorGreen", robot.intakeColor.green());
        telemetry.addData("odsLightDetected", robot.odsFront.getLightDetected());
        telemetry.addData("odsLightDetectedRaw", robot.odsFront.getRawLightDetected());
        telemetry.addData("jewel red: ", robot.colorSensor.red());
        telemetry.addData("jewel blue", robot.colorSensor.blue());
        telemetry.addData("front range", robot.rangeFront.getDistance(DistanceUnit.CM));

        telemetry.update();
    }

    @Override
    public void stop() {

    }
}