package org.firstinspires.ftc.teamcode.Utility.ProbabilityDrive;

import static org.firstinspires.ftc.teamcode.Utility.ProbabilityDrive.Glyph.BROWN;
import static org.firstinspires.ftc.teamcode.Utility.ProbabilityDrive.Glyph.GRAY;

/**
 * Created by weznon on 2/19/18.
 */

public class LegalityChecker {

    public static Cryptobox birb = new Cryptobox((new Glyph[][]{{GRAY, BROWN, BROWN, GRAY}, {BROWN, GRAY, GRAY, BROWN}, {GRAY, BROWN, BROWN, GRAY}}));

    public static Cryptobox snek = new Cryptobox((new Glyph[][]{{GRAY, GRAY, BROWN, BROWN}, {GRAY, BROWN, BROWN, GRAY}, {BROWN, BROWN, GRAY, GRAY}}));

    public static Cryptobox freg = new Cryptobox((new Glyph[][]{{BROWN, GRAY, BROWN, GRAY}, {GRAY, BROWN, GRAY, BROWN}, {BROWN, GRAY, BROWN, GRAY}}));

    public static Cipher isCipher(Cryptobox in) throws NoCipherMatch {
        if (in.isLegalSub(birb)) {
            return Cipher.Birb;
        } else if (in.isLegalSub(snek)) {
            return Cipher.Snek;
        } else if (in.isLegalSub(freg)) {
            return Cipher.Freg;
        } else {
            throw new NoCipherMatch();
        }
    }

    public static class NoCipherMatch extends Exception {

    }

}
