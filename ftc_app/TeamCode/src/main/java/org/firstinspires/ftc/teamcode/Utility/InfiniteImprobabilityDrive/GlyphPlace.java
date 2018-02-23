package org.firstinspires.ftc.teamcode.Utility.InfiniteImprobabilityDrive;

/**
 * Created by weznon on 2/20/18.
 */

public class GlyphPlace {

    public int column1;
    public int column2;
    public Glyph glyph1;
    public Glyph glyph2;
    public Cipher pattern;
    public Cryptobox cryptobox;

    public GlyphPlace(int column1, int column2, Glyph glyph1, Glyph glyph2, Cipher pattern, Cryptobox cryptobox) {
        this.column1 = column1;
        this.column2 = column2;
        this.glyph1 = glyph1;
        this.glyph2 = glyph2;
        this.pattern = pattern;
        this.cryptobox = cryptobox;
    }
}
