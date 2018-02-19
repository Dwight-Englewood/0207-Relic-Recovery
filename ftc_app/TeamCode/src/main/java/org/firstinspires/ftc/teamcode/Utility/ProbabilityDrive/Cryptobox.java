package org.firstinspires.ftc.teamcode.Utility.ProbabilityDrive;

import org.firstinspires.ftc.teamcode.Utility.FixedSizeArray;
import org.firstinspires.ftc.teamcode.Utility.Tuple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by weznon on 2/16/18.
 */

public class Cryptobox {

    private Glyph[][] boz = new Glyph[4][3];


    //yes i know this is sideways - it makes more sense to have a column as a single array since that is how
    //columnes work

    public Cryptobox (Glyph[][] boz) {
        this.boz = boz;
    }

    public  boolean isLegal(Cryptobox in) {
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

    public Cryptobox placeGlyphNewBox(Glyph in, int col) {
        Glyph[][] mutate = new Glyph[4][3];
        if ()

        return new Cryptobox()
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
    public Tuple<Tuple<Integer,Integer>, Cipher>[] canPlaceAndNotMessUpCipher(Glyph first, Glyph second) {
        //first off we need to know all the ways we can place the glyphs
        ArrayList<Cryptobox> possibilities = new ArrayList<>();
        for (int f = 0; f < 3; f++) {
            //first we get all the ways to place the first glyph
            try {
                possibilities.add(this.placeGlyphNewBox(first, f));
            } catch (ProbabilityDriveError e) {

            }

        }
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
