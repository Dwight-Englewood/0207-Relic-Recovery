# Automatic Unfolding of the Robot

## Rationale

As part of our robot's design, it is highly compact at the beginning of the match, to allow for the full robot to fit into the proper size limits. However, this poses a problem, as the bot should be able to unfold itself. If not for use in auton, at the very least this would allow the drivers to start a match more smoothly, as they do not need to think about the order in which the robot's part need to be moved to unfold the robot and allow it to operate completely, while simulatenously not tearing the entire robot apart from trying to have a part move through another. 

## Implementation

The method


## Results
Ultimately, this method did not prove as useful as we had hoped. During our autonomous rountine, we end up unfolding the robot so we can read Vuforia, and place the glyph into the column. The drivers do not need this function, as the bot is already unfolded at the start of Teleop period. Additionally, the unfolding of the robot during auton happens gradually - we do not unfold all at once, instead unfolding parts as they are needed.

We did however, create mini-unfold functions that we can use in other code locations. For example, we have a method that will move the jewel knocker to a folded position, and another which moves it to a down position. While we no longer have a single unfold method, we ultimately have more modular control over the unfolding and folding of the many inter woven parts of the robot
