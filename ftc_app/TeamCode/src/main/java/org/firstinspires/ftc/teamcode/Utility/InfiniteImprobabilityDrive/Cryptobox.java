package org.firstinspires.ftc.teamcode.Utility.InfiniteImprobabilityDrive;

import java.util.ArrayList;

import static org.firstinspires.ftc.teamcode.Utility.InfiniteImprobabilityDrive.Glyph.BROWN;
import static org.firstinspires.ftc.teamcode.Utility.InfiniteImprobabilityDrive.Glyph.EMPTY;
import static org.firstinspires.ftc.teamcode.Utility.InfiniteImprobabilityDrive.Glyph.GRAY;
import static org.firstinspires.ftc.teamcode.Utility.InfiniteImprobabilityDrive.LegalityChecker.isCipher;

/**
 * Created by weznon on 2/16/18.
 */

public class Cryptobox {

    //Matrix to represent the cryptobox
    //Stored as a 3x4, where each sub array is a column
    //makes working with it easier
    private Glyph[][] boz = new Glyph[3][4];

    public static void main(String[] args) {
        /*
        System.out.println(LegalityChecker.birb);
        System.out.println("-----");
        System.out.println(LegalityChecker.snek);
        System.out.println("-----");
        System.out.println(LegalityChecker.freg);
        */
        Cryptobox test = new Cryptobox(new Glyph[][]{{GRAY, EMPTY, EMPTY, EMPTY}, {EMPTY, EMPTY, EMPTY, EMPTY}, {EMPTY, EMPTY, EMPTY, EMPTY}});
        System.out.println(test);
        /*
        ArrayList<Tuple<Tuple<Tuple<Integer, Integer>, Cipher>, Cryptobox>> merp = test.findPlacements(BROWN, BROWN, true);

        for (int i = 0; i < merp.size(); i++) {
            System.out.print(merp.get(i).fst.fst.fst);
            System.out.print(", ");
            System.out.print(merp.get(i).fst.fst.snd);
            System.out.print(", ");
            System.out.println(merp.get(i).fst.snd);
            System.out.println("-----------");
            System.out.println(merp.get(i).snd);
            System.out.println("-----------");

        }
        */
        System.out.println(test.improbabilityDrive());

        System.out.println(test.findColumnPlacement(GRAY, BROWN));
    }

    //Simple Constructor - Nothing fancy here
    public Cryptobox(Glyph[][] boz) {
        this.boz = boz;
    }

    //Finds the probability of the next glyphs not being able to be placed while preserving
    //a cipher pattern
    public double improbabilityDrive() {
        //System.out.println("Finding Probability\n----------");
        double gg;
        double gb;
        double bg;
        double bb;

        gg = probabilityWeighted(this.findPlacements(GRAY, GRAY, true));
        gb = probabilityWeighted(this.findPlacements(GRAY, BROWN, true));
        bg = probabilityWeighted(this.findPlacements(BROWN, GRAY, true));
        bb = probabilityWeighted(this.findPlacements(BROWN, BROWN, true));

        //Averaging the values
        return (1 - (gg + gb + bg + bb)/(double) 4);
    }

    public double probabilityWeighted(ArrayList<GlyphPlace> in) {
        if (in.size() == 0) {
            //If there was no possible placement, return 0
            return 0;
        }
        for (int i = 0; i < in.size(); i++) {
            if (in.get(i).column1 == in.get(i).column2) {
                //if there was a placement with both glyphs in same column, weight with probability 1

                return 1;
            } else {
                ;
            }
        }
        //We reach this return iff there was a possible placement(s), but the glyphs must be split
        return .25;
        //this value is how much undervalued a split glyph placement is
        //splitting the placement of a glyph is more time consuming - thus, we underweight it since we want to avoid
        //placing glyphs that lead to this as much as possible, while also not ignoring the possibility

    }


    //Returns the number of glyphs already in a column
    public static int columnFilled(Glyph[] in) {
        int out = 0;
        for (int i = 0; i < in.length; i++) {
            if (in[i].equals(Glyph.EMPTY)) {

            } else {
                out = out + 1;
            }
        }

        return out;
    }

    //Returns the first column in which you can place the glyphs simultaneously and maintain cipher pattern
    //if no such column exsists, ie you must split the glyphs or it is impossible to maintain the pattern,
    //this method will return -1
    public int findColumnPlacement(Glyph first, Glyph second) {
        ArrayList<GlyphPlace> possibilityList = this.findPlacements(first, second, false);
        for (int i = 0; i < possibilityList.size(); i++) {
            if (possibilityList.get(i).column1 == possibilityList.get(i).column2) {
                return possibilityList.get(i).column1;
            }
        }
        return -1;

    }

