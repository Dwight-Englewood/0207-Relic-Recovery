package org.firstinspires.ftc.teamcode.Utility;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class GlyphClamps {

    private Servo front, back;

    public void init(HardwareMap hwMap){
        back = hwMap.servo.get("clampb");
        front = hwMap.servo.get("clampf");
        back.scaleRange(.1,.9);
        front.scaleRange(.1,.9);
    }

    public void clampBack(boolean clamp) {
        back.setPosition(clamp ? 1 : 0);
    }

    public void clampFront(boolean clamp) {
        front.setPosition(clamp ? 1 : 0);
    }

}
