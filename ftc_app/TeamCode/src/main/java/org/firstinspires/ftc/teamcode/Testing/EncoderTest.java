package org.firstinspires.ftc.teamcode.Testing;
/**
 * Created by weznon on 11/5/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Enums.MovementEnum;

@TeleOp(name = "Encoder Test", group = "Teleop")
public class EncoderTest extends OpMode {
    Bot robot = new Bot();

    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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

        if (gamepad1.b)
            robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        if (gamepad1.a)
            robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);

        if (gamepad1.dpad_up)
            robot.drive(MovementEnum.FORWARD, .2);
        if (gamepad1.dpad_down)
            robot.drive(MovementEnum.BACKWARD, .2);
        if (gamepad1.dpad_left)
            robot.drive(MovementEnum.LEFTSTRAFE, .5);
        if (gamepad1.dpad_right)
            robot.drive(MovementEnum.RIGHTSTRAFE, .5);


        telemetry.addData("FL Ticks", robot.FL.getCurrentPosition());
        telemetry.addData("BL Ticks", robot.BL.getCurrentPosition());
        telemetry.addData("FR Ticks", robot.FR.getCurrentPosition());
        telemetry.addData("BR Ticks", robot.BR.getCurrentPosition());
        telemetry.update();
    }


    @Override
    public void stop() {

    }
}
