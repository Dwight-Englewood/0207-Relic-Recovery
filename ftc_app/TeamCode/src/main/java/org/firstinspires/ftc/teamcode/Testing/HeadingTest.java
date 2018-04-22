package org.firstinspires.ftc.teamcode.Testing;
/**
 * Created by weznon on 11/5/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Utility.Bot;

@TeleOp(name = "HeadingTest", group = "Testing")
//@Disabled
public class HeadingTest extends OpMode {
    Bot robot = new Bot();
    ElapsedTime timer = new ElapsedTime();
    int target = 0, countdown;

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
       if (gamepad1.a && countdown <= 0) {
           target += 1;
           countdown = 5;
       } else if (gamepad1.b && countdown <= 0) {
           target -= 1;
           countdown = 5;
       }

       if (gamepad1.right_trigger > .15) {
           robot.adjustHeading(target, gamepad1.left_trigger > .15);
       }

       countdown--;
       telemetry.addData("heading", robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
       telemetry.addData("target", target);
       telemetry.update();
    }


    @Override
    public void stop() {

    }
}
