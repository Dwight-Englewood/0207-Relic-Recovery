package org.firstinspires.ftc.teamcode.Utility.ProbabilityDrive;

/**
 * Created by weznon on 2/16/18.
 */

public enum Glyph {
    BROWN,
    GRAY,
    EMPTY;

    public static Glyph invert(Glyph g) {
        if (g.equals(BROWN)) {
            return GRAY;
        } else if (g.equals(GRAY)) {
            return BROWN;
        } else {
            return EMPTY;
        }
    }
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

    public boolean isMatch(Glyph background, boolean inverse) {
        if (inverse) {
            if (this.equals(EMPTY)) {
                return true;
            } else {
                return (this.equals(background));
            }
        } else {
            if (this.equals(EMPTY)) {
                return true;
            } else {
                return (this.equals(invert(background)));
            }
        }
    }
}
