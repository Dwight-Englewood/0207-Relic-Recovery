package org.firstinspires.ftc.teamcode.Utility;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class GlyphClamps {

    public enum ClampPos {
        CLAMPED, STANDARD, OUT, RELEASE;
    }

    public Servo front, back;
    private HardwareMap hwMap;

    public GlyphClamps(HardwareMap hwMap) {
        this.hwMap = hwMap;
        this.init();
    }

    private void init(){
        back = this.hwMap.servo.get("clampb");
        front = this.hwMap.servo.get("clampf");
        back.scaleRange(.1 , .9);
        front.scaleRange(.1 , .9);
    }

    public void clampBack(ClampPos pos) {
       switch (pos) {
           case CLAMPED:
               back.setPosition(1);
               break;

           case STANDARD:
               back.setPosition(.5);
               break;

           case OUT:
               back.setPosition(0);
               break;

           case RELEASE:
               back.setPosition(.65);
               break;
       }
    } //Clamped is 1

    public void clampFront(ClampPos pos) {
        switch (pos) {
            case CLAMPED:
                front.setPosition(0);
                break;

            case STANDARD:
                front.setPosition(.5);
                break;

            case OUT:
                front.setPosition(1);
                break;

            case RELEASE:
                front.setPosition(.35);
                break;
        }
    } //Clamped is 0

}
