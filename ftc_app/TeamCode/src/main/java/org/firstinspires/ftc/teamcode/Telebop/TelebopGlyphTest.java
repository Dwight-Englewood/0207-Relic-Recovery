package org.firstinspires.ftc.teamcode.Telebop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Utility.Bot;
import org.firstinspires.ftc.teamcode.Utility.EnumController;
import org.firstinspires.ftc.teamcode.Utility.InfiniteImprobabilityDrive.Cryptobox;
import org.firstinspires.ftc.teamcode.Utility.InfiniteImprobabilityDrive.Glyph;
import org.firstinspires.ftc.teamcode.Utility.InfiniteImprobabilityDrive.GlyphPlace;
import org.firstinspires.ftc.teamcode.Utility.MovementEnum;
import org.firstinspires.ftc.teamcode.Utility.ReleasePosition;
import org.firstinspires.ftc.teamcode.Utility.Tuple;

import java.util.ArrayList;

/**
 * Created by aburur on 9/10/17.
 */
@TeleOp(name = "Telebop", group = "Teleop")
public class TelebopGlyphTest extends OpMode {

    final int cooldown = 5;
    //Creating a robot as an instance field - we need this to use later on for interacting with the robot
    Bot robot = new Bot();
    //brakeToggle is a boolean which is toggled for whether the robot is in brake mode or not
    boolean brakeToggle = false;
    //invert is used as a toggle for whether to invert controls or not
    boolean invert = false;
    boolean glyphMode = true;
    boolean movingInt = false;
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
    double liftScaledown = .7;
    double liftScaleup = .4;
    double relicArmPos1 = 1;
    double relicArmPos2 = 1;
    int cooldownServo1 = 0;
    int cooldownServo2 = 0;


    boolean intaking = false;

    Cryptobox pattern;

    GlyphPlace currentPlace;
    GlyphPlace nextPlace;
    private double average;
    private int loops;

    static final int doubleGlyphReading = 20;
    static ArrayList<Glyph>  intakeReadings = new ArrayList<>();

    /**
     * The init function handles all initialization of our robot, including fetching robot elements from the hardware map, as well as setting motor runmodes and sensor options
     */

    public static void main(String[] args) {
        for (int i = 0; i < 11; i++) {
            intakeReadings.add(Glyph.BROWN);
        }
        for (int i = 0; i < 11; i++) {
            intakeReadings.add(Glyph.GRAY);
        }
        for (int i = 0; i < intakeReadings.size(); i++) {
            if (intakeReadings.get(i).equals(Glyph.EMPTY)) {
                intakeReadings.remove(i);
                i--;
            } else {
                break;
            }
        }
        for (int i = intakeReadings.size() - 1; i > 0; i--) {
            if (intakeReadings.get(i).equals(Glyph.EMPTY)) {
                intakeReadings.remove(i);
                i++;
            } else {
                break;
            }
        }

        double amountBrown = 0;
        double amountGray = 0;

        double frontBrown = 0;
        double frontGray = 0;

        int nonEmpty = 0;

        int midPoint = intakeReadings.size() / 2;
        for (int i = 0; i < intakeReadings.size(); i++) {
            if (intakeReadings.get(i).equals(Glyph.BROWN)) {
                amountBrown++;
                nonEmpty++;
                if (i < midPoint) {
                    frontBrown = frontBrown + 1;
                }
            } else if (intakeReadings.get(i).equals(Glyph.GRAY)) {
                amountGray++;
                nonEmpty++;
                if (i < midPoint) {
                    frontGray = frontGray + 1;
                }
            }
        }
        Glyph glyph1;
        Glyph glyph2;
        if (Math.abs(frontBrown / nonEmpty - .5) < Math.abs(frontBrown - 1) && Math.abs(frontBrown / nonEmpty - .5) < Math.abs(frontBrown - 0)) {
            //this means even distribution of brown over all readings, aka we got only a brown one
            if (intakeReadings.size() > doubleGlyphReading) {
                glyph1 = Glyph.BROWN;
                glyph2 = Glyph.BROWN;
            }
        } else if (Math.abs(frontGray / nonEmpty - .5) < Math.abs(frontGray - 1) && Math.abs(frontGray / nonEmpty - .5) < Math.abs(frontGray - 0)) {
            //this means even distribution of brown over all readings, aka we got only a gray one
            if (intakeReadings.size() > doubleGlyphReading) {
                glyph1 = Glyph.GRAY;
                glyph2 = Glyph.GRAY;
            }
        } else {
            glyph1 = Glyph.EMPTY;
            glyph2 = Glyph.EMPTY;
        }

        if (Math.abs(frontBrown / nonEmpty - 1) > Math.abs(frontGray / nonEmpty - 1)) {
            glyph1 = Glyph.BROWN;
            glyph2 = Glyph.GRAY;
        } else if ((Math.abs(frontBrown / nonEmpty - 1) <= Math.abs(frontGray / nonEmpty - 1))) {
            glyph1 = Glyph.GRAY;
            glyph2 = Glyph.BROWN;
        } else {
            glyph1 = Glyph.EMPTY;
            glyph2 = Glyph.EMPTY;
        }

        System.out.println(glyph1);
        System.out.println(glyph2);
    }
    @Override
    public void init() {

        controller = new EnumController<>(ReleasePosition.MIDDLE);
        //Setup  the robot so it will function
        robot.init(hardwareMap);

        //Reset encoders for all the drive train motors. This is important for various processes which depend on encoder ticks
        robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //Turn off the LED on the color sensor
        robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.colorSensor.enableLed(true);
        telemetry.addLine("Ready.");
        telemetry.update();

        //TODO REASD STUIFF FROM AUTON
        this.pattern = new Cryptobox(new Glyph[3][4]);
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
        robot.jewelUp();

        //We now tell the drive train motors to use encoders
        robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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
            brakeCountdown = 40;
        }

