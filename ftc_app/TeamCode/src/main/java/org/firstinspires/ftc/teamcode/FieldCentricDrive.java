package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by aburur on 9/20/17.
 */

@TeleOp(name="FieldCentricDrive", group="Example")
@Disabled
public class FieldCentricDrive extends OpMode
{
    private Bot robot = new Bot();


    @Override
    public void init() {
        robot.init(hardwareMap, telemetry);
    }

    @Override
    public void init_loop() {

        //Commented out to test the Gyro calibration OpMode
        /*if (imu.isGyroCalibrated() && cal)
        {
            telemetry.addLine("READY");
            cal = false;
        }*/
    }

    @Override
    public void start() {
        telemetry.clear();
    }

    @Override
    public void loop() {

        robot.fieldCentricDrive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
        telemetry.update();
    }

    @Override
    public void stop() {

    }

}