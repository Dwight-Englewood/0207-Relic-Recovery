package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Bot;

/**
 * Created by aburur on 12/19/17.
 */

@TeleOp(name = "Sensor Test", group = "Teleop")
//@Disabled
public class SensorTest extends OpMode {

    ModernRoboticsI2cRangeSensor rangeBack = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "range");
    ModernRoboticsI2cColorSensor colorSensor = hardwareMap.get(ModernRoboticsI2cColorSensor.class, "cs");


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