package org.firstinspires.ftc.teamcode.Telebop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utility.Bot;
import org.firstinspires.ftc.teamcode.Utility.MovementEnum;

/**
 * Created by plotnw on 11/21/17.
 */

@TeleOp(name = "single user mode", group = "Teleop")
//@Disabled
public class TelebopOneController extends OpMode {
    Bot robot = new Bot();

    @Override
    public void init() {
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
    }

    @Override
    public void loop()
    {

    }

    @Override
    public void stop() {
        robot.drive(MovementEnum.STOP, 0);
    }

}