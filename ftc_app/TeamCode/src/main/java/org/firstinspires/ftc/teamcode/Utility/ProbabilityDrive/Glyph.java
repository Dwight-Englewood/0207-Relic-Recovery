package org.firstinspires.ftc.teamcode.Utility.ProbabilityDrive;

/**
 * Created by weznon on 2/16/18.
 */

public enum Glyph {
    BROWN,
    GRAY,
    EMPTY;

    //this doesnt work - doesnt check for inverse patterns
    //make it do two checks, one for not equals for all, that should garuntee
    public boolean isLegal(Glyph background) {
        if (this.equals(EMPTY)) {
            return true;
        } else {
            return (this.equals(background));
        }
    }
}
