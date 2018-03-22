package org.firstinspires.ftc.teamcode.Utility;

/**
 * Created by weznon on 11/17/17.
 */

public enum ReleasePosition {

    DOWN        (0),
    MIDDLE      (.52),
    MIDDLEUP    (.57),
    UP          (1),
    INIT        (.72),
    DROP        (.77);

    private double val;

    ReleasePosition (double val) {
        this.val = val;
    }

    public double getVal() {
        return this.val;
    }

}
