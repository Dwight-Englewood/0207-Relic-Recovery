package org.firstinspires.ftc.teamcode.Telebop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Utility.Bot;
import org.firstinspires.ftc.teamcode.Utility.EnumController;
import org.firstinspires.ftc.teamcode.Utility.ReleasePosition;


@TeleOp(name = "Worlds_Telebop", group = "Teleop")
public class Worlds_Telebop extends OpMode {

    Bot robot = new Bot();
    EnumController<ReleasePosition> glyphController;
    EnumController<Boolean> frontClampController, backClampController;
    final double liftScaledown = .9;
    final double liftScaleup = .75;

    boolean brakeToggle, pingyBrakeToggle, invert, isRelicMode, movingIntake, placing;
    int brakeCooldown, invertCooldown, modeSwapCooldown, clawCooldown, placingCooldown;
    double relicArmPos1, relicArmPos2;

    @Override
    public void init() {
        robot.init(hardwareMap);
        glyphController = new EnumController<>(ReleasePosition.MIDDLE);


        robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.intakeDrop.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.jewelColorBack.enableLed(false);
        robot.jewelColorForward.enableLed(false);

        brakeToggle = pingyBrakeToggle = invert = isRelicMode = movingIntake = placing = false;
        brakeCooldown = invertCooldown = modeSwapCooldown = clawCooldown = placingCooldown = 0;
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
        if (gamepad1.start && invertCooldown <= 0) {
            invert = !invert;
            invertCooldown = 40;
        }

        if (gamepad1.left_bumper && brakeCooldown <= 0) {
            brakeToggle = !brakeToggle;
            brakeCooldown = 40;
        }
        if (gamepad1.b && brakeCooldown <= 0) {
            pingyBrakeToggle = !pingyBrakeToggle;
            brakeCooldown = 40;
        }
        if (gamepad2.start && modeSwapCooldown <= 0) {
            isRelicMode = !isRelicMode;
            modeSwapCooldown = 40;
        }

        robot.tankDrive(gamepad1.left_stick_y, gamepad1.right_stick_y, gamepad1.left_trigger > .9 ? 1 : .5 * gamepad1.left_trigger, gamepad1.right_trigger > .9 ? 1 : .5 * gamepad1.right_trigger, invert, brakeToggle, pingyBrakeToggle);

        if (gamepad2.x) {
            robot.jewelTeleop();
        } else if (!movingIntake){
            robot.jewelUpTeleop();
        }

        if (!isRelicMode) /*Glyph mode*/ {
            if (gamepad2.y && placingCooldown <= 0) {
                placing = !placing;
                placingCooldown = 20;
            }

            if (gamepad1.right_bumper && placing) {
                frontClampController.addInstruction(Boolean.FALSE, 10);
                backClampController.addInstruction(Boolean.FALSE, 10);
            }

        } else /*Relic mode*/ {

        }

        if (placing) {
            glyphController.addInstruction(ReleasePosition.UP, 10);
            frontClampController.addInstruction(Boolean.TRUE, 5);
            backClampController.addInstruction(Boolean.TRUE, 5);
            robot.backIntakeWallUp();
        } else {
            robot.backIntakeWallDown();
        }

        //Decrement cooldown counters
        invertCooldown--;
        brakeCooldown--;
        modeSwapCooldown--;
        clawCooldown--;
        placingCooldown--;

        //Move the glyph plate
        robot.releaseMove(glyphController.process());
        glyphController.reset();

        //Process the clamps
        robot.glyphClamps.frontClamped = frontClampController.process();
        robot.glyphClamps.backClamped = backClampController.process();
        robot.glyphClamps.process();

        //Move the relic arm servos
        robot.relicArmServo1.setPosition(Range.clip(relicArmPos1, 0, 1));
        robot.relicArmServo2.setPosition(Range.clip(relicArmPos2, 0, 1));
    }

    @Override
    public void stop() {

    }
}