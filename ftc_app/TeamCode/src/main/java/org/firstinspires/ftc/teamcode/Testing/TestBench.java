package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by aburur on 11/9/17.
 */
//@TeleOp(name = "Test Bench", group = "Teleop")
@Disabled
public class TestBench extends OpMode {

    Servo flipper, releaseLeft, releaseRight;
    double relLPos, relRPos,flipPos ;

    public void init() {
        releaseRight = hardwareMap.get(Servo.class, "rel r");
        releaseLeft = hardwareMap.get(Servo.class, "rel l");
        flipper = hardwareMap.get(Servo.class, "flip");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        relLPos = .5;
        relRPos = .5;
        flipPos = .5;
    }

    @Override
    public void loop() {

        if (gamepad1.a){
            flipPos += .005;
        }
        else if (gamepad1.b){
            flipPos -= .005;
        }

        if (gamepad1.x){
            relRPos += .005;
            relLPos -= .005;
        }
        else if (gamepad1.y){
            relRPos -= .005;
            relLPos += .005;
        }

        relRPos = Range.clip(relRPos, 0,1);
        relLPos = Range.clip(relLPos, 0, 1);
        flipPos = Range.clip(flipPos, 0, 1);
        releaseLeft.setPosition(relLPos);
        releaseRight.setPosition(relRPos);
        flipper.setPosition(flipPos);

        telemetry.addData("release l position", relLPos);
        telemetry.addData("release r position", relRPos);
        telemetry.addData("flip position", flipPos);
    }

    @Override
    public void stop() {

    }

}
