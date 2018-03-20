package org.firstinspires.ftc.teamcode.Testing;
/**
 * Created by aburur on 3/19/18.
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utility.Bot;
import org.firstinspires.ftc.teamcode.Utility.MovementEnum;
import org.firstinspires.ftc.teamcode.Utility.ReleasePosition;


@TeleOp(name = "LineupTest", group = "Teleop")
public class LineupTest extends OpMode {
    Bot robot = new Bot();

    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.jewelUp();
        robot.releaseMove(ReleasePosition.INIT);
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            robot.backLineup(50, 4);
        } else if (gamepad1.b) {
            robot.rightLineup(66, 90, 2.5);
        } else if (gamepad1.x) {
            robot.leftLineup(66, 90, 2.5);
        } else {
            robot.drive(MovementEnum.STOP);
        }
    }

    @Override
    public void stop() {

    }
}