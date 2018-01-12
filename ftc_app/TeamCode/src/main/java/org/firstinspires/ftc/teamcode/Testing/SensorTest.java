package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Bot;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by aburur on 12/19/17.
 */

@TeleOp(name = "Sensor Test", group = "Teleop")
//@Disabled
public class SensorTest extends OpMode {
    Bot robot = new Bot();
    Constructor<ModernRoboticsI2cRangeSensor.Register> woop;
    ModernRoboticsI2cRangeSensor.Register kms;

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

        try {
            woop = ModernRoboticsI2cRangeSensor.Register.class.getDeclaredConstructor(int.class);
        } catch (NoSuchMethodException a) {

        }

        woop.setAccessible(true);

        try {
            kms = woop.newInstance(4);
        } catch (InstantiationException a) {

        } catch (IllegalAccessException a) {

        } catch (InvocationTargetException a) {

        }

        //telemetry.addData("reg4", robot.rangeBack.read8(kms));

        //telemetry.addData("range", robot.rangeBack.getDistance(DistanceUnit.CM));
        //telemetry.addData("range Optical", robot.rangeBack.cmOptical());
        //telemetry.addData("range ultrasonic", robot.rangeBack.cmUltrasonic());
        //telemetry.addData("jewel red", robot.colorSensor.red());

        //for use when rangeBack is an I2cDeviceSynch
        telemetry.addData("reg 4: range", robot.rangeBack.read(4, 1));
        telemetry.addData("reg 5: range", robot.rangeBack.read(5, 1));


        telemetry.addData("jewel blue", robot.colorSensor.blue());

        telemetry.update();
    }

    @Override
    public void stop() {

    }
}