        if (gamepad2.start && relicCountdown <= 0) {
            glyphMode = !glyphMode;
            relicCountdown = 60;
        }

        //Main driving function. See Bot.java for documentation
        robot.tankDrive(gamepad1.left_stick_y, gamepad1.right_stick_y, gamepad1.left_trigger, gamepad1.right_trigger, invert, brakeToggle); // Tank drive???

        if (glyphMode) {
            //this is the actual flipping of the flipper
            //need some stuff here for wallCountdown
            if (gamepad1.right_bumper) {
                //This is priority 5 as we want the actual flipping (placing the glyph) to have precedence over other auto done positions, which only serve to aid in glyph movement.
                controller.addInstruction(ReleasePosition.UP, 5);
                robot.flipUp();
                //the intake wall is to ensure that glyphs dont fall out during normal driving. However, it must be moved down in order to place glyphs
                robot.backIntakeWallDown();
                wallCountdown = 55;
            } else if (wallCountdown <= 0) {
                controller.addInstruction(ReleasePosition.MIDDLE, 0);
                robot.backIntakeWallUp();
            }

            //Gamepad 2 Stuff
            if (gamepad2.right_bumper) {
                //Specific to the teleop, we have 3 levels of priority
                //A regular change in the position is 1 - these are the standard change
                controller.addInstruction(ReleasePosition.DOWN, 1);
                robot.intake(1);
            } else {
                if (gamepad2.right_trigger > .2 || gamepad2.left_trigger > .2) {
                    controller.addInstruction(ReleasePosition.DOWN, 1);
                    //robot.intake(-1);
                    robot.intakeOne.setPower(-.9 * gamepad2.right_trigger);
                    robot.intakeTwo.setPower(-.9 * gamepad2.left_trigger);
                    intaking = true;
                } else {
                    //This line is not needed, as this specific addition to the controller object will never change the output. However, it is included to keep clarity as to what will happen
                    //The zero priority will not change the result of process, as priority is seeded at 0 - and is strictly increasing. This is equivalent to a blank statement, which we use to keep code clarity
                    controller.addInstruction(ReleasePosition.MIDDLE, 0);
                    robot.intake(0);
                    intaking = false;
                }
            }

            //Our intake is put on a motor which allows it to be raised or lowered. This section allows for the drivers to raise it during matches, to reach glyphs which are on top of other ones
            if (gamepad2.right_stick_y > .3) {
                robot.intakeDrop.setPower(-1);
                robot.jewelOut();
                //priority 6 since if this doesnt happen the robot goes boom
                controller.addInstruction(ReleasePosition.INIT, 10);
                movingInt = true;
            } else if (gamepad2.right_stick_y < -.3) {
                robot.intakeDrop.setPower(1);
                //priority 6 since if this doesnt happen the robot goes boom
                controller.addInstruction(ReleasePosition.INIT, 10);
                robot.jewelOut();
                movingInt = true;
            } else if (!gamepad2.x) {
                robot.intakeDrop.setPower(0);
                robot.jewelUp();
                movingInt = false;
            } else {
                movingInt = false;
                robot.intakeDrop.setPower(0);
            }

            //mini flipper mechanism control. this mini flipper mechanism is used to make sure glyphs are properly aligned into the main flipper mechanism
            if (gamepad2.b) {
                robot.flipUp();
            } else if (!gamepad1.right_bumper) {
                robot.flipDown();
            }

            //controls the linear slide mechanism, to allow for placing of glyphs above row 2
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
            if (gamepad2.a) {
                robot.relicArmVexControl(.5, DcMotorSimple.Direction.REVERSE);
            } else if (gamepad2.y) {
                robot.relicArmVexControl(.5, DcMotorSimple.Direction.FORWARD);
            } else {
                robot.relicArmVexControl(0, DcMotorSimple.Direction.FORWARD);
            }

            if (cooldownServo1 <= 0) {
                if (gamepad2.left_trigger > 0.1) {
                    relicArmPos1 += .02;
                    cooldownServo1 = cooldown;
                } else if (gamepad2.left_bumper) {
                    relicArmPos1 -= .02;
                    cooldownServo1 = cooldown;
                }
            }

            if (cooldownServo2 <= 0) {
                if (gamepad2.right_trigger > .1) {
                    relicArmPos2 += .05;
                    cooldownServo2 = cooldown;

                } else if (gamepad2.right_bumper) {
                    relicArmPos2 -= .05;
                    cooldownServo2 = cooldown;
                }
            }
        }

