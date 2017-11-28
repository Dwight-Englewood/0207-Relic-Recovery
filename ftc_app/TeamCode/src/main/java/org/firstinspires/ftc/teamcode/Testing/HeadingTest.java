package org.firstinspires.ftc.teamcode.Testing;
/**
 * Created by weznon on 11/5/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Bot;


@TeleOp(name = "HeadingTest", group = "Teleop")
public class HeadingTest extends OpMode {
    Bot robot = new Bot();
    ElapsedTime timer = new ElapsedTime();

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
        if (timer.milliseconds() < 5000)
            robot.adjustHeading(180);
        else if (timer.milliseconds() < 10000)
            robot.adjustHeading(90);
        else if (timer.milliseconds() < 15000)
            robot.adjustHeading(-90);
        else
            robot.adjustHeading(0);

        telemetry.addData("heading", robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
        telemetry.addData("time", timer.milliseconds());
        telemetry.update();
    }


    @Override
    public void stop() {

    }
}
