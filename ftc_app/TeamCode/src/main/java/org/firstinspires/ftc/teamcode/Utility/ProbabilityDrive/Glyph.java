package org.firstinspires.ftc.teamcode.Utility.ProbabilityDrive;

/**
 * Created by weznon on 2/16/18.
 */

public enum Glyph {
    BROWN,
    GRAY,
    EMPTY;

    public boolean isLegal(Glyph background) {
        if (this.equals(EMPTY)) {
            return true;
        } else {
            return (this.equals(background));
        }
    }
}
