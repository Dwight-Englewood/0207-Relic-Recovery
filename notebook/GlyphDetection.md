# Automatic Glyph Color Detection

## Rationale
Being able to find the color of an intaked glyph is immensly useful ability, as it allows for the usage of the Infinite Improbability Drive during teleop without the drivers having to manually enter glyph colors, which ultimately would be slower than not having the IID at all. It also it essential for the usafe of the IID during autonomous; the algorithm and choices it makes are useless if it is impossible to figure out what glyphs the robot has picked up. 

## Implementation

The detection function has had a couple of attempts 
### Attempt 1
The first attempt at writing this function was based on a limited set of data collected from the sensors. We hoped that there would be a relationship between the alpha channel from a color sensor mounted in the intake and the distance  recorded by the 
