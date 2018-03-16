# Enum Controller

## Rationale

The glyph plate on our robot has multiple states it must be in, where a state is defined by servo position. We have in total 7 different positions for the glyph release mechanism. However, the code will sometimes attempt to move the glyph plate to 2 different positions simultaneously. For example, a driver may tell it to flip upwards to place a glyph, but be simultaneously running our intake; we have 2 different positions for each of the events - which one does the robot choose?

The first solution to this problem is found in Appendix D

It is a very poor solution, and hard to decipher.

```
   } else if (gamepad2.right_trigger > .3) {
       if (abnormalReleaseFlag) {
          ;
       } else {
          abnormalReleaseFlag = true;
          currentPosition = ReleasePosition.DOWN;
       }
       robot.intake(-1);
   } else {
```
This is a snippet of the code used. The way this algorithm worked relied on a boolean, `abnormalReleaseFlag`. It served as a mini priority flag - if it was true, it indicated that the value should not be changed from whatever it was set to. For example, the above snippet controlled the movement of the glyph when the intake was being raised or lowered; keeping the glyph plate in the same position would result in the robot breaking itself, from a motor fighting against a strong servo to both be in the same spot. However, later in the code, the portion which controls the placement of the glyphs, and moving the glyph plate into an upwards position, looked like this:

```
if (gamepad2.y) {
    currentPosition = ReleasePosition.UP;
    robot.flipUp();
    robot.backIntakeWallDown();
    wallCountdown = 30;
}
```

This part will silently override any value that appeared before it, as it ignores the state of `abnormalReleaseFlag`. This is bad, as it could easily cause issues that are hard to discover later as it is a complete override of the entire system. However, this is needed, as we don't want other states to interfere with palcing a glyph, as that could result in the glyph plate suddenly dropping while placing a glyph - not an ideal situation.

The ultimate solution to our problem was to implement a more robust priority system that allowed for more than one priority - essentially a priority queue.

## Implementation

The resulting solution is located in Appendix D, in the class `Utility/EnumController.java`

An EnumController accepts a generic object, which serves as the values it sorts through based on the priorities given.

It has 3 instance fields.
`defaultVal` - the value that is returned by the `EnumController#process` method if no elements have been added to the queue.
`instruction` - a arraylist that contains the objects in the queue
`priorities` - an arraylist that contains the priorities for the object in the same index in `instruction`

The only way to add values to the arraylists is through a function, add - this ensures that the lists will always be the same length.

The main aspect of the implementation is in `EnumController#process`

### `EnumController#process`

This method takes no inputs, and returns an object of the generic type passed to the EnumController during construction.

It functions by iterating over the 2 lists, and at each stage, comparing the priority at that index to the current maximum priority. It continues through the list, and returns whatever the highest priority object was. It is stored in a temporary variable, so one does not need to loop over the list again later to find the object.

In the case of equal priority, the method will take the first object of that priority.

If the instance field lists are size 0, the method will return `defaultVal`

Additionally, a negative priority will instantly exit the loop and return the negative priority object. This can be used in cases where one must be absolutely sure no other elements of the queue will override the certain object.

## Result

The `EnumController` greatly simplified managing the glyph plate position during Teleop. The examples given at the start change from

```
   } else if (gamepad2.right_trigger > .3) {
       abnormalReleaseFlag = true;
       currentPosition = ReleasePosition.DOWN;
       robot.intake(-1);
   } else {
```
to

```
if (gamepad2.right_trigger > .3) {
                controller.addInstruction(ReleasePosition.INIT, 10);
                robot.intake(-1);
}
```
and

```
if (gamepad2.y) {
    currentPosition = ReleasePosition.UP;
    robot.flipUp();
    robot.backIntakeWallDown();
    wallCountdown = 30;
}
```

to

```
if (gamepad2.y) {
    controller.addInstruction(ReleasePosition.UP, 5);
    robot.flipUp();
    robot.backIntakeWallDown();
    wallCountdown = 30;
}
```

This code is much clearer than the original version, and is also more robust - instead of just having 3 priorities, one of which was hard to use, we now have essentially unlimited priorities, allowing for much greater fleixbility when adding additional glyph plate states later on.