    //Fancy toString method
    //traverses the cryptobox in a slightly odd fashion, to make the printing easier
    @Override
    public String toString() {
        String built = "";
        //to print this nicely, for each row, we o across the columns and print
        //however, since we store the transpose of a cryptobox, in the code we
        //go along each column and print the value in each row
        for (int i = 3; i >= 0; i--) {
            for (int j = 0; j < 3; j++) {
                built = built.concat(this.boz[j][i].toString());
                if (j != 2) {
                    built = built + "|";
                }

            }
            built = built + "\n";
        }
        return built;
    }

    //Checks if the cryptobox is a legal sub cipher pattern
    //Returns true if glyphs can be placed to make the input pattern
    public boolean isLegalSub(Cryptobox in) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (this.boz[i][j].isMatch(in.boz[i][j], false)) {
                    ;
                } else {
                    for (int i2 = 0; i2 < 3; i2++) {
                        for (int j2 = 0; j2 < 4; j2++) {
                            if (this.boz[i2][j2].isMatch(in.boz[i2][j2], true)) {
                                ;
                            } else {
                                return false;
                            }
                        }
                    }
                }
            }
        }


        return true;
    }

    //Adds a glyph to a specific column
    //Throws a ImprobabilityDriveError with a message to let us know that the issue was because
    //the column was already full
    //This version returns a new Cryptobox reference, to avoid reference issues in java
    public Cryptobox placeGlyphClone(Glyph in, int col) throws ImprobabilityDriveError {
        Glyph[][] mutate = new Glyph[3][4];
        int row = columnFilled(this.boz[col]);
        if (row > 3) {
            throw new ImprobabilityDriveError("column no free space msg msg msg rhing");
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


    //Same as placeGlyphClone, but mutates the object reference instead of making a new object
    public void placeGlyphMutate(Glyph in, int col) throws ImprobabilityDriveError {
        //note:col must be either 0,1,2
        //will crash oterhwise - be careful
        try {
            this.boz[col][columnFilled(this.boz[col])] = in;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ImprobabilityDriveError("column i wanted to add to was full! or some other array index wonkiness: " + e.toString());

        }


    }

    //first is the glyph that will be on the bottom ie the first one out of the bot
    //Returns an object with 4 values:
    //Column for the first glyph placement
    //Column for the second glyph placement
    //Cipher for which pattern is being completed
    //Cryptobox for the actual final placement of the glyphs
    //TODO: Comment this and explain how it works - its fairly wonky
    public ArrayList<GlyphPlace> findPlacements(Glyph first, Glyph second, boolean debug) {
        //first off we need to know all the ways we can place the glyphs
        ArrayList<GlyphPlace> ret = new ArrayList<>();
        for (int f = 0; f < 3; f++) {
            //For each column, we add the first glyph
            try {
                Cryptobox merp = this.placeGlyphClone(first, f);
                //Then, after adding the first glyph, we attempt to place the second
                for (int s = 0; s < 3; s++) {

                    Cryptobox merp1 = merp.placeGlyphClone(second, s);

                    try {
                        Cipher mee = isCipher(merp1);
                        if (debug) {
                            System.out.println(merp1);
                        }
                        ret.add(new GlyphPlace(f, s, mee, merp1));
                        /*
                        System.out.print(ret.get(ret.size() - 1).fst.fst.fst);
                        System.out.print(", ");
                        System.out.print(ret.get(ret.size() - 1).fst.fst.snd);
                        System.out.print(", ");
                        System.out.println(ret.get(ret.size() - 1).fst.snd);
                        System.out.println("-----------");
                        System.out.println(ret.get(ret.size() - 1).snd);
                        System.out.println("-----------");
                        */
                    } catch (LegalityChecker.NoCipherMatch e) {
                    }
                }
            } catch (ImprobabilityDriveError e) {
                System.out.println("gonna assume that this meant no spots left");
            }
        }

        return ret;
    }

    //Custom Exception, to organize debugging
    public class ImprobabilityDriveError extends Exception {
        String msg;

        public ImprobabilityDriveError() {
            msg = "";
        }

        public ImprobabilityDriveError(String msg) {
            this.msg = msg;
        }
    }
}
