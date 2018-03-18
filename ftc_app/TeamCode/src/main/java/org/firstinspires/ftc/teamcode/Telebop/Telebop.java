package org.firstinspires.ftc.teamcode.Telebop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Utility.Bot;
import org.firstinspires.ftc.teamcode.Utility.EnumController;
import org.firstinspires.ftc.teamcode.Utility.MovementEnum;
import org.firstinspires.ftc.teamcode.Utility.ReleasePosition;

/**
 * Created by aburur on 9/10/17.
 */
@TeleOp(name = "Telebop", group = "Teleop")
public class Telebop extends OpMode {

    final int cooldown = 5;
    //Creating a robot as an instance field - we need this to use later on for interacting with the robot
    Bot robot = new Bot();
    //brakeToggle is a boolean which is toggled for whether the robot is in brake mode or not
    boolean brakeToggle = false;
    boolean pingyBrakeToggle = false;
    //invert is used as a toggle for whether to invert controls or not
    boolean invert = false;
    boolean glyphMode = true;
    boolean movingInt = false;
    boolean placing = false;
    boolean holdPlace = false;
    boolean wallDown = false;
    boolean parking = false;
    //brakeCountdown is used for adding delays to the brake toggle
    int brakeCountdown = 0;
    int relicCountdown = 0;
    //wallCountdown is used for adding timing to our glyph wall
    int wallCountdown = 0;
    //controller is used for managing the position of the flipper mechanism
    //for how it works, see the class, Utility.EnumController
    EnumController<ReleasePosition> controller;
    //These doubles determine the speed at which the lift will move
    //As they are used in multiple places, rather than using "magic numbers" we define them as an instance field
    double liftScaledown = .8;
    double liftScaleup = .55;
    double relicArmPos1 = 1;
    double relicArmPos2 = 1;
    int cooldownServo2 = 0;


    /**
     * The init function handles all initialization of our robot, including fetching robot elements from the hardware map, as well as setting motor runmodes and sensor options
     */

    @Override
    public void init() {

        controller = new EnumController<>(ReleasePosition.MIDDLE);
        //Setup the robot so it will function
        robot.init(hardwareMap);

        //Reset encoders for all the drive train motors. This is important for various processes which depend on encoder ticks
        robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //Turn off the LED on the color sensor
        robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.intakeDrop.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.jewelColorBack.enableLed(false);
        robot.jewelColorForward.enableLed(false);
        telemetry.addLine("Ready.");
        telemetry.update();

    }

    @Override
    public void init_loop() {
    }

