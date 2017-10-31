package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Enums.MovementEnum;

/**
 * Created by aburur on 9/10/17.
 */
@TeleOp(name="Telebop", group="Teleop")
public class Telebop extends OpMode
{
        Bot robot = new Bot();

        @Override
        public void init() {
            robot.init(hardwareMap, telemetry);
        }

        @Override
        public void init_loop() {
        }

        @Override
        public void start() {
            telemetry.clear();
        }

        @Override
        public void loop() {
            robot.fieldCentricDrive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

            if (gamepad1.right_trigger > .5) {
                robot.intake(1);
            }
            else {
                robot.intake(0);
            }

            if (gamepad1.a){
                robot.intakeBrake.setPower(.3);
            }
            else {
                robot.intakeBrake.setPower(0);
            }

        }

        @Override
        public void stop() {
            robot.drive(MovementEnum.STOP, 0);
        }

}