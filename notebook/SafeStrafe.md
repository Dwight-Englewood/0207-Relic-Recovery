# Safe Strafe

## Rationale
This year, our bot utilizes a mecanum wheel drive train, allowing it to strafe from side to side. However, strafing is a process prone to error. Even a slight difference in speed of one of the motors will lead to the robot taking a path more akin to an arc than a straight line. While this is not a huge problem for the drivers, as they can quickly adjust any poor movement, this is not a solution that extends to the autonomous period. We wished to design a system for the bot to auto correct any non straight strafes, allowing us to safely use strafing during autonomous without fear of being thrown off of any alignment required later on in the opmode. This is especially useful when lining up to place a glyph, which is a movement particularly suited to strafing, while also requiring a high degree of accuracy.

## Implementation

The implementation of a safe strafe is found under `Bot#safeStrafe`

```java
public void safeStrafe(float targetHeading, boolean isRight, Telemetry telemetry, double powerCenter) {
       //Get the heading value from the REV IMU
       angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
       //Generate the error between the target and current headings.
       headingError = targetHeading - angles.firstAngle;
       //Multiply the error by a stable powerModifier, .0055. This is our P-term.
       driveScale = headingError * powerModifier;

       // Set the powers for the motors based on the proportional term.
       leftPower = powerCenter - driveScale;
       rightPower = powerCenter + driveScale;

       //Clip the powers to within an acceptable range. Note that negatives are not acceptable as they would spin the robot in entirely the wrong direction during a strafe.
       if (leftPower > 1)
           leftPower = 1;
       else if (leftPower < 0)
           leftPower = 0;

       if (rightPower > 1)
           rightPower = 1;
       else if (rightPower < 0)
           rightPower = 0;

       //Apply the powers for a right or left strafe, depending on the value of the isRight boolean.
       if (isRight) {
           FL.setPower(leftPower);
           FR.setPower(-rightPower);
           BL.setPower(-leftPower);
           BR.setPower(rightPower);
       } else {
           FL.setPower(-leftPower);
           FR.setPower(rightPower);
           BL.setPower(leftPower);
           BR.setPower(-rightPower);
       }
       telemetry.addData("leftPower", leftPower);
       telemetry.addData("rightPower", rightPower);
   }
```

The function takes the heading the robot should be moving at, a boolean for which direction the robot is strafing, a telemetry object for debugging, and the center for the power.

It first gets the current heading of the robot by polling the IMU values, and calculates the distance from the `targetHeading` It then calculates a `driveScale`, which is proportional to the error, and adds to power accordingly.

## Result

By using this, it ensures our strafes will remain true to the target angle. Thus, we can use strafes in autonomous without fear of the autonomous breaking down
