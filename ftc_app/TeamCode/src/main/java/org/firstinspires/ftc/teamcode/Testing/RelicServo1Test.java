package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Utility.Bot;

/**
 * Created by aburur on 12/19/17.
 */

@TeleOp(name = "Relic Servo 1 Test", group = "Testing")
//@Disabled
public class RelicServo1Test extends OpMode {
    Bot robot = new Bot();
    double val = .5;
    int cooldown = 0;

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
        if (cooldown < 0) {
            if (gamepad1.x) {
                val += .02;
                cooldown = 10;
            } else if (gamepad1.b) {
                val -= .02;
                cooldown = 10;
            }
        } else {
            cooldown--;
        }

        val = Range.clip(val, 0, 1);

        robot.relicArmServo1.setPosition(val);

        telemetry.addData("value", val);
        telemetry.update();
    }

    @Override
    public void stop() {

    }
}