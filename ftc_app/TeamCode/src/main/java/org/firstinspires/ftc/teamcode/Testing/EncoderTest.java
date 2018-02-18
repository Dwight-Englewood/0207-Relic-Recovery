package org.firstinspires.ftc.teamcode.Testing;
/**
 * Created by weznon on 11/5/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Utility.Bot;

@TeleOp(name = "Encoder Test", group = "Teleop")
//@Disabled
public class EncoderTest extends OpMode {
    Bot robot = new Bot();
    int target = robot.distanceToRevs(30);
    double power;

    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        telemetry.addLine("GO");
        telemetry.update();
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
       robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {

        telemetry.addData("FL Ticks", robot.FL.getCurrentPosition());
        telemetry.addData("BL Ticks", robot.BL.getCurrentPosition());
        telemetry.addData("FR Ticks", robot.FR.getCurrentPosition());
        telemetry.addData("BR Ticks", robot.BR.getCurrentPosition());
        telemetry.addData("Lift Ticks", robot.lift.getCurrentPosition());
        telemetry.update();
    }


    @Override
    public void stop() {

    }
}
