package org.firstinspires.ftc.teamcode.Utility.ProbabilityDrive;

/**
 * Created by weznon on 2/16/18.
 */

public class Cryptobox {

    private Glyph[][] boz = new Glyph[3][4];

    public Cryptobox (Glyph[][] boz) {
        this.boz = boz;
    }

    public  boolean isSubset(Cryptobox in) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; i < 4; i++) {
                if (this.boz[i][j].isLegal(in.boz[i][j])) {
                    ;
                } else {
                    return false;
                }
            }
        }
        return true;
    }


}
