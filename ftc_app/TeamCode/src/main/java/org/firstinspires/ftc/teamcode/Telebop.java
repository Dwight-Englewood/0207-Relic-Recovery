package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Utility.EnumController;
import org.firstinspires.ftc.teamcode.Utility.MovementEnum;
import org.firstinspires.ftc.teamcode.Utility.ReleasePosition;

/**
 * Created by aburur on 9/10/17.
 */
@TeleOp(name = "Telebop", group = "Teleop")
public class Telebop extends OpMode {
    Bot robot = new Bot();
    boolean brakeToggle = false;

    int countdown = 0;
    int wallCountdown = 0;

    EnumController<ReleasePosition> controller = new EnumController<>(ReleasePosition.MIDDLE);

    boolean invert = false;

    double liftScaledown = .7;
    double liftScaleup = .4;

    /**
     * The init function handles all initialization of our robot, including fetching robot elements from the hardware map, as well as setting motor runmodes and sensor options
     */

    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.colorSensor.enableLed(false);
    }

    @Override
    public void init_loop() {}

    /**
     * During the start phase, we make sure the jewel servo has been moved back up, as it was moved down to knock the jewel during atuno
     */
    @Override
    public void start() {
        telemetry.clear();
        robot.jewelUp();
        robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    /**
     * Main loop of the teleop - where all the driver control stuff happens
     *
     * alt: where the magic happens
     */
    @Override
    public void loop() {
        //Brake toggle. Th ebrake was implemented so the drivers could more easiyl get onto the balancing stone at the end of matches, as it will immediately halt movement of the bot
        if (gamepad1.right_bumper && countdown <= 0) {
            brakeToggle = !brakeToggle;
            //The drivers will always end up holding the button for more than 1 cycle of the loop function. Therefore, it is important that it doesn't immediately revert the toggle. 
            //Hence, the coutdown. It will prevent the toggle from accidentaly not being triggered due to the boolean being swapped twice
            countdown = 5;
        }

        //Main driving function. See Bot.java for documentation
        robot.tankDrive(gamepad1.left_stick_y, gamepad1.right_stick_y, gamepad1.left_trigger, gamepad1.right_trigger, invert, brakeToggle); // Tank drive???


        //invert (durrently disabled)
        /*
        if (gamepad1.left_bumper && countdown <= 0) {
            //i = i ? false : true;
            countdown = 30;
        }
        */

        //relic stuff (currently disabled)
        /*
        if (gamepad2.dpad_up) {
            robot.relicArmServo.setPower(.5);
        } else if (gamepad2.dpad_down) {
            robot.relicArmServo.setPower(-.5);
        } else {
            robot.relicArmServo.setPower(0);
        }

        if (gamepad2.dpad_right) {
            robot.relicArmINNOUT.setPower(.5);
        } else if (gamepad2.dpad_left) {
            robot.relicArmINNOUT.setPower(-.5);
        } else {
            robot.relicArmINNOUT.setPower(0);
        }

        if (gamepad1.right_trigger > .3) {
            robot.relicArmVex.setPower(.5);
        } else if (gamepad1.left_trigger > .3) {
            robot.relicArmVex.setPower(-.5);
        } else {
            robot.relicArmVex.setPower(0);
        }
        */
        //The bumper controls the intake of glyphs, as well as adjusts the angle of the flipper mechanism
        //For more documentation of the controller object which controls the position of the flipper, see Utilites/EnumController.java
        if (gamepad2.right_bumper) {
            controller.addInstruction(ReleasePosition.DOWN, 1);
            robot.intake(1);
        } else if (gamepad2.right_trigger > .3) {
            controller.addInstruction(ReleasePosition.DOWN, 1);
            robot.intake(-1);
        } else {
            //This line is not needed, as this specific addition to the controller object will never change the output. However, it is included to keep clarity as to what will happen
            controller.addInstruction(ReleasePosition.MIDDLE, 0);
            robot.intake(0);
        }
        
        //Our intake is put on a motor which allows it to be raised or lowered. This section allows for the drivers to raise it during matches, to reach glyphs which are on top of other ones
        if (gamepad2.right_stick_y > .3) {
            robot.intakeDrop.setPower(-1);
        } else if (gamepad2.right_stick_y < -.3) {
            robot.intakeDrop.setPower(1);
        } else {
            robot.intakeDrop.setPower(0);
        }
        
        //mini flipper mechanism control. this mini flipper mechanism is used to make sure glyphs are properly aligned into the main flipper mechanism
        if (gamepad2.b) {
            robot.flipUp();
        } else if (!gamepad2.y) {
            robot.flipDown();
        }

        //controls the linear slide mechanism, to allow for placing of glpyhs above row 2 
        if (gamepad2.left_stick_y > .15) {
            controller.addInstruction(ReleasePosition.MIDDLEUP, 1);

            robot.lift.setPower(gamepad2.left_stick_y * liftScaleup);
        } else if (gamepad2.left_stick_y < -.15) {
            controller.addInstruction(ReleasePosition.MIDDLEUP, 1);

            robot.lift.setPower(gamepad2.left_stick_y * liftScaledown);
        } else {
            controller.addInstruction(ReleasePosition.MIDDLE, 0);
            robot.lift.setPower(0);
        }
        
        //this is the actual flipping of the flipper
        //need some stuff here for wallCountdown
        if (gamepad2.y) {
            controller.addInstruction(ReleasePosition.UP, 5); //The priority on this controller position is higher than all others, as it must take precedence (needs to be expanded, possibly in the better doc in object)
            robot.flipUp();
            robot.backIntakeWallDown(); //the intake wall is to ensure that glyphs dont fall out during normal driving. However, it must be moved down in order to place glyphs
            wallCountdown = 40;
        } else if (wallCountdown <= 0) {
            controller.addInstruction(ReleasePosition.MIDDLE, 0);
            robot.backIntakeWallUp();
        }

        if (gamepad2.x) {
            robot.jewelServoBottom.setPosition(.3);
        } else {
            robot.jewelUp();
        }
        
        //Decrement the counters
        countdown--;
        wallCountdown--;
        
        //process the values added to the controller - the controller doesnt help if we never get the values out of it
        robot.releaseMove(controller.process());
        controller.reset();

        //Telemetry things, generally booleans that could be important for drivers to be able to tell are active, as well as cooldowns
        telemetry.addData("Braking", brakeToggle);
        telemetry.addData("Brake cooldown? ", countdown > 0 ? "Yep" : "Nope");
        telemetry.addData("Wall on cooldown? ", wallCountdown > 0 ? "Yep" : "Nope");
        telemetry.update();
    }

    @Override
    public void stop() {
        robot.drive(MovementEnum.STOP, 0);
        robot.releaseLeft.close();
        robot.releaseRight.close();
        robot.jewelServoBottom.close();
        robot.jewelServoTop.close();
        robot.flipper.close();
        robot.backIntakeWall.close();
    }

}
