#Scaled Drive Speed
##Rationale
Early on in the testing of our autonomous modes, we noticed that the mecanum wheels we use are very prone to slipping after a movement. In order to combat this, we implemented a couple of features, the first and most important being scaled drive speed. Using the encoders attached to our drive motors, the robot is able to accurately speed up and slow down during movements in autonomous. The goal being that such sclaed speed increases and decreases will make our autonomous modes more accurate overall.

##Implementation
The function slowDownScale takes as input the current encoder values of all drive train motors, as well as the target for each of them. By taking individual targets we are able to adapt this function for all types of movement, be it driving forward, turning or strafing on the mecanum wheels.

```java
public double slowDownScale(int tickFL, int tickFR, int tickBL, int tickBR, int targetTickFL, int targetTickFR, int targetTickBL, int targetTickBR) {
        double scale;
        //If all of the drive train motors are within 25 ticks of the target, stop movement.
        if (
                (Math.abs(tickFL - targetTickFL) < 25) &&
                        (Math.abs(tickFR - targetTickFR) < 25) &&
                        (Math.abs(tickBL - targetTickBL) < 25) &&
                        (Math.abs(tickBR - targetTickBR) < 25)
                ) {
            scale = 0;
        //If all of the drive train motors are within 200 ticks of the starting point, scale speed by .1
        } else if (
                (Math.abs(tickFL) < 200) &&
                        (Math.abs(tickFR) < 200) &&
                        (Math.abs(tickBL) < 200) &&
                        (Math.abs(tickBR) < 200)
                ) {
            scale = .1;
        //If all of the drive train motors are within 1000 ticks of the starting point, scale speed by .3
        } else if (
                (Math.abs(tickFL) < 1000) &&
                        (Math.abs(tickFR) < 1000) &&
                        (Math.abs(tickBL) < 1000) &&
                        (Math.abs(tickBR) < 1000)
                ) {
            scale = .3;
        //If all of the drive train motors are within 500 ticks of the target, scale speed by .1
        } else if (
                (Math.abs(tickFL - targetTickFL) < 500) &&
                        (Math.abs(tickFR - targetTickFR) < 500) &&
                        (Math.abs(tickBL - targetTickBL) < 500) &&
                        (Math.abs(tickBR - targetTickBR) < 500)
                ) {
            scale = .1;
        //If all of the drive train motors are within 2000 ticks of the target, scale speed by .3
        } else if (
                (Math.abs(tickFL - targetTickFL) < 2000) &&
                        (Math.abs(tickFR - targetTickFR) < 2000) &&
                        (Math.abs(tickBL - targetTickBL) < 2000) &&
                        (Math.abs(tickBR - targetTickBR) < 2000)
                ) {
            scale = .3;

        //If all of the drive train motors are within 2500 ticks of the target, scale speed by .5
        } else if (
                (Math.abs(tickFL - targetTickFL) < 2500) &&
                        (Math.abs(tickFR - targetTickFR) < 2500) &&
                        (Math.abs(tickBL - targetTickBL) < 2500) &&
                        (Math.abs(tickBR - targetTickBR) < 2500)
                ) {
            scale = .5;
        //If all of the drive train motors are within 3500 ticks of the target, scale speed by .7
        } else if (
                (Math.abs(tickFL - targetTickFL) < 3500) &&
                        (Math.abs(tickFR - targetTickFR) < 3500) &&
                        (Math.abs(tickBL - targetTickBL) < 3500) &&
                        (Math.abs(tickBR - targetTickBR) < 3500)
                ) {
            scale = .7;
        //Else scale speed by 1 (Full Speed!)
        } else {
            scale = 1;
        }
        return scale;
    }
```

##Results

This function has been applied throughout our autonomous programs and has helped to make our 85 point autonomous very consistent. One such use case can be seen when we are approaching the cryptobox. If this drive were not consistent, then the rest of our autonomous would be messed up. However, because the robot always goes to the same spot, we are able to program in relatively hard values with little stretch room without the worry of inconsistency. This function has made our programming experience much simpler and our robot's performance much better this season. 
