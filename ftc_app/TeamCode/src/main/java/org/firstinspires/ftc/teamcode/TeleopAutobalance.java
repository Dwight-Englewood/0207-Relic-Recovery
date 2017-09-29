package org.firstinspires.ftc.teamcode;
/**
 * Created by weznon on 9/29/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TeleopAutobalance", group = "Teleop")
public class TeleopAutobalance extends OpMode {

    private Bot robot = new Bot();

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        robot.init(hardwareMap, telemetry);
        telemetry.update();
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
