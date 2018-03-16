# Heading Adjustment
## Rationale
In autonomous modes, we often find that the robot needs to be positioned in a specific way in order to complete the task at hand. Essentially, we need the robot to be oriented to a specific heading value. The adjust heading accomplishes this for use in all of our autonomous programs.

##Implementation
The way we do our heading adjustments is by polling the REV hub IMU for its heading value, then comparing it to a given target and using a P-Loop to complete the turn without over-shooting.

```java
public void adjustHeading(int targetHeading, boolean slow) {
       //Initialize the turnleft boolean.
       boolean turnLeft = false;

       //Get the current heading from the imu.
       float curHeading = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;

       //If within a reasonable degree of error of the target heading, set power to zero on all motors and leave the function.
       if (Math.abs(curHeading - targetHeading) <= .5) {
           drive(MovementEnum.STOP);
           return;
       }
       //Generate our proportional term
       float powFactor = Math.abs(targetHeading - curHeading) * (float) (slow ? .0055 : .02);

       //Choose how to turn based on given target
       switch (targetHeading) {
           case 0:
               turnLeft = curHeading <= 0;
               break;

           case 90:
               turnLeft = !(curHeading <= -90 || curHeading >= 90);
               break;

           case 180:
               turnLeft = !(curHeading <= 0);
               break;

           case -90:
               turnLeft = curHeading <= -90 || curHeading >= 90;
               break;

           case 45:
               turnLeft = !(curHeading <= -45 || curHeading >= 45);
               break;

           case -45:
               turnLeft = curHeading <= -45 || curHeading >= 45;
               break;

       }

       //Clip the powers to within an acceptable range for the motors and apply the proportional factor.
       leftPower = Range.clip((turnLeft ? -1 : 1) * powFactor, -1, 1);
       rightPower = Range.clip((turnLeft ? 1 : -1) * powFactor, -1, 1);

       //Set power to all motors
       FL.setPower(leftPower);
       BL.setPower(leftPower);
       FR.setPower(rightPower);
       BR.setPower(rightPower);

   }
```

##Results
Overall, we are very happy with this implementation of heading adjustment. It is able to very accurately move the robot to a given heading and the proportional term helps to prevent slippagevery effectively by slowing the robot down as it approaches the target. 
