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

A cryptobox pattern is stored as a 3x4 matrix in the code, as the pseudo-transpose of the real cryptobox layout. This allows for easier access to columns, as the pseudo-transpose layout means a single column is represented by a single array, with index 0 the bottom glyph position, index 3 the top.

The main functionality of the algorithm is contained in the `IID#findPlacements`, `IID#findColumnPlacement`, `IID#probabilityWeighted`, `IID#improbabilityDrive` methods

### `IID#findPlacements`

This method takes as input two glyphs to add to the object's cryptobox.

The logic of the method is contained in two nested `for` loops. The first `for` loop trys to place the first glyph in a column. The nested `for` loop then attempts to place the second glyph, and for each new cryptobox pattern, with the two glyphs to be added, will determine if the pattern is a valid cipher; can the pattern still be filled in to make a cipher? If it can, the columns the glyphs were placed in is recorded, as well as the cipher pattern that remains allowed. If multiple ciphers could still be completed, then it will return the first of Bird > Snake > Frog. This ordering was determined by hand tests and driver input - Bird generally is more flexible in the colors of glyphs that can be added than snake and frog. Additionally, the drivers prefer completing a Bird Cipher over the other two.

This method returns an array consisting of a object with 4 instance fields: an int representing the first glyph's column, an int representing the second glyph's column, a cipher enum representing which cipher is able to be completed, and a cryptobox that is the final placement of the glyphs added to the exsisting cryptobox.

### `IID#findColumnPlacement`

This method takes an input produced by `IID#findPlacements`

This method loops over all elements of the input and attempts to find a pattern where both glyphs are placed in the same column. This is important, as being able to place the glyphs in the same column is a massive timesaver during auton. Splitting up the glyphs takes more time, as the robot must line up twice. 

It returns an int representing which column to place the glyphs in to maintain a cipher pattern. If no such placement is found, it returns -1

### `IID#probabilityWeighted`

This method takes an input produced by `IID#findPlacements`

This method will loop over the input and determing if a single column placement of the glyphs exsists, in which case the function returns a 1 - full weighting in the probability calculation.
If you must split the glyphs to maintain a cipher, the weight is 0.25. In our probability calculation, we do not want to fully ignore splitting glyphs - however, we want to avoid them, and avoid taking a path that results in 3 out of the 4 possible combinations of colors for the glyphs to be added to the cryptobox, but must always be split, versus a path that has 2 out of 4 glyphs combinations to be placed, but both in a single column.
If no such placements exsist, ie the input is empty, it will return 0

### `IID#improbabilityDrive`
This method takes a cryptobox

This method calculates the probability that a given cryptobox, when you fetch 2 new glyphs from the center pile, will be able to be completed. Two versions of this function exsist, one where brown and gray glyph colors are weighted equally, and one that allows for variable weights, which can be used to maintain accuracy if the pile is biased, and does not have evenly distributed colors. 

The algorithm is uses is to call `IID#probabilityWeighted` for all possible glyph inputs (Gray Gray, Brown Gray, Gray Brown, Brown Brown) and average the weight.

This method returns the chance the you will pick up a pair of glyphs which cannot be placed to maintain a valid cipher pattern

## Results


