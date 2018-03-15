# Brake Toggle

## Rationale
At the end of the match, we want to make sure the drivers can easily get back onto the balancing plate. However, doing this requires very fine control to end up balanced on the plate. This is a hard to accomplish feat, due to the drivers being forced to quickly react to being on the plate, in addition to the inherent lag in a FTC robot. One feature the drivers felt would be useful was a mode which greatly slowed down and stopped the robot immediately. This not only would solve the issue of getting onto the balancing plate, but also allow for a mode with finer control over the robots motion. This mode was implemented this in the Brake toggle

## Implementation
The implementation for this is located in the teleop itself, with handeling located in `Bot#tankDrive`.

It utilizes a automatic cooldown, which is discussed in more detail in another entry. This is used in the teleop to ensure the drivers can easily switch between the modes, without accidentaly swapping back. 

In the function `Bot#tankDrive`, it is passed a boolean to indicate whether the bot is in brake mode or not. It then sets the zero power behavior accordingly. 

Later on, when we are setting the values for `leftPower` and `rightPower`.

` leftPower = Range.clip((!invert ? leftStick : rightStick * -1) * (brake ? .4 : 1), -1, 1)`

Inside the setter, we have a ternary operator which chooses either .4 or 1; .4 if break mode is active, 1 if it is not. This allows for the slow down with break toggle.

## Result
This feature is used to great effect during the teleop modes. This can be used to help balance the robot during endgame, as well as allow for more fine tuned movement during normal operation times to ensure that glpyhs can be placed well, and avoid them falling out of the cryptobox.

