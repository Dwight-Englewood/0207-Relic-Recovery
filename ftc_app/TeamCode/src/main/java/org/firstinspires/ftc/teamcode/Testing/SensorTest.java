package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by aburur on 12/19/17.
 */

@TeleOp(name = "Sensor Test", group = "Teleop")
//@Disabled
public class SensorTest extends OpMode {

    ModernRoboticsI2cRangeSensor rangeBack;
    ModernRoboticsI2cColorSensor colorSensor;


    public void init() {
        colorSensor = hardwareMap.get(ModernRoboticsI2cColorSensor.class, "cs");
        rangeBack = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "range");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        /*telemetry.addData("Left CS red", robot.cryptoColorL.red());
        telemetry.addData("Left CS blue", robot.cryptoColorL.blue());
        telemetry.addData("Right CS red", robot.cryptoColorR.red());
        telemetry.addData("Right CS blue", robot.cryptoColorR.blue()); */

        telemetry.addData("range", rangeBack.getDistance(DistanceUnit.CM));
        telemetry.addData("range Optical", rangeBack.cmOptical());
        telemetry.addData("range ultrasonic", rangeBack.cmUltrasonic());
        telemetry.addData("jewel red", colorSensor.red());
        telemetry.addData("jewel blue", colorSensor.blue());

        telemetry.update();
    }

    @Override
    public void stop() {

    }
}