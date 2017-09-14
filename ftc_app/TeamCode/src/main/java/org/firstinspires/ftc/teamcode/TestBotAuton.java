package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;

/**
 * Created by aburur on 9/14/17.
 */

    @Autonomous(name = "TestBotAuton", group = "Auton")
    public class TestBotAuton extends LinearOpMode {

        DistanceSensor sensorDistance1;
        DistanceSensor sensorDistance2;

        @Override
        public void runOpMode() {

            // get a reference to the distance sensor that shares the same name.
            sensorDistance1 = hardwareMap.get(DistanceSensor.class, "sensor1");
            sensorDistance2 = hardwareMap.get(DistanceSensor.class, "sensor2");

            // wait for the start button to be pressed.
            waitForStart();

            // loop and distance data.
            // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
            while (opModeIsActive()) {
                // send the info back to driver station using telemetry function.
                telemetry.addData("distance1", sensorDistance1.getDistance(DistanceUnit.CM));
                telemetry.addData("distance2", sensorDistance2.getDistance(DistanceUnit.CM));
            }

                telemetry.update();
            }

}