        if (gamepad2.x) {
            robot.jewelTeleop();
        } else if (!movingInt) {
            robot.jewelUp();
        }

        //Decrement the counters
        brakeCountdown--;
        relicCountdown--;
        wallCountdown--;
        cooldownServo1--;
        cooldownServo2--;

        if (cooldownServo1 == Integer.MIN_VALUE) {
            cooldownServo1 = 0;
        }

        if (cooldownServo2 == Integer.MIN_VALUE) {
            cooldownServo2 = 0;
        }

        //process the values added to the controller - the controller doesnt help if we never get the values out of it

        relicArmPos1 = Range.clip(relicArmPos1, 0, 1);
        relicArmPos2 = Range.clip(relicArmPos2, 0, 1);
        robot.releaseMove(controller.process());
        robot.relicArmServo1.setPosition(relicArmPos1);
        robot.relicArmServo2.setPosition(relicArmPos2);
        controller.reset();


        //this can be written to remove the extra loops to get rid of the empties

        //TODO
        if (intaking) {
            Glyph merp = robot.findGlyphType();
            intakeReadings.add(merp);
        } else if (intakeReadings.size() != 0) {
            //remove leading and trailing empty values - makes it easier to determine the glyphs and stuff
            for (int i = 0; i < intakeReadings.size(); i++) {
                if (intakeReadings.get(i).equals(Glyph.EMPTY)) {
                    intakeReadings.remove(i);
                    i--;
                } else {
                    break;
                }
            }
            for (int i = intakeReadings.size() - 1; i > 0; i--) {
                if (intakeReadings.get(i).equals(Glyph.EMPTY)) {
                    intakeReadings.remove(i);
                    i++;
                } else {
                    break;
                }
            }

            double amountBrown = 0;
            double amountGray = 0;

            double frontBrown = 0;
            double frontGray = 0;

            int nonEmpty = 0;

            int midPoint = intakeReadings.size() / 2;
            for (int i = 0; i < intakeReadings.size(); i++) {
                if (intakeReadings.get(i).equals(Glyph.BROWN)) {
                    amountBrown++;
                    nonEmpty++;
                    if (i < midPoint) {
                        frontBrown = frontBrown + 1;
                    }
                } else if (intakeReadings.get(i).equals(Glyph.GRAY)) {
                    amountGray++;
                    nonEmpty++;
                    if (i < midPoint) {
                        frontGray = frontGray + 1;
                    }
                }
            }
            Glyph glyph1;
            Glyph glyph2;
            if (Math.abs(frontBrown / nonEmpty - .5) < Math.abs(frontBrown - 1) && Math.abs(frontBrown / nonEmpty - .5) < Math.abs(frontBrown - 0)) {
                //this means even distribution of brown over all readings, aka we got only a brown one
                if (intakeReadings.size() > doubleGlyphReading) {
                    glyph1 = Glyph.BROWN;
                    glyph2 = Glyph.BROWN;
                }
            } else if (Math.abs(frontGray / nonEmpty - .5) < Math.abs(frontGray - 1) && Math.abs(frontGray / nonEmpty - .5) < Math.abs(frontGray - 0)) {
                //this means even distribution of brown over all readings, aka we got only a gray one
                if (intakeReadings.size() > doubleGlyphReading) {
                    glyph1 = Glyph.GRAY;
                    glyph2 = Glyph.GRAY;
                }
            } else {
                glyph1 = Glyph.EMPTY;
                glyph2 = Glyph.EMPTY;
            }

            if (Math.abs(frontBrown / nonEmpty - 1) > Math.abs(frontGray / nonEmpty - 1)) {
                glyph1 = Glyph.BROWN;
                glyph2 = Glyph.GRAY;
            } else if ((Math.abs(frontBrown / nonEmpty - 1) <= Math.abs(frontGray / nonEmpty - 1))) {
                glyph1 = Glyph.GRAY;
                glyph2 = Glyph.BROWN;
            } else {
                glyph1 = Glyph.EMPTY;
                glyph2 = Glyph.EMPTY;
            }

            //currentPlace = pattern.findBestPlacement(glyph1, glyph2);
            pattern.placeGlyphPlace(currentPlace);
            nextPlace = pattern.whatGlyphsToGoFor(.5, .5, false).snd;

            intakeReadings.clear();
        }



