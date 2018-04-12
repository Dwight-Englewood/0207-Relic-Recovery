package org.firstinspires.ftc.teamcode.Telebop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Utility.Bot;
import org.firstinspires.ftc.teamcode.Utility.EnumController;
import org.firstinspires.ftc.teamcode.Utility.GlyphClamps;
import org.firstinspires.ftc.teamcode.Utility.ReleasePosition;


@TeleOp(name = "Worlds_Telebop", group = "Teleop")
public class Worlds_Telebop extends OpMode {

    Bot robot = new Bot();
    EnumController<ReleasePosition> glyphController;
    EnumController<GlyphClamps.ClampPos> frontClampController, backClampController;
    final double liftScaledown = .9;
    final double liftScaleup = .75;

    boolean brakeToggle, pingyBrakeToggle, invert, isRelicMode, movingIntake, placing, manualClampBack, manualClampFront, farOut;
    int brakeCooldown, invertCooldown, modeSwapCooldown, clawCooldown, placingCooldown, clampFrontCooldown, clampBackCooldown;
    double relicArmPos1, relicArmPos2, leftTrigger, rightTrigger;

    @Override
    public void init() {
        robot.init(hardwareMap);
        glyphController = new EnumController<>(ReleasePosition.MIDDLEUP); //might need to be MIDDLE
        frontClampController = new EnumController<>(GlyphClamps.ClampPos.STANDARD);
        backClampController = new EnumController<>(GlyphClamps.ClampPos.STANDARD);

        robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.intakeDrop.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.jewelColorBack.enableLed(false);
        robot.jewelColorForward.enableLed(false);

        brakeToggle = pingyBrakeToggle = invert = isRelicMode = movingIntake = placing = manualClampBack = manualClampFront = farOut = false;
        brakeCooldown = invertCooldown = modeSwapCooldown = clawCooldown = placingCooldown = clampBackCooldown = clampFrontCooldown = 0;
        relicArmPos1 = relicArmPos2 = 1;

        telemetry.addLine("Ready.");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.intakeDrop.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        leftTrigger = gamepad1.left_trigger > .9 ? 1 : (float).5 * gamepad1.left_trigger;
        rightTrigger = gamepad1.right_trigger > .9 ? 1 : (float).5 * gamepad1.right_trigger;

        if (gamepad1.start && invertCooldown <= 0) {
            invert = !invert;
            invertCooldown = 40;
        }

        if (gamepad1.left_bumper && brakeCooldown <= 0) {
            brakeToggle = !brakeToggle;
            brakeCooldown = 40;
        }
        /*if (gamepad1.b && brakeCooldown <= 0) {
            pingyBrakeToggle = !pingyBrakeToggle;
            brakeCooldown = 40;
        }*/
        if (gamepad2.start && modeSwapCooldown <= 0) {
            isRelicMode = !isRelicMode;
            modeSwapCooldown = 40;
        }

        robot.tankDrive(gamepad1.left_stick_y, gamepad1.right_stick_y, leftTrigger, rightTrigger, invert, brakeToggle, false);

        if (robot.intakeDrop.getCurrentPosition() >= 300) {
            glyphController.addInstruction(ReleasePosition.DROP, 10);
            robot.backIntakeWallDown();
        }

        if (gamepad2.right_stick_y > .3) {
            robot.intakeDrop.setPower(-1);
            robot.intake(.5);
            robot.jewelOut();
            glyphController.addInstruction(ReleasePosition.DROP, 10);
            frontClampController.addInstruction(GlyphClamps.ClampPos.CLAMPED, 10);
            backClampController.addInstruction(GlyphClamps.ClampPos.CLAMPED, 10);
            movingIntake = true;
        } else if (gamepad2.right_stick_y < -.3) {
            robot.intakeDrop.setPower(1);
            glyphController.addInstruction(ReleasePosition.DROP, 10);
            frontClampController.addInstruction(GlyphClamps.ClampPos.CLAMPED, 10);
            backClampController.addInstruction(GlyphClamps.ClampPos.CLAMPED, 10);
            robot.intake(.5);
            robot.jewelOut();
            movingIntake = true;
        } else if (!gamepad2.x && !(gamepad2.left_trigger > .1 || gamepad2.right_trigger > .1 || gamepad2.right_bumper)) {
            robot.intakeDrop.setPower(0);
            robot.intake(0);
            robot.jewelUpTeleop();
            movingIntake = false;
        } else if (!(gamepad2.left_trigger > .1 || gamepad2.right_trigger > .1 || gamepad2.right_bumper)){
            movingIntake = false;
            robot.intakeDrop.setPower(0);
            robot.intake(0);
        }

        if (gamepad2.left_bumper) {
            robot.jewelTeleop();
        } else if (!movingIntake){
            robot.jewelUpTeleop();
        }

        if (!isRelicMode) /*Glyph mode*/ {
            if (gamepad2.y && placingCooldown <= 0) {
                placing = !placing;
                placingCooldown = 70;
            }

            if (gamepad1.right_bumper && placing) {
                frontClampController.addInstruction(GlyphClamps.ClampPos.STANDARD, 10);
                backClampController.addInstruction(GlyphClamps.ClampPos.STANDARD, 10);
            }

            if (gamepad2.a && clampBackCooldown <= 0) {
                manualClampBack = !manualClampBack;
                clampBackCooldown = 40;
            }
            if (gamepad2.b && clampFrontCooldown <= 0) {
                manualClampFront = !manualClampFront;
                clampFrontCooldown = 40;
            }

            if (gamepad2.x && clampBackCooldown <= 0 && clampFrontCooldown <= 0) {
                farOut = !farOut;
                clampFrontCooldown = 20;
                clampBackCooldown = 20;
            }

            if (placing) {
                if (placingCooldown <= 0) {
                    glyphController.addInstruction(ReleasePosition.UP, 10);
                }
                frontClampController.addInstruction(GlyphClamps.ClampPos.CLAMPED, 5);
                backClampController.addInstruction(GlyphClamps.ClampPos.CLAMPED, 5);
                manualClampBack = false;
                manualClampFront = false;
                robot.backIntakeWallDown();
            } else if (placingCooldown <= 10) {
                robot.backIntakeWallUp();
            }

            if (manualClampFront) {
                frontClampController.addInstruction(GlyphClamps.ClampPos.CLAMPED, 3);
            }

            if (manualClampBack) {
                backClampController.addInstruction(GlyphClamps.ClampPos.CLAMPED, 3);
            }

            if (farOut) {
                frontClampController.addInstruction(GlyphClamps.ClampPos.OUT, 7);
                backClampController.addInstruction(GlyphClamps.ClampPos.OUT, 7);
            }

            if (gamepad2.right_bumper) {
                //Specific to the teleop, we have 3 levels of priority
                //A regular change in the position is 1 - these are the standard change
                glyphController.addInstruction(ReleasePosition.DOWN, 1);
                robot.intake(.7);
            } else {
                if (gamepad2.right_trigger > .2 || gamepad2.left_trigger > .2) {
                    glyphController.addInstruction(ReleasePosition.DOWN, 1);
                    robot.intake(-.95);
                } else {
                    //This line is not needed, as this specific addition to the controller object will never change the output. However, it is included to keep clarity as to what will happen
                    //The zero priority will not change the result of process, as priority is seeded at 0 - and is strictly increasing. This is equivalent to a blank statement, which we use to keep code clarity
                    glyphController.addInstruction(ReleasePosition.MIDDLE, 0);
                    robot.intake(0);
                }
            }

            if (gamepad2.left_stick_y < -.15) {
                glyphController.addInstruction(ReleasePosition.MIDDLEUP, 1);
                robot.lift.setPower(gamepad2.left_stick_y * liftScaleup);
            } else if (gamepad2.left_stick_y > .15) {
                glyphController.addInstruction(ReleasePosition.MIDDLEUP, 1);
                robot.lift.setPower(gamepad2.left_stick_y * liftScaledown);
            } else {
                glyphController.addInstruction(ReleasePosition.MIDDLE, 0);
                robot.lift.setPower(0);
            }

        } else /*Relic mode*/ {
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

            if (clawCooldown <= 0) {
                if (gamepad2.right_trigger > .1) {
                    relicArmPos2 += .1;
                    clawCooldown = 5;

                } else if (gamepad2.right_bumper) {
                    relicArmPos2 -= .1;
                    clawCooldown = 5;
                }
            }
        }

        //Decrement cooldown counters
        invertCooldown--;
        brakeCooldown--;
        modeSwapCooldown--;
        clawCooldown--;
        placingCooldown--;
        clampBackCooldown--;
        clampFrontCooldown--;

        //Move the glyph plate
        robot.releaseMove(glyphController.process());
        glyphController.reset();

        //Process the clamps
        //robot.glyphClamps.frontClamped = frontClampController.process().booleanValue();
        //robot.glyphClamps.backClamped = backClampController.process().booleanValue();
        //robot.glyphClamps.process();

        robot.glyphClamps.clampFront(frontClampController.process());
        robot.glyphClamps.clampBack(backClampController.process());
        telemetry.addData("front clamp", frontClampController.process());
        telemetry.addData("back clamp", backClampController.process());
        frontClampController.reset();
        backClampController.reset();

        //Move the relic arm servos
        robot.relicArmServo1.setPosition(Range.clip(relicArmPos1, 0, 1));
        robot.relicArmServo2.setPosition(Range.clip(relicArmPos2, 0, 1));

        telemetry.addData("Inverted?", invert);
        telemetry.addData("Braking?", brakeToggle);
        telemetry.addData("Relic Mode?", isRelicMode);
        //telemetry.addData("PingyBraking?", pingyBrakeToggle);
    }

    @Override
    public void stop() {

    }
}