package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
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
    Position curSensor = Position.RIGHT;
    int cooldown = 0;

    @Override
    public void init() {
        robot.init(hardwareMap);
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
        robot.setupAuton(curSensor);

        if (gamepad1.y && cooldown < 0) {
            switch (curSensor) {
                case RIGHT:
                    curSensor = Position.LEFT;
                    break;
                case LEFT:
                    curSensor = Position.RIGHT;
                    break;
            }
            cooldown = 20;
        }

        if (gamepad1.b) {
            robot.lineup(RelicRecoveryVuMark.RIGHT, telemetry);
        } else if (gamepad1.a) {
            robot.lineup(RelicRecoveryVuMark.CENTER, telemetry);
        } else if (gamepad1.x) {
            robot.lineup(RelicRecoveryVuMark.LEFT, telemetry);
        } else {
            robot.drive(MovementEnum.STOP);
            timer.reset();
        }

        cooldown--;
        telemetry.addData("back sensor: ", robot.rangeBack.getDistance(DistanceUnit.CM));
        telemetry.addData("left sensor: ", robot.rangeLeft.getDistance(DistanceUnit.CM));
        telemetry.addData("right sensor: ", robot.rangeRight.getDistance(DistanceUnit.CM));
        telemetry.addData("Time Elapsed: ", timer.milliseconds());
        telemetry.addData("cooldown? ", cooldown > 0 ? "Yep" : "Nope");
        telemetry.update();
    }


    @Override
    public void stop() {

    }
}
