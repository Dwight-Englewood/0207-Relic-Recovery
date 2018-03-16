# Invertible Driving

## Rationale
Similarl to the brake mechanism, being able to invert the control scheme for the driver of the robot is very useful. Such a mode could be applied in different ways by the drivers to improve their efficiency during teleop

## Implementation
The invert mode implementation relies on another key technique used in our programming, that of automatic cooldowns. See the entry on Automatic Cooldowns for further details on this process. 

Whenever a specific button is pressed on the controllers, and if the cooldown has passed, the driver mode will switch to invert, which is held by a boolean. This boolean is passed to our drive function for processing, located at `Utility/Bot#tankDrive`. See the documentation of the bot class for details on the full drive method

```
FL.setPower(!invert ? leftStick : rightStick * -1);
BL.setPower(!invert ? leftStick : rightStick * -1);
FR.setPower(!invert ? rightStick : leftStick * -1);
BR.setPower(!invert ? rightStick : leftStick * -1);
```

Here a ternary operator, splitting on the state of invert, decides which input doubles to use for each power. If the controls are inverted, then it will use the opposite stick, as well as inverting the direction of the stick, creating a fully inverted robot. This effectively swaps the front of the robot. The normal operation is with the front as the glyph placer - in invert mode, the front is the intake

## Results
While our drivers primarily drive in the non inverted mode, the invert function is immensely helpful in instances in which they get stuck or are otherwise forced to drive at odd angles, as the invert can make it easier for the driver to forsee how the robot will move. One example of this is when placing glyphs at the far cryptobox or when scoring a relic on the relic mat.
