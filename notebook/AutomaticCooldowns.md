# Automatic Cooldowns of Walls

## Rationale
In the initial robot design, glyphs would often slide through the front of the glyph placer, after being intaked or pushed forward slightly by a servo. To combat this, the builders placed a small vertical plate in front of the glyph placer to prevent glyphs from falling out. Of course, this wall must also be moved when we want to place glyphs.

There are two control options for the wall. We could have the drivers press a button to toggle the position of the wall, or we could have the wall be moved for a set amount of time, then move back into place. Both of these methods require a cooldown to be implemented in the code. 

There are other functions of the bot which are dependent on timing. We also have various toggles on the robot for different driver modes : see driver modes documentation : 
To control these toggles, the drivers must press a button; however, without cooldown implementations, the toggle must only be true for an odd number of loops in the teleop. Any other number will result in the toggle flipping back to what it was beforehand. Nothing would change for the drivers. This requires a cooldown on the ability to toggle the driver control.

## Implementation

The implementation of this driver enhancement is located in the `Telebop/Telebop.java` class.

Each cooldown has an associated `int` that stores the current cooldown value, which is then used by different tests for different functionalities

### Intake Wall

```
if (gamepad1.right_bumper) {
    controller.addInstruction(ReleasePosition.UP, 5);
    robot.flipUp();
    robot.backIntakeWallDown();
    wallCountdown = 55;
} else if (wallCountdown <= 0) {
    controller.addInstruction(ReleasePosition.MIDDLE, 0);
    robot.backIntakeWallUp();
}

wallCountdown--;
```

The back wall is tied to the same control as the glyph placer. When we place glyphs, we also lower the intake wall. Whenever, the glyph placer is in the up position, we set the cooldown to 55 teleop loops. 

If the dirvers are not placing a glyph, then we test for the cooldown being less than 0; has 55 iterations of the teleop loop occured? If so, then the robot will raise the intake wall automatically.

### Driver Mode Cooldowns

```
if (gamepad1.start && relicCountdown <= 0) {
    glyphMode = !glyphMode;
    relicCountdown = 30;
}

relicCountdown--;
```

This functions similarly. To swap modes, the cooldown must be less than 0, to indiate that at least 30 loops have passed since the last mode swap.

## Results

The automatic cooldowns on various functions of the robot allow the drivers to concentrate less on micro managing the robot's functions and focus more on placing glyphs.
