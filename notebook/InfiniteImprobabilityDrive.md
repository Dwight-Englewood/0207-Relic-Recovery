# Infinite Improbability Drive

## Rationale

Doing autonomous, we wanted to have be able to place more than the one preloaded glyph in the Vuforia column. However, multi-glyph autonomous' run into the issue of ruining a cipher pattern; randomly
placing pairs of glyphs can result in a pattern at the end of autonomous which looks like this:

<insert picture here>

This pattern cannot be further filled in and complete one of the three cipher patterns. To avoid this, we first tried to handwrite rules for different scenarios. 

For example, if the vuforia glyph was placed in column one, then the following pairs of glyphs were "acceptable" - placing these in a certain column would still allow the drivers to complete a cipher during Teleop.

However, handwriting rules is very tedious, and while doable for an autonomous with 3 glyphs, a 5 glyph placing auton would have many more cases to try. Therefore, we decided to write an algorithm to decide whether a glyph
is placable or not, as well as determining probabilty that a given glyph pattern could still be completed. This has the added benefit of allowing a greater ease of use of the algorithm in Teleop mode, as it can decide for the drivers where to place certain glyphs and whether ones need to be ejected from the bot. This reduces strain on the drivers and allows them to more focus on driving.

## Implementation

The main implementation of this algorithm is located in <appendix bumfuck nebraska>. 

--comments from the shit here


## Results


