package org.firstinspires.ftc.teamcode.Utility.ProbabilityDrive;

/**
 * Created by weznon on 2/16/18.
 */

public enum Glyph {
    BROWN,
    GRAY,
    EMPTY;

    @Override
    public String toString() {
        if (this.equals(BROWN)) {
            return "B";
        } else if (this.equals(GRAY)) {
            return "G";
        } else {
            return " ";
        }
    }

    public boolean isLegal(Glyph background, boolean inverse) {
        if (!inverse) {
            if (this.equals(EMPTY)) {
                return true;
            } else {
                return (this.equals(background));
            }
        } else {
            if (this.equals(EMPTY)) {
                return true;
            } else {
                return (!this.equals(background));
            }

    }
}
}
