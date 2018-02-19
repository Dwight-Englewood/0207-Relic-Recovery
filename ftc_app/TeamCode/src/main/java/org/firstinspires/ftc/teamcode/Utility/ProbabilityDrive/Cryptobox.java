package org.firstinspires.ftc.teamcode.Utility.ProbabilityDrive;

import org.firstinspires.ftc.teamcode.Utility.Tuple;

import java.util.ArrayList;

import static org.firstinspires.ftc.teamcode.Utility.ProbabilityDrive.LegalityChecker.isCipher;

/**
 * Created by weznon on 2/16/18.
 */

public class Cryptobox {

    private Glyph[][] boz = new Glyph[3][4];


    //yes i know this is sideways - it makes more sense to have a column as a single array since that is how
    //columnes work

    public Cryptobox (Glyph[][] boz) {
        this.boz = boz;
    }

    public boolean isLegalSub(Cryptobox in) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; i < 4; i++) {
                if (this.boz[i][j].isLegal(in.boz[i][j], false)) {
                    ;
                } else {
                    return false;
                }
            }

        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; i < 4; i++) {
                if (this.boz[i][j].isLegal(in.boz[i][j], true)) {
                    ;
                } else {
                    return false;
                }
            }

        }
        return true;
    }

    //gives the number of glyphs already in column
    public static int howFull(Glyph[] in) {
        int out = 0;
        for (int i = 0; i < in.length; i++) {
            if (in[i].equals(Glyph.EMPTY)) {

            } else {
                out = out + 1;
            }
        }

        return out;
    }

    public Cryptobox placeGlyphNewBox(Glyph in, int col) throws ProbabilityDriveError{
        Glyph[][] mutate = new Glyph[3][4];
        int row = howFull(this.boz[col]);
        if (row > 3) {
            throw new ProbabilityDriveError("column no free space msg msg msg rhing");
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == col && j == row) {
                    mutate[i][j] = in;
                } else {
                    mutate[i][j] = this.boz[i][j];
                }
            }
        }

        return new Cryptobox(mutate);
    }


    public void placeGlyph(Glyph in, int col) throws ProbabilityDriveError{
        //note:col must be either 0,1,2
        //will crash oterhwise - be careful
        //if no free space, will not do anything - silent fail might be bad idea?
        //actually it will throw out of bounds error
        try {
            this.boz[col][howFull(this.boz[col])] = in;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ProbabilityDriveError("column i wanted to add to was full! or some other array index wonkiness: " + e.toString());

        }


    }
    //first is the glyph that will be on the bottom ie the first one out of the bot
    //returns a tuple<Tuple, Cipher>
    //tuple is the columnn the first glyph needs to go in, and column second glyph needs to go into
    //cipher is the cipher that is still legal - if multiple it will be a list though i might remove that later cuz after 3 glyphs its always gonna be length 1 i think
    //hi i removed the list inside and added to outside cuz theyres a chance of multiple possile ways to place the glyph
    //need to make a analysis function for that later
    public ArrayList<Tuple<Tuple<Integer,Integer>, Cipher>> canPlaceAndNotMessUpCipher(Glyph first, Glyph second) {
        //first off we need to know all the ways we can place the glyphs
        ArrayList<Tuple<Tuple<Integer, Integer>, Cipher>> ret = new ArrayList<>();
        ArrayList<Cryptobox> possibilities = new ArrayList<>();
        for (int f = 0; f < 3; f++) {
            //first we get all the ways to place the first glyph
            try {
                Cryptobox merp = this.placeGlyphNewBox(first, f);

                for (int s = 0; s < 3; s++) {
                    Cryptobox merp1 = this.placeGlyphNewBox(second, s);
                    try {
                        Cipher mee = isCipher(merp1);
                        ret.add(new Tuple<Tuple<Integer, Integer>, Cipher>(new Tuple<Integer, Integer>(f, s), mee));
                    } catch (LegalityChecker.NoCipherMatch e) {

                    }
                }
            } catch (ProbabilityDriveError e) {
                System.out.println("gonna assume that this meant no spots left");
            }
        }

        return ret;
    }

    public class ProbabilityDriveError extends Exception{
        String msg;
        public ProbabilityDriveError() {
            msg = "";
        }

        public ProbabilityDriveError(String msg) {
            this.msg = msg;
        }
    }
}
