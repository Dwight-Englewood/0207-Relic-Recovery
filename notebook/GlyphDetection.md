# Automatic Glyph Color Detection

## Rationale
Being able to find the color of an intaked glyph is immensly useful ability, as it allows for the usage of the Infinite Improbability Drive during teleop without the drivers having to manually enter glyph colors, which ultimately would be slower than not having the IID at all. It also it essential for the usafe of the IID during autonomous; the algorithm and choices it makes are useless if it is impossible to figure out what glyphs the robot has picked up. 

## Implementation
###Detection
The detection function has had a couple of attempts.
#### Attempt 1
The first attempt at writing this function was based on a limited set of data collected from the sensors. We hoped that there would be a relationship between the alpha channel from a color sensor mounted in the intake and the distance recorded by the distance sensor. 

```
public Glyph findGlyphType() {

        double distanceCM = this.intakeDistance.getDistance(DistanceUnit.CM);
        if (Double.isNaN(distanceCM)) {
            return Glyph.EMPTY;
            //only happens with open air - and thus no glyph is present
        }

        double compAlphaVal = -10 * distanceCM + 150;

        if (this.intakeColor.alpha() > compAlphaVal) {
            return GRAY;
        } else {
            return Glyph.BROWN;
        }

    }
```

This was the first iteration of the code. However, this approach did not work. Once tested on the robot, a number of calculations came up. The first was that open air no longer returned NaN for distance, as light would reflect off nearby parts of the sensor mount.

Additionally, the metric used for comparison was off. While it had held true for the gathered data, it did not work on the robot. We then decided we must use a different metric
#### Attempt 2
The second attempt was started by collection of more real data from the robot. The data collected can be seen in the attached chart.

Additionally, light falloff is based on an inverse square law. Thus, instead of a linear function, one would expect a more accurate metric for comparison to be found using an equation of some form Alpha = m * Distance^-2 + b. We then attempted to use a regression using this relation to find values of m and b for each glyph color. We could then take the gathered data and compute distance to each curve of best fit - the closer line is the glyph color. 

However, as seen in the attached chart, this method does not work either. There are many instances where gray glyphs are closer to the brown glyph regression. This is explain through the poor R^2 values and the high variation in data. The speculated reason for this is an innacuracy in the data collection, as the distance was estimated based on the flucuating value. Averaging the distance sensor input over a period of time would help solve this error in data collection; however, the data is already so close that it does not seem to matter.

#### Attempt 3
Clearly, mounting the color sensor did not result in usable values. Therefore, we added another color sensor into the side of the intake. This hopefully would make the distance reading more constant, as the glyphs have less variance in side to side location, though they can have great differences in the distance in height from the bottom of the intake.

Once this was mounted, improved results were immediate. Almost all readings of the glyphs were under the distance of 5.45, which meant we could directly compare the alpha channel to determine glyph type. 

This worked for the most part, but failed in 2 scenarios. It failed when the glyph was intaken at a 45 degree angle, and as well when the glyph intake was perfect - if the glyph was intaked at an angle, the distance would no longer be less than 5.45, which throw off the comparison. Additionallly, if the intake was perfect, the reading would be over 500 for both colors of glyphs, making it impossible to determine which is which. This would work for most scenarios, we wanted to be able to make a solution that could work in all possible scenarios.

#### Attmept 4
We had a fancy PixyCam to use, so we decided to put it to work. But our initial attempt at training did not work very well. We had lots of false positives and overall poor readings. For more details on the results of this, see « vision attempts document » 

A second attempt was somewhat more succesful, but required a very bright light

A third attempt was succesful, and would have been trusted to work in the intake 90% of the time. However, we wanted to explore more options with color sensors.

#### Attmept 5

color sensor far mount stuff

explainy words

no worky too low

we mount further and higher

gooder

yay

still only kinda work though

### Application
While we are currently unable to detect the color of the glyph, we can still write methods and algorithms that utilize said function, despite its flaws. 
Attempting to poll data from the sensor while a glyph is passing overhead will result in multiple points of being collected from the same glyph as it slides over the sensor. Naively gathering data from the sensor will result in the robot believing multiple glyphs have passed over if one assumes that everytime a glyph is recieved by `findGlyphType`