        double distanceCM = robot.intakeDistance.getDistance(DistanceUnit.CM);

        double compAlphaVal = -10 * distanceCM + 150;
        //Telemetry things, generally booleans that could be important for drivers to be able to tell are active, as well as cooldowns
        telemetry.addData("Braking", brakeToggle);
        telemetry.addData("Alt Mode?", !glyphMode);
        telemetry.addData("Invert?", invert);
        telemetry.addData("glypType", robot.findGlyphType());
        if (gamepad2.dpad_up) {

            average = average + robot.intakeDistance.getDistance(DistanceUnit.CM);
            loops++;


            telemetry.addData("csalpha", robot.intakeColor.alpha());
            telemetry.addData("csred", robot.intakeColor.red());
            telemetry.addData("csgreen", robot.intakeColor.green());
            telemetry.addData("csblue", robot.intakeColor.blue());
            telemetry.addData("ds", robot.intakeDistance.getDistance(DistanceUnit.CM));
            telemetry.addData("average distance", average / loops);
            telemetry.addData("averageCompVal", compAlphaVal);
        } else {
            average = 0;
            loops = 0;
        }


        //telemetry.addData("productAlphaDistance", robot.intakeColor.alpha() * robot.intakeDistance.getDistance(DistanceUnit.CM));
        //for (int i = 0; i < shit.size(); i++) {
            //telemetry.addData("shit".concat(Integer.toString(shit.get(i).fst)), shit.get(i).snd);
        //}
        //telemetry.update();
    }

    @Override
    public void stop() {
        robot.drive(MovementEnum.STOP, 0);
        robot.jewelUp();
    }

}
