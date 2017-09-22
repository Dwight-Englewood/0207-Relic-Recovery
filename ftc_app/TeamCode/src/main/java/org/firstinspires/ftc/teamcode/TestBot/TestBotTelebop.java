package org.firstinspires.ftc.teamcode.TestBot;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by aburur on 9/14/17.
 */

    @TeleOp(name = "TestBotTelebop", group = "Teleop")
    @Disabled
    public class TestBotTelebop extends LinearOpMode {

        DistanceSensor sensorDistance1;
        DistanceSensor sensorDistance2;
        BNO055IMU imu;
        DcMotor fl, fr, bl, br;
        Servo left, right;

        @Override
        public void runOpMode() {

            //Sensors
            sensorDistance1 = hardwareMap.get(DistanceSensor.class, "sensor1");
            sensorDistance2 = hardwareMap.get(DistanceSensor.class, "sensor2");
            imu = hardwareMap.get(BNO055IMU.class, "imu");
            imu.initialize(new BNO055IMU.Parameters());

            //Motors
            fl = hardwareMap.get(DcMotor.class, "fl");
            fl.setDirection(DcMotorSimple.Direction.REVERSE);
            fr = hardwareMap.get(DcMotor.class, "fr");
            bl = hardwareMap.get(DcMotor.class, "bl");
            bl.setDirection(DcMotorSimple.Direction.REVERSE);
            br = hardwareMap.get(DcMotor.class, "br");

            left = hardwareMap.get(Servo.class, "left");
            left.setPosition(0);
            right = hardwareMap.get(Servo.class, "right");
            right.setPosition(1);

            ElapsedTime timer = new ElapsedTime();

            int x = 1;
            boolean back = false;
            boolean strafing = false;

            // wait for the start button to be pressed.
            waitForStart();

            while (opModeIsActive()) {

                if (Math.abs(gamepad1.left_stick_y) > .2 && !strafing) {
                    leftTrain(gamepad1.left_stick_y);
                }

                if (Math.abs(gamepad1.right_stick_y) > .2 && !strafing) {
                    rightTrain(gamepad1.right_stick_y);
                }

                if (gamepad1.right_trigger > .2){
                    rightStrafe(gamepad1.right_trigger);
                    strafing = true;
                }
                else if (gamepad1.left_trigger > .2){
                    leftStrafe(gamepad1.left_trigger);
                    strafing = true;
                }
                else
                    strafing = false;

                if (!strafing && Math.abs(gamepad1.left_stick_y) < .2 && Math.abs(gamepad1.right_stick_y) < .2){
                    driveForward(0);
                }

                switch (x)
                {
                    case 1:
                        if (timer.milliseconds() > 250)
                        {
                            timer.reset();
                            left.setPosition(0);
                            right.setPosition(1);
                            x++;
                            back = false;
                            break;
                        }
                    case 2:
                        if (timer.milliseconds() > 250)
                        {
                            timer.reset();
                            left.setPosition(.25);
                            right.setPosition(.75);
                            if (!back)
                                x++;
                            else
                                x--;
                        }
                        break;
                    case 3:
                        if (timer.milliseconds() > 250)
                        {
                            timer.reset();
                            left.setPosition(.5);
                            right.setPosition(.5);
                            if (!back)
                                x++;
                            else
                                x--;
                        }
                        break;
                    case 4:
                        if (timer.milliseconds() > 250)
                        {
                            timer.reset();
                            left.setPosition(.75);
                            right.setPosition(.25);
                            if (!back)
                                x++;
                            else
                                x--;
                        }
                        break;
                    case 5:
                        if (timer.milliseconds() > 250)
                        {
                            timer.reset();
                            left.setPosition(1);
                            right.setPosition(0);
                            x--;
                            back = true;
                        }
                        break;
                }



                // send the info back to driver station using telemetry function.
                telemetry.addData("distance1", sensorDistance1.getDistance(DistanceUnit.CM));
                telemetry.addData("distance2", sensorDistance2.getDistance(DistanceUnit.CM));
                telemetry.addData("imu orientation", imu.getAngularOrientation());
                telemetry.addData("imu accel", imu.getAcceleration());
                telemetry.update();
            }

                telemetry.update();
            }


    public void driveForward(double pow)
    {
        fr.setPower(pow);
        fl.setPower(pow);
        br.setPower(pow);
        bl.setPower(pow);
    }

    public void leftTrain(double pow)
    {
        fl.setPower(pow);
        bl.setPower(pow);
    }
    public void rightTrain(double pow)
    {
        fr.setPower(pow);
        br.setPower(pow);
    }

    public void leftStrafe(double pow)
    {
        fr.setPower(-pow);
        fl.setPower(pow);
        br.setPower(pow);
        bl.setPower(-pow);
    }
    public void rightStrafe(double pow)
    {
        fr.setPower(pow);
        fl.setPower(-pow);
        br.setPower(-pow);
        bl.setPower(pow);
    }

}