The solution we created is the following
```
for (int i = 0; i < intakeReadings.size(); i++) {
            if (intakeReadings.get(i).equals(Glyph.EMPTY)) {
                intakeReadings.remove(i);
                i--;
            } else {
                break;
            }
        }
        for (int i = intakeReadings.size() - 1; i > 0; i--) {
            if (intakeReadings.get(i).equals(Glyph.EMPTY)) {
                intakeReadings.remove(i);
                i++;
            } else {
                break;
            }
        }
        double amountBrown = 0;
        double amountGray = 0;
        double frontBrown = 0;
        double frontGray = 0;
        int nonEmpty = 0;
        int midPoint = intakeReadings.size() / 2;
        Glyph glyph1 = Glyph.EMPTY;
        Glyph glyph2 = Glyph.EMPTY;
        if (intakeReadings.size() < doubleGlyphReading) {
            for (int i = 0; i < intakeReadings.size(); i++) {
                if (intakeReadings.get(i).equals(Glyph.BROWN)) {
                    amountBrown++;
                } else if (intakeReadings.get(i).equals(Glyph.GRAY)) {
                    amountGray++;
                }
            }
            if (amountBrown > amountGray && amountBrown > doubleGlyphReading / 2.2 ) {
                glyph1 = Glyph.BROWN;
            } else if (amountGray > amountBrown && amountGray > doubleGlyphReading / 2.2) {
                glyph2 = Glyph.GRAY;
            }
            break;
        } else {
            for (int i = 0; i < intakeReadings.size(); i++) {
                if (intakeReadings.get(i).equals(Glyph.BROWN)) {
                    amountBrown++;
                    nonEmpty++;
                    if (i < midPoint) {
                        frontBrown = frontBrown + 1;
                    } else {
                        frontBrown = frontBrown - 1;
                    }
                } else if (intakeReadings.get(i).equals(Glyph.GRAY)) {
                    amountGray++;
                    nonEmpty++;
                    if (i < midPoint) {
                        frontGray = frontGray + 1;
                    } else {
                        frontGray = frontGray - 1;
                    }
                }
            }
            boolean foundGlyph = false;
            if (Math.abs(frontBrown / amountBrown - 0) < Math.abs(frontBrown / amountBrown - 1) && Math.abs(frontBrown / amountBrown - 0) < Math.abs(frontBrown / amountBrown - -1)) {
                //this means even distribution of brown over all readings, aka we got only a brown one
                if (intakeReadings.size() > doubleGlyphReading) {
                    glyph1 = Glyph.BROWN;
                    glyph2 = Glyph.BROWN;
                    foundGlyph = true;
                }
            } else if (Math.abs(frontGray / amountGray - 0) < Math.abs(frontGray / amountGray - 1) && Math.abs(frontGray / amountGray - .5) < Math.abs(frontGray / amountGray - -1)) {
                //this means even distribution of brown over all readings, aka we got only a gray one
                if (intakeReadings.size() > doubleGlyphReading) {
                    glyph1 = Glyph.GRAY;
                    glyph2 = Glyph.GRAY;
                    foundGlyph = true;
                }
            }
            if (!foundGlyph) {


                if (Math.abs(frontBrown / amountBrown - 1) > Math.abs(frontGray / amountGray - 1)) {
                    System.out.println(89);
                    glyph1 = Glyph.GRAY;
                    glyph2 = Glyph.BROWN;
                } else {
                    System.out.println(88);
                    glyph1 = Glyph.BROWN;
                    glyph2 = Glyph.GRAY;
                }
            }


        }
```

This algorithm first removes consecutive empty readings from the beginning and end of an arraylist of readings from `findGlyphType`, collected whenever the intake is being run. The above method is run in the loop after the intake is stopped.

It will then iterate over the list once, collecting data to get 5 values. It will find the number of times brown was read by the color sensor, how many times gray was read, how many non empty values were read, and whether the value was in the first half of the data set, or the second.

When two glyphs are intaked, it can be reasonably asssumed that the midway point will be an approriate divide forwhen the color readings should change. Therefore, if the reading is in the first half, then 1 is added to the weighting variable, `frontColor`, and if the second half, -1 is added. 

We then can calculate the average weighting. If a glyph was the first to be intaked, then the weighting should be close to 1, and if the second one, readings should have come from the second half of the list, and thus the weighting should be close to -1. If th weighting is close to 0, that could mean 2 different things. It could mean that no meaningful data for that color was collected, or it could mean that values for that color were detected across the entire set of polled data - 2 glyphs of the same color were taken in. Of course, we also must check that there is enough data for 2 glyphs to have come in. We found that generally 10 readings would come in per glyph - so if the size of the list is greater than 20, we can reasonably expect that there were 2 glyphs, and if less, then only 1.

## Results

While ultimately the actual detection of the color is a bust, aspects relying on that method are written and work. Testing the processor of the glyph detection data proved it to work, sucesfully identifying the types of glyphs coming in if the colors are pre-provided. While ultimately the methods as they are do not work, there is only one piece left in the puzzle that would allow for things such as the Probability Drive and Cipher Assist to work.
