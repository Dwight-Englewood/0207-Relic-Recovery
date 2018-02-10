package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Utility.Bot;

/**
 * Created by aburur on 12/19/17.
 */

@TeleOp(name = "Sensor Test", group = "Testing")
@Disabled
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
        telemetry.addData("RangeBack: ", robot.rangeBack.getDistance(DistanceUnit.CM));
        telemetry.addData("RangeLeft: ", robot.rangeLeft.getDistance(DistanceUnit.CM));
        telemetry.addData("RangeRight: ", robot.rangeRight.getDistance(DistanceUnit.CM));
        telemetry.addData("intakeColor: ", robot.intakeColor.alpha());
        telemetry.addData("jewelColor: ", robot.colorSensor.alpha());
        //telemetry.addData("ods light: ", robot.ods.getLightDetected());
        //telemetry.addData("ods raw: ", robot.ods.getRawLightDetected());
        telemetry.update();
    }

    @Override
    public void stop() {

    }
}