# Auto Adjustment of the Glyph Placer

## Rationale
Our glyph placer has a total of 7 positions commonly used in the robotics code. We have a position for the following 
    DOWNER,
    DOWN,
    MIDDLE,
    MIDDLEUP,
    UP,
    INIT,
    DROP

Our drivers would have a hard time manually changing the glyph placer to these positions in the different scenarios; additionally, some positions are required for the robot to not destroy itself. Our glyph intake is able to be raised up. This has the side effect that the wheels of our intake are pushed back on the bot - into a location where the glyph placer normally resides. Whenever this happens we must be sure that the glyph placer is also moved up. This would be a difficult task for the drivers to do while also concentrating on placing glyphs. Therefore, we wanted to make a simple way for the programmers to control the position of the glyph placer for the drivers.

## Implementation

The control for this functionality is spread out over the entire teleop, and the central algorithm is the EnumController object. See kljasdfkljhadlfjkhadfjklh for documetation of that object.

By using the EnumController, we can use enums to represent the various states of the glyph placer. When certain controls are used which require the movement of the glyph placer, a enum is added to an EnumController. At the end of each teleop loop, the EnumController is processed, and the glyph set to the correct value.


include some actual code segments of the usage

not entirely sure how to make it not the same thing as the enumcontroller

## Results

This implementation has allowed for many driver improvements. Prior to this implementation, glyphs would often fall out of the glyph placer while it was being raised, to place glyphs above the 2nd row. The drivers could raise the glyph placer slightly to help keep the glyphs in, but this required additional focus from the drivers to this small detail. The teleop now, whenever the lift is being used, now automatically will tilt the glyph placer to minimize the chacne of glyphs falling out. 