    /**
     * During the start phase, we make sure the servo has been moved back up, as it was moved down to knock the jewel during atuno
     */
    @Override
    public void start() {
        //Clear the telemetry, to make sure there isn't random stuff that is not useful
        //telemetry.clear();

        //During autonomous, we move the jewel arm down. We now move it back up to avoid having it run into things
        //By putting this in Telebop#start, the drivers are not required to manually do this each match
        robot.jewelUpTeleop();

        //We now tell the drive train motors to use encoders
        robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.intakeDrop.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    /**
     * Main loop of the teleop - where all the driver control stuff happens
     * <p>
     * alt: where the magic happens
     */
    @Override
    public void loop() {
        //invert (currently disabled)
        /*if (gamepad1.start) {
            invert = !invert;
            brakeCountdown = 15;
        }*/


        //The bumper controls the intake of glyphs, as well as adjusts the angle of the flipper mechanism
        //For more documentation of the controller object which controls the position of the flipper, see Utilites/EnumController.java

        //Gamepad 1 Stuff
        //Brake toggle. Th ebrake was implemented so the drivers could more easiyl get onto the balancing stone at the end of matches, as it will immediately halt movement of the bot
        if (gamepad1.left_bumper && brakeCountdown <= 0) {

            //switches brakeToggle to which ever boolean it was not
            //ie true -> false
            //   false -> true
            brakeToggle = !brakeToggle;
            //The drivers will always end up holding the button for more than 1 cycle of the loop function. Therefore, it is important that it doesn't immediately revert the toggle.
            //Hence, the brakeCountdown. It will prevent the toggle from accidentally not being triggered due to the boolean being swapped twice
            brakeCountdown = 50;
        }

        if (gamepad1.a && brakeCountdown <= 0) {
            pingyBrakeToggle = !pingyBrakeToggle;
            brakeCountdown = 40;
        }

        if (gamepad2.start && relicCountdown <= 0) {
            glyphMode = !glyphMode;
            relicCountdown = 80;
        }

        //Main driving function. See Bot.java for documentation
        robot.tankDrive(gamepad1.left_stick_y, gamepad1.right_stick_y, gamepad1.left_trigger > .9 ? 1 : .75 * gamepad1.left_trigger, gamepad1.right_trigger > .9 ? 1 : .75 * gamepad1.right_trigger, invert, brakeToggle, pingyBrakeToggle);

        if (robot.intakeDrop.getCurrentPosition() >= 300) {
            controller.addInstruction(ReleasePosition.DROP, 10);
            robot.backIntakeWallDown();
            wallDown = true;
            parking = true;
        } else {
            wallDown = false;
            parking = false;
        }

        if (Math.abs(robot.lift.getCurrentPosition()) >= 320) {
            robot.backIntakeWallDown();
            wallDown = true;
        } else if (!parking){
            wallDown = false;
        }

        if (glyphMode) {
            //this is the actual flipping of the flipper
            //need some stuff here for wallCountdown
            if (gamepad1.right_bumper && !placing && !holdPlace) {
                placing = true;
                robot.backIntakeWallDown();
                wallCountdown = 5;
            } else if (wallCountdown <= 0 && !placing) {
                controller.addInstruction(ReleasePosition.MIDDLE, 0);
                if (!wallDown) {
                    robot.backIntakeWallUp();
                }
                holdPlace = false;
            }

            if ((placing && wallCountdown <= 0) || holdPlace) {
                if (gamepad1.right_bumper) {
                    //This is priority 5 as we want the actual flipping (placing the glyph) to have precedence over other auto done positions, which only serve to aid in glyph movement.
                    //the intake wall is to ensure that glyphs dont fall out during normal driving. However, it must be moved down in order to place glyphs
                    controller.addInstruction(ReleasePosition.UP, 5);
                    robot.flipUp();
                    wallCountdown = 40;
                    holdPlace = true;
                }
                placing = false;
            }

            //Gamepad 2 Stuff
            if (gamepad2.right_bumper) {
                //Specific to the teleop, we have 3 levels of priority
                //A regular change in the position is 1 - these are the standard change
                controller.addInstruction(ReleasePosition.DOWN, 1);
                robot.intake(.7);
            } else {
                if (gamepad2.right_trigger > .2 || gamepad2.left_trigger > .2) {
                    controller.addInstruction(ReleasePosition.DOWN, 1);
                    //robot.intake(-1);
                    robot.intake(-.95);
                } else {
                    //This line is not needed, as this specific addition to the controller object will never change the output. However, it is included to keep clarity as to what will happen
                    //The zero priority will not change the result of process, as priority is seeded at 0 - and is strictly increasing. This is equivalent to a blank statement, which we use to keep code clarity
                    controller.addInstruction(ReleasePosition.MIDDLE, 0);
                    robot.intake(0);
                }
            }

            //mini flipper mechanism control. this mini flipper mechanism is used to make sure glyphs are properly aligned into the main flipper mechanism
            if (gamepad2.b) {
                robot.flipUp();
            } else if (!gamepad1.right_bumper) {
                robot.flipDown();
            }

            //controls the linear slide mechanism to allow for placing of glyphs above row 2
            if (gamepad2.left_stick_y < -.15) {
                controller.addInstruction(ReleasePosition.MIDDLEUP, 1);
                robot.lift.setPower(gamepad2.left_stick_y * liftScaleup);
            } else if (gamepad2.left_stick_y > .15) {
                controller.addInstruction(ReleasePosition.MIDDLEUP, 1);

                robot.lift.setPower(gamepad2.left_stick_y * liftScaledown);
            } else {
                controller.addInstruction(ReleasePosition.MIDDLE, 0);
                robot.lift.setPower(0);
            }
        } else {
            robot.lift.setPower(0);
            if (gamepad2.a) {
                robot.relicArmVexControl(.8, DcMotorSimple.Direction.REVERSE);
            } else if (gamepad2.y) {
                robot.relicArmVexControl(.8, DcMotorSimple.Direction.FORWARD);
            } else {
                robot.relicArmVexControl(0, DcMotorSimple.Direction.FORWARD);
            }


            if (gamepad2.left_trigger > 0.1) {
                relicArmPos1 = .35;
            } else if (gamepad2.left_bumper) {
                relicArmPos1 = 0;
            }


            if (cooldownServo2 <= 0) {
                if (gamepad2.right_trigger > .1) {
                    relicArmPos2 += .1;
                    cooldownServo2 = cooldown;

                } else if (gamepad2.right_bumper) {
                    relicArmPos2 -= .1;
                    cooldownServo2 = cooldown;
                }
            }
        }

        if (gamepad2.x) {
            robot.jewelTeleop();
        } else if (!movingInt) {
            robot.jewelUpTeleop();
        }

        //Our intake is put on a motor which allows it to be raised or lowered. This section allows for the drivers to raise it during matches, to reach glyphs which are on top of other ones
        if (gamepad2.right_stick_y > .3) {
            robot.intakeDrop.setPower(-1);
            robot.intake(.5);
            robot.jewelOut();
            //priority 6 since if this doesnt happen the robot goes boom
            controller.addInstruction(ReleasePosition.DROP, 10);
            movingInt = true;
        } else if (gamepad2.right_stick_y < -.3) {
            robot.intakeDrop.setPower(1);
            //priority 6 since if this doesnt happen the robot goes boom
            controller.addInstruction(ReleasePosition.DROP, 10);
            robot.intake(.5);
            robot.jewelOut();
            movingInt = true;
        } else if (!gamepad2.x && !(gamepad2.left_trigger > .1 || gamepad2.right_trigger > .1 || gamepad2.right_bumper)) {
            robot.intakeDrop.setPower(0);
            robot.intake(0);
            robot.jewelUpTeleop();
            movingInt = false;
        } else if (!(gamepad2.left_trigger > .1 || gamepad2.right_trigger > .1 || gamepad2.right_bumper)){
            movingInt = false;
            robot.intakeDrop.setPower(0);
            robot.intake(0);
        }

        //Decrement the counters
        brakeCountdown--;
        relicCountdown--;
        wallCountdown--;
        cooldownServo2--;

        //process the values added to the controller - the controller doesnt help if we never get the values out of it

        relicArmPos1 = Range.clip(relicArmPos1, 0, 1);
        relicArmPos2 = Range.clip(relicArmPos2, 0, 1);
        robot.releaseMove(controller.process());
        robot.relicArmServo1.setPosition(relicArmPos1);
        robot.relicArmServo2.setPosition(relicArmPos2);
        controller.reset();

        //Telemetry things, generally booleans that could be important for drivers to be able to tell are active, as well as cooldowns
        telemetry.addData("Braking", brakeToggle);
        telemetry.addData("Alt Mode?", !glyphMode);
        telemetry.addData("PingyBraking?", pingyBrakeToggle);
        telemetry.update();

    }

    @Override
    public void stop() {
        robot.drive(MovementEnum.STOP, 0);
    }

}
