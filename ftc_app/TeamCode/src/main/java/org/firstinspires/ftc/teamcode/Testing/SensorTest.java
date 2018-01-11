package org.firstinspires.ftc.teamcode.Testing;

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
        /*telemetry.addData("Left CS red", robot.cryptoColorL.red());
        telemetry.addData("Left CS blue", robot.cryptoColorL.blue());
        telemetry.addData("Right CS red", robot.cryptoColorR.red());
        telemetry.addData("Right CS blue", robot.cryptoColorR.blue()); */

        telemetry.addData("range", robot.rangeBack.getDistance(DistanceUnit.CM));
        telemetry.addData("range Optical", robot.rangeBack.cmOptical());
        telemetry.addData("range ultrasonic", robot.rangeBack.cmUltrasonic());
        telemetry.addData("jewel red", robot.colorSensor.red());
        telemetry.addData("jewel blue", robot.colorSensor.blue());

        telemetry.update();
    }

    @Override
    public void stop() {

    }
}