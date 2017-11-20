package org.firstinspires.ftc.teamcode.Autonomous;

/**
 * Created by aburur on 11/18/17.
 */


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Enums.MovementEnum;

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


@Autonomous(name="PlantAuton", group="Auton")
//@Disabled
public class PlantAuton extends OpMode
{
    Bot robot = new Bot();
    int command = 1;
    int target;
    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        switch (command){
            case 1:
                target = robot.distanceToRevs(40);
                robot.runToPosition(target, .5);
                command++;
                break;
            case 2:
                 if (Math.abs(target - robot.BL.getCurrentPosition()) < 100 &&
                     Math.abs(target - robot.BR.getCurrentPosition()) < 100 &&
                     Math.abs(target - robot.FL.getCurrentPosition()) < 100 &&
                     Math.abs(target - robot.FR.getCurrentPosition()) < 100
                    ) { robot.drive(MovementEnum.STOP, 0); command++; }
                    break;
        }
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }

}

