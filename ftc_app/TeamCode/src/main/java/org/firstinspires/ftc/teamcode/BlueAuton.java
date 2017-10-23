
package org.firstinspires.ftc.teamcode;

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

@Autonomous(name="BlueAuton", group="Auton")
//@Disabled
public class BlueAuton extends OpMode
{

    ModernRoboticsI2cColorSensor colorSensor;
    Servo servo;
    Bot robot = new Bot();
    ElapsedTime timer;
    boolean done = false;

    @Override
    public void init() {
        robot.init(hardwareMap, telemetry, true);
        colorSensor = hardwareMap.get(ModernRoboticsI2cColorSensor.class, "c");
        colorSensor.enableLed(true);

        servo = hardwareMap.get(Servo.class, "s");
        servo.setPosition(0.8);
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
        servo.setPosition(.4);
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        if (colorSensor.blue() > 2 && !done) {
            //robot.drive(-1);
            done = true;
            timer.reset();
        }
        else if (colorSensor.red() > 2 && !done)
        {
            //robot.drive(1);
            done = true;
            timer.reset();
        }
        if (timer.milliseconds() > 1000 && done); {
            //robot.drive(0);
        }
        telemetry.addData("blue", colorSensor.blue());
        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }

}
