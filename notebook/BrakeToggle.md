# Brake Toggle

## Rationale
At the end of every match, we want to make sure the drivers can easily get back onto the balancing plate. However, doing this requires very fine control to end up balanced on the plate. This is difficult for the drivers to accomplish due to their being forced to quickly react to the plateas well as the inherent lag built into the control system of a FTC robot. One feature the drivers felt would be useful was a mode which greatly slowed down and stopped the robot immediately. This not only would solve the issue of getting onto the balancing plate, but also allow for a mode with finer control over the robots motion. This mode was implemented as a brake toggle.

## Implementation
The implementation for this is located in the teleop itself, with handeling located in `Bot#tankDrive`.

It utilizes a automatic cooldown, which is discussed in more detail in the writeup on Automatic Cooldowns. This is used in the teleop to ensure the drivers can easily switch between the modes, without accidentaly swapping back.

The function `Bot#tankDrive`, is passed a boolean to indicate whether the bot is in brake mode or not. It then sets the zero power behavior accordingly and scales down the input power values.

```java
leftPower = Range.clip((!invert ? leftStick : rightStick * -1) * (brake ? .4 : 1), -1, 1)
```

Inside the setter, we have a ternary operator which chooses either .4 or 1; .4 if break mode is active, 1 if it is not. This allows for the slow down with break toggle.

## Result
This feature is used to great effect within the teleop modes. In endgame, our drivers have successfully been able to get their total balance time down to ~5 seconds with this mode enabled. This allows us to score more points during teleop without worrying about being unable to park for the 20 extra at the end. The brake toggle has had a significant positive effect on our drive team's ability to score points.
