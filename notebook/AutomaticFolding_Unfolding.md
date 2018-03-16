# Automatic Folding/Unfolding of the Robot

## Rationale

As part of our robot's design, it is highly compact at the beginning of the match, to allow for the full robot to fit into the proper size limits. However, this poses a problem, as the bot should be able to unfold itself. If not for use in auton, at the very least this would allow the drivers to start a match more smoothly, as they do not need to think about the order in which the robot's part need to be moved to unfold the robot and allow it to operate completely, while simulatenously not tearing the entire robot apart from trying to have a part move through another. Additionally, having a folding function helps the drivers to park on the balancing stone in endgame as the center of mass of the bot is closer to its geometric center.

## Implementation
The implementation of the unfold in the autonomous mode:
```Java
    case 5:
      commandString = "Begin unfold";
      //Set the glyph plate to the DROP position
      robot.releaseMove(ReleasePosition.DROP);
      //Move the jewel arm out of the way
      robot.jewelOut();
      //Begin moving the intake down
      robot.intakeDrop.setPower(-1);
      //Move on to the next command
      timer.reset();
      command++;
      break;

    case 6:
      commandString = "Unfold";
      if (timer.milliseconds() > 1000) {
          //Put the glyph fork back down after another .5 seconds
          robot.flipDown();
          //Move on to the next command
          timer.reset();
          command++;
      } else if (timer.milliseconds() > 550) {
          //Stop dropping the intake after .5 seconds
          robot.intakeDrop.setPower(0);
          //Reset the glyph plate to the MIDDLE position
          robot.releaseMove(ReleasePosition.MIDDLE);
          //Flip the glyph fork up to move the glyph back into place on the plate
          robot.flipUp();
          //Bring the jewel arm back up
          robot.jewelUp();
      }
      break;
```

The implementation of the fold controls in Teleop:

This segment of code is what controls the position of most parts of the bot, dependent on whether or not the intake is folded up or not.
```Java
        //Use the encoder on the intake motor to tell if it is raised
        if (robot.intakeDrop.getCurrentPosition() >= 300) {
            //If so, move the glyph plate out of the way and raise the back intake wall out of the way.
            controller.addInstruction(ReleasePosition.DROP, 10);
            robot.backIntakeWallDown();

            //If folded, set these boolean flags to true so that the rest of the code can see that we are folded and not interfere.
            wallDown = true;
            parking = true;
        } else {
            wallDown = false;
            parking = false;
        }
```
And this controls the actual movement of the lift
```Java
        if (gamepad2.right_stick_y > .3) {
            //Lower the intake if it is currently lifted
            robot.intakeDrop.setPower(-1);
            robot.intake(.5);
            robot.jewelOut();
            //priority 10 since if this doesnt happen the robot will crush itself
            controller.addInstruction(ReleasePosition.DROP, 10);
            //Flags other sections of the program so that they do not interfere with the movement of the intake
            movingInt = true;
        } else if (gamepad2.right_stick_y < -.3) {
            robot.intakeDrop.setPower(1);
            //priority 10 since if this doesnt happen the robot will crush itself
            controller.addInstruction(ReleasePosition.DROP, 10);
            robot.intake(.5);
            robot.jewelOut();
            //Flags other sections of the program so that they do not interfere with the movement of the intake
            movingInt = true;
        //If interfering buttons are not being pressed, reset the intake's power and the jewel servo to their traditional locations
        } else if (!gamepad2.x && !(gamepad2.left_trigger > .1 || gamepad2.right_trigger > .1 || gamepad2.right_bumper)) {
            robot.intakeDrop.setPower(0);
            robot.intake(0);
            robot.jewelUp();
            movingInt = false;
        } else if (!(gamepad2.left_trigger > .1 || gamepad2.right_trigger > .1 || gamepad2.right_bumper)){
            movingInt = false;
            robot.intakeDrop.setPower(0);
            robot.intake(0);
        }
```



## Results
Thanks in large part to the driver's ability to press a single button and have the entire robot fold into itself, they have been able to get their parking time down to ~5 seconds including the fold. This means that the drive team can spend the maximum amount of time in teleop scoring glyphs and the relics as possible before they need to think about parking. This makes us a much more efficient team in teleop than we were before. 

Along with this, we have create mini-unfold functions that we can use in other code locations. For example, we have a method that will move the jewel knocker to a folded position, and another which moves it to a down position. While we no longer have a single unfold method, we ultimately have more modular control over the unfolding and folding of the many interwoven parts of the robot
