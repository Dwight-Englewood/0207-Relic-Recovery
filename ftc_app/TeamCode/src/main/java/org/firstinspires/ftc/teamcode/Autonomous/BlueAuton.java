
package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Enums.MovementEnum;

@Autonomous(name="BlueAuton", group="Auton")
//@Disabled
public class BlueAuton extends OpMode
{
    Bot robot = new Bot();
    ElapsedTime timer;

    @Override
    public void init() {
        robot.init(hardwareMap);
        timer = new ElapsedTime();
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {

    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        robot.semiUnfoldBot();
        timer.reset();
        robot.jewelOut();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        if (timer.milliseconds() > 2000) {
            robot.drive(MovementEnum.STOP, 0);
            robot.jewelUp();
        } else if (robot.colorSensor.blue() >= 2) {
            robot.drive(MovementEnum.BACKWARD, .2);
            //robot.adjustHeading(120);
        } else if (robot.colorSensor.red() >= 2) {
            robot.drive(MovementEnum.FORWARD, .2);
            //robot.adjustHeading(60);
        }

        telemetry.addData("red", robot.colorSensor.red());
        telemetry.addData("blue", robot.colorSensor.blue());
        telemetry.addData("time", timer.milliseconds());
        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }

}
