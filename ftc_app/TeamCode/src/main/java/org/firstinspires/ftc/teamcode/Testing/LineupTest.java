package org.firstinspires.ftc.teamcode.Testing;
/**
 * Created by aburur on 3/19/18.
 */

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Utility.Bot;
import org.firstinspires.ftc.teamcode.Utility.MovementEnum;
import org.firstinspires.ftc.teamcode.Utility.ReleasePosition;

@Disabled
@TeleOp(name = "LineupTest", group = "Teleop")
public class LineupTest extends OpMode {
    Bot robot = new Bot();

    boolean scaling = false;
    double power;
    int generalTarget = robot.distanceToRevsNRO20(50);

    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.jewelUp();
        robot.releaseMove(ReleasePosition.MIDDLE);
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        robot.runToPosition(generalTarget);
    }

    @Override
    public void loop() {
        if (gamepad1.left_bumper && !scaling) {
            robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        } else if (!scaling) {
            robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
        }


        if (gamepad1.a) {
            robot.backLineup(22, 2);
        } else if (gamepad1.b) {
            robot.rightLineup(66, 90, 2);
        } else if (gamepad1.x) {
            robot.leftLineup(66, 90, 2);
        } else if (gamepad1.y) {
            scaling = true;
            power = robot.slowDownScale(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
            robot.drive(MovementEnum.BACKWARD, power);
        } else {
            robot.tankDrive(gamepad1.left_stick_y, gamepad1.right_stick_y, gamepad1.left_trigger, gamepad1.right_trigger, false, false, false);
            scaling = false;
        }

        if (gamepad1.right_bumper) {
            robot.backIntakeWallDown();
            robot.releaseMove(ReleasePosition.UP);
        } else {
            robot.releaseMove(ReleasePosition.MIDDLE);
            robot.backIntakeWallUp();
        }

        telemetry.addData("left range", robot.rangeLeft.getDistance(DistanceUnit.CM));
        telemetry.addData("right range", robot.rangeRight.getDistance(DistanceUnit.CM));
        telemetry.addData("back range", robot.rangeBack.getDistance(DistanceUnit.CM));
    }


    @Override
    public void stop() {

    }
}