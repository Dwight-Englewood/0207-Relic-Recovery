# Safe Strafe

## Rationale
This year, our bot utilizes a mecanum wheel drive train, allowing it to strafe from side to side. However, strafing is a process prone to error. Even a slight difference in speed of one of the motors will lead to the robot taking a path more akin to an arc than a straight line. While this is not a huge problem for the drivers, as they can quickly adjust any poor movement, this is not an solution that extends to the autonomous. We wished to design a system for the bot to auto correct any non straight strafes, allowing us to safely use strafing during autonomous without fear of strafing throwing off any alignment requried later on in the teleop. This is especially useful when lining up to place a glyph, which is a movement particularly suited to strafing, while also requiring a high degree of accuracy. 

## Implementation

The implementation of a safe strafe is found under `Bot#safeStrafe`



The function takes the heading the robot should be moving, a boolean for which direction the robot is strafing, a telemetry object for debugging, and the center for the power.

It first gets the current heading of the robot by polling the IMU values, and calculates the distance from the `targetHeading` It then calculates a `driveScale`, which is proportional to the error, and adds to power accordingly.

## Result

By using this, it ensures our strafes will remain true to the target angle. Thus, we can use strafes in autonomous without fear of the autonomous breaking down


