package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Utility.MovementEnum;
import org.firstinspires.ftc.teamcode.Utility.Position;

/**
 * Created by aburur on 1/16/18.
 */
@TeleOp(name = "Lineup Test", group = "Testing")
//@Disabled
public class LineupTest extends OpMode
{
    Bot robot = new Bot();
    ElapsedTime timer = new ElapsedTime();

    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.setupAuton(Position.LEFT);
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void loop() {
        if (gamepad1.b) {
            robot.lineup(Position.MIDDLE);
        } else if (gamepad1.a) {
            robot.lineup(Position.LEFT);
        } else if (gamepad1.y) {
            robot.lineup(Position.RIGHT);
        } else {
            robot.drive(MovementEnum.STOP);
        }

        telemetry.addData("back sensor: ", robot.rangeBack.getDistance(DistanceUnit.CM));
        telemetry.addData("left sensor: ", robot.rangeLeft.getDistance(DistanceUnit.CM));
        telemetry.addData("right sensor: ", robot.rangeRight.getDistance(DistanceUnit.CM));
        telemetry.update();
    }


    @Override
    public void stop() {

    }
}
