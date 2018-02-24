package org.firstinspires.ftc.teamcode.Testing;
/**
 * Created by weznon on 2/23/18.
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorREVColorDistance;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@TeleOp(name = "BadSensorTest", group = "Teleop")
public class BadSensorTest extends OpMode {

    ColorSensor colorsensor;
    DistanceSensor distanceSensor;


    @Override
    public void init() {
        colorsensor = hardwareMap.get(ColorSensor.class, "revcs");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "revcs");

    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        telemetry.addData("csalpha", colorsensor.alpha());
        telemetry.addData("csred", colorsensor.red());
        telemetry.addData("csgreen", colorsensor.green());
        telemetry.addData("csblue", colorsensor.blue());
        telemetry.addData("ds", distanceSensor.getDistance(DistanceUnit.CM));
        telemetry.addData("productAlphaDistance", colorsensor.alpha() * distanceSensor.getDistance(DistanceUnit.CM));
    }

    @Override
    public void stop() {

    }
}
