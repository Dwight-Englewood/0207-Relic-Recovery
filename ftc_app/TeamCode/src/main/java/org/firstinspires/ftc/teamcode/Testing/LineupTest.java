package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Utility.MovementEnum;
import org.firstinspires.ftc.teamcode.Utility.ReleasePosition;

/**
 * Created by aburur on 1/16/18.
 */
@TeleOp(name = "Lineup Still Adjust Test", group = "Testing")
@Disabled
public class LineupTest extends OpMode
{
    Bot robot = new Bot();
    boolean aligned = false;
    int command = 0;
    ElapsedTime timer = new ElapsedTime();

    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.jewelUp();
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

        if (gamepad1.a) {
            command = 0;
            timer.reset();
            aligned = false;
        }

        if (aligned) {
            robot.releaseMove(ReleasePosition.DROP);
            robot.drive(MovementEnum.STOP);
        }

        switch (command) {
            case 0:
                if (timer.milliseconds() > 1500) {
                    robot.drive(MovementEnum.STOP);
                    command++;
                    timer.reset();
                } else {
                    robot.adjustHeading(0, false, telemetry);
                    aligned = false;
                }
                break;

            case 1:
                double currentDistance = robot.rangeRight.getDistance(DistanceUnit.CM);
                if (Math.abs(currentDistance - 119) < 2) {
                    robot.drive(MovementEnum.STOP);
                    timer.reset();
                    command++;
                } else if (currentDistance < 119) {
                    robot.drive(MovementEnum.LEFTSTRAFE, .3);
                } else {
                    robot.drive(MovementEnum.RIGHTSTRAFE, .1);
                }
                break;

            case 2:
                if (timer.milliseconds() > 1500) {
                    robot.drive(MovementEnum.STOP);
                    command++;
                    timer.reset();
                } else {
                    robot.adjustHeading(0, false, telemetry);
                }
                break;

            case 3:
                currentDistance = robot.rangeBack.getDistance(DistanceUnit.CM);
                if (Math.abs(currentDistance - 37) < 2) {
                    robot.drive(MovementEnum.STOP);
                    timer.reset();
                    command++;
                } else if (currentDistance < 37) {
                    robot.drive(MovementEnum.FORWARD, .1);
                } else {
                    robot.drive(MovementEnum.BACKWARD, .3);
                }
                break;

            case 4:
                if (timer.milliseconds() > 1000) {
                    robot.drive(MovementEnum.STOP);
                    command++;
                    timer.reset();
                    aligned = true;
                } else {
                    robot.adjustHeading(0, false, telemetry);
                }
                break;

        }

        telemetry.addData("Aligned: ", aligned);
        telemetry.update();
    }


    @Override
    public void stop() {

    }
}
