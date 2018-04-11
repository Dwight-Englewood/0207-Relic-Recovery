package org.firstinspires.ftc.teamcode.Telebop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utility.Bot;


@TeleOp(name = "Worlds_Telebop", group = "Teleop")
public class Worlds_Telebop extends OpMode {

    Bot robot = new Bot();
    final double liftScaledown = .9;
    final double liftScaleup = .75;

    @Override
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

    }

    @Override
    public void stop() {

    }
